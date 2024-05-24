package jp.aqua.shizen

import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.aqua.shizen.item.presentation.ItemViewModel
import jp.aqua.shizen.read.book.presentation.BookViewModel
import jp.aqua.shizen.read.presentation.ReadViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class BookTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun add_book_works() {
        composeTestRule.setContent {
            val viewModel: ReadViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val uriString = "content://com.android.providers.downloads" +
                    ".documents/document/msf%3A1000000034"
            val uri = Uri.parse(uriString)
            val context = LocalContext.current
            runBlocking {
                assert(viewModel.addBook(uri, context).isSuccess)
            }
        }
    }
}

