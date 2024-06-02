package jp.aqua.shizen

import jp.aqua.shizen.dictionary.deck.data.DeckDatabase
import jp.aqua.shizen.dictionary.deck.data.DeckRepository
import jp.aqua.shizen.dictionary.knownwords.data.KnownWordsDao
import jp.aqua.shizen.dictionary.knownwords.data.KnownWordsDatabase
import jp.aqua.shizen.listen.video.data.VideoDatabase
import jp.aqua.shizen.listen.video.data.VideoRepository
import jp.aqua.shizen.read.book.data.BookDao
import jp.aqua.shizen.read.book.data.BookDatabase
import jp.aqua.shizen.read.book.data.BookRepository
import jp.aqua.shizen.read.reader.data.ReaderRepository
import jp.aqua.shizen.utils.Readium
import java.io.File

class Application : android.app.Application() {
    // Необходим для обработки и отображения книг
    lateinit var readium: Readium
    // Необходим для создания BookRepository
    lateinit var bookDao: BookDao
    // Необходим для создания BookRepository
    lateinit var wordsDao: KnownWordsDao
    lateinit var bookRepository: BookRepository
        private set
    lateinit var videoRepository: VideoRepository
        private set
    lateinit var deckRepository: DeckRepository
        private set
    lateinit var readerRepository: ReaderRepository
        private set
    // Директория для хранение файлов приложения
    private lateinit var storageDir: File
    // Директория для хранение обложек книг
    private lateinit var coverDir: File


    override fun onCreate() {
        super.onCreate()
        storageDir = File(filesDir?.path + "/")
        coverDir = File(storageDir, "covers/").also {
            if (!it.exists()) it.mkdir()
        }
        readium = Readium(this)
        wordsDao = KnownWordsDatabase.getDatabase(this).knownWordsDao()
        bookRepository =
            BookDatabase.getDatabase(this).bookDao()
                .let { dao ->
                    bookDao = dao
                    BookRepository(readium, dao, wordsDao, storageDir, coverDir)
                }
        videoRepository =
            VideoDatabase.getDatabase(this).videoDao()
                .let { dao ->
                    VideoRepository(
                        dao
                    )
                }
        deckRepository =
            DeckDatabase.getDatabase(this).deckDao()
                .let { dao ->
                    DeckRepository(
                        dao,
                        storageDir
                    )
                }
        readerRepository = ReaderRepository(readium, bookDao, wordsDao)
    }
}





