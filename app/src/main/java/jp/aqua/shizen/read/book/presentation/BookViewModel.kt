package jp.aqua.shizen.read.book.presentation

import androidx.lifecycle.SavedStateHandle
import jp.aqua.shizen.item.model.TocEntry
import jp.aqua.shizen.item.presentation.ItemViewModel
import jp.aqua.shizen.read.book.data.BookRepository
import jp.aqua.shizen.read.reader.data.BookInitData
import jp.aqua.shizen.read.reader.data.ReaderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.readium.r2.shared.util.Try
import org.readium.r2.shared.util.Error

class BookViewModel(
    savedStateHandle: SavedStateHandle,
    private val bookRepository: BookRepository,
    private val readerRepository: ReaderRepository
) : ItemViewModel(savedStateHandle, bookRepository) {
    suspend fun updateCurrentBook(bookID: Int?, tocEntry: TocEntry?): Try<BookInitData, Error>? =
        withContext(Dispatchers.IO) {
            bookID?.let { id ->
                val book = bookRepository.getItemImmediately(id)
                val res = readerRepository.updateCurrentBook(book, tocEntry)
                res
            }
        }
}
