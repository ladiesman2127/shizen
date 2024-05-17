package jp.aqua.shizen.dictionary.deck.data

import android.content.Context
import android.net.Uri
import jp.aqua.shizen.dictionary.utils.WordCard
import jp.aqua.shizen.item.data.ItemRepository
import jp.aqua.shizen.item.model.Item
import jp.aqua.shizen.utils.getFileName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class DeckRepository(
    private val deckDao: DeckDao,
    private val storageDir: File,
) : ItemRepository {

    suspend fun addDeck(
        title: String,
        cover: String? = null,
    ) {
        val decksDir = File(storageDir, "decks/")
        if (!decksDir.exists())
            decksDir.mkdir()
        val currentDeckDir = File(decksDir, UUID.randomUUID().toString())
        currentDeckDir.mkdir()
        deckDao.insertDeck(
            Item(
                href = currentDeckDir.path,
                title = title,
                cover = cover,
                creation = DateTime().toDate().time,
                language = "",
            )
        )
    }

    suspend fun storeContent(
        uri: Uri,
        context: Context,
        deckDir: File
    ): File = withContext(Dispatchers.IO) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(deckDir, uri.getFileName(context))
        val outputStream = FileOutputStream(file)
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        inputStream?.close()
        outputStream.close()
        file
    }

    suspend fun storeIndexFile(
        deckDir: File,
        front: String,
        back: String
    ): File = withContext(Dispatchers.IO) {
        val indexFileData = WordCard.Builder()
            .setFront(front)
            .setBack(back)
            .build()
            .toByteArray()
        val cardDir = File(deckDir, "/${UUID.randomUUID()}").apply { mkdir() }
        val indexFile = File(cardDir, "index.html")
        val outputStream = FileOutputStream(indexFile)
        outputStream.write(indexFileData)
        outputStream.close()
        indexFile
    }

    override fun getAllItems(): Flow<List<Item>> = deckDao.getAllDecks()

    override fun getItem(id: Int): Flow<Item> = deckDao.getDeck(id)

    override suspend fun deleteItem(item: Item) = deckDao.deleteDeck(item)

    override suspend fun insertItem(item: Item) = deckDao.insertDeck(item)

    override suspend fun updateItem(item: Item) = deckDao.updateDeck(item)
}