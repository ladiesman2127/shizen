package jp.aqua.shizen.read.reader.data

import jp.aqua.shizen.item.model.Item
import jp.aqua.shizen.item.model.TocEntry
import jp.aqua.shizen.read.book.data.BookDao
import jp.aqua.shizen.utils.Readium
import org.json.JSONObject
import org.readium.r2.navigator.epub.EpubNavigatorFactory
import org.readium.r2.shared.ExperimentalReadiumApi
import org.readium.r2.shared.publication.Locator
import org.readium.r2.shared.publication.Publication
import org.readium.r2.shared.util.Error
import org.readium.r2.shared.util.Try
import org.readium.r2.shared.util.getOrElse
import java.io.File

@OptIn(ExperimentalReadiumApi::class)
class ReaderRepository(
    private val readium: Readium,
    private val bookDao: BookDao
) {
    lateinit var bookInitData: BookInitData

    suspend fun updateCurrentBook(book: Item, tocEntry: TocEntry?): Try<BookInitData, Error> {
        val publicationFile = File(book.href)
        val asset = readium.assetRetriever
            .retrieve(publicationFile)
            .getOrElse {
                return Try.failure(it)
            }
        val publication = readium.publicationOpener
            .open(asset = asset, allowUserInteraction = false)
            .getOrElse {
                return Try.failure(it)
            }
        val navigatorFactory = EpubNavigatorFactory(publication)
        val initialLocator = Locator.fromJSON(JSONObject(tocEntry?.href ?: book.progression))
        bookInitData = BookInitData(
            book = book,
            publication = publication,
            navigatorFactory = navigatorFactory,
            initialLocator = initialLocator
        )
        return Try.success(bookInitData)
    }

    suspend fun updateProgression(locator: Locator) =
        bookDao.updateBook(
            bookInitData.book.copy(
                progression = locator.toJSON().toString()
            )
        )
}

@OptIn(ExperimentalReadiumApi::class)
data class BookInitData(
    val book: Item,
    val publication: Publication,
    val initialLocator: Locator? = null,
    val navigatorFactory: EpubNavigatorFactory
)
