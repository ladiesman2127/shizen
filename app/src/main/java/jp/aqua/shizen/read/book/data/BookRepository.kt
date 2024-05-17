package jp.aqua.shizen.read.book.data

import android.content.Context
import android.net.Uri
import android.util.Log
import jp.aqua.shizen.item.data.ItemRepository
import jp.aqua.shizen.item.model.Item
import jp.aqua.shizen.item.model.TocEntry
import jp.aqua.shizen.read.book.extensions.storeCoverImage
import jp.aqua.shizen.read.book.extensions.storeParsedBook
import jp.aqua.shizen.utils.Readium
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import nl.siegmann.epublib.epub.*
import nl.siegmann.epublib.domain.Book
import org.joda.time.DateTime
import org.readium.r2.shared.util.getOrElse
import java.io.File
import java.io.IOException

/**
 * Repository of books, which helps to retrieve and store books.
 * @param bookDao BookDao
 * @param storageDir Dir where copy of the book will be stored
 */
class BookRepository(
    private val readium: Readium,
    private val bookDao: BookDao,
    private val storageDir: File,
    private val coverDir: File
) : ItemRepository {
    override fun getAllItems(): Flow<List<Item>> = bookDao.getAllBooks()

    override fun getItem(id: Int): Flow<Item> = bookDao.getBook(id)

    fun getItemImmediately(id: Int): Item = bookDao.getBookImmediately(id)

    override suspend fun insertItem(item: Item) = bookDao.insertBook(item)

    override suspend fun deleteItem(item: Item) {
        val itemFolder = File(item.href).parentFile
        itemFolder?.deleteRecursively()
        bookDao.deleteBook(item)
    }

    override suspend fun updateItem(item: Item) = bookDao.updateBook(item)

    suspend fun addBook(
        uri: Uri,
        context: Context
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            // Входной поток файла
            val inputStream = context.contentResolver.openInputStream(uri)
            // Книга обработанная через библиотеку EpubLib
            val book = EpubReader().readEpub(inputStream)
            inputStream?.close()
            val bookDir = book.storeParsedBook(storageDir)
            val bookCover = book.storeCoverImage(coverDir)
            val toc = getToc(readium, bookDir)
            // Добавление книги в базу данных
            insertBookIntoDatabase(bookDir.path, bookCover.path, book, toc)
            // Успешное добавление книги
            Result.success(book.title.value)
        } catch (e: IOException) {
            // Вывод ошибки при добавлении книги в консоль
            e.printStackTrace()
            // Возврат информации о неуспешном добавлении книги
            Result.failure(e)
        }
    }

    private suspend fun getToc(
        readium: Readium,
        bookDir: File
    ): List<TocEntry> =
        withContext(Dispatchers.IO) {
            Log.i("toc BOOK DIR", bookDir.path)
            val asset = readium.assetRetriever.retrieve(bookDir)
                .getOrElse {
                    Log.e("Error in asset retrieving in getToc", it.message)
                    return@withContext emptyList()
                }
            val publication =
                readium.publicationOpener.open(asset, allowUserInteraction = false).getOrElse {
                    Log.e("Error in publication opening in getToc", it.message)
                    return@withContext emptyList()
                }
            publication.tableOfContents.map {
                TocEntry(
                    title = it.title ?: "No toc name",
                    href = publication.locatorFromLink(it)?.toJSON().toString()
                )
            }
        }

    private suspend fun insertBookIntoDatabase(
        href: String,
        coverHref: String,
        book: Book,
        toc: List<TocEntry>
    ) = bookDao.insertBook(
        Item(
            href = href,
            cover = coverHref,
            creation = DateTime().toDate().time,
            title = book.title.value,
            authors = book.metadata.authors.map { it.toString() },
            language = book.metadata.language,
            tableOfContents = toc,
            progression = "{}",
            description = book.metadata.descriptions.firstOrNull(),
        )

    )
}
