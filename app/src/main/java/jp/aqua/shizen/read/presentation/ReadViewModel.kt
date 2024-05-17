package jp.aqua.shizen.read.presentation

import android.content.Context
import android.net.Uri
import jp.aqua.shizen.read.book.data.BookRepository
import jp.aqua.shizen.utils.ScreenViewModel

/**
 * View model for Read Screen
 *
 * Contains methods to access Room DB through [BookRepository]
 */
class ReadViewModel(
    private val bookRepository: BookRepository,
) : ScreenViewModel(bookRepository) {
    override suspend fun addBook(uri: Uri, context: Context) = bookRepository.addBook(uri, context)
}

