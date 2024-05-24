package jp.aqua.shizen.read.book

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import jp.aqua.shizen.AppViewModelProvider
import jp.aqua.shizen.read.book.presentation.BookScreen
import jp.aqua.shizen.read.book.presentation.BookViewModel
import jp.aqua.shizen.ui.theme.ShizenTheme

class BookActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            ShizenTheme {
                val context = LocalContext.current
                val viewModel: BookViewModel by viewModels<BookViewModel> {
                    AppViewModelProvider.Factory
                }
                val uiState by viewModel.uiState.collectAsState()
                BookScreen(
                    context = context,
                    viewModel = viewModel,
                    uiState = uiState,
                    onNavigateUp = {
                        finish()
                    }
                )
            }
        }
    }
}
