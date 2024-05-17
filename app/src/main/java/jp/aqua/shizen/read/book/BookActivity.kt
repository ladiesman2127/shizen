package jp.aqua.shizen.read.book

import android.graphics.PointF
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
import jp.aqua.shizen.read.reader.presentation.ReaderViewModel
import jp.aqua.shizen.ui.theme.ShizenTheme
import org.readium.r2.navigator.VisualNavigator
import org.readium.r2.navigator.epub.EpubNavigatorFragment
import org.readium.r2.navigator.input.InputListener
import org.readium.r2.navigator.input.TapEvent
import org.readium.r2.shared.ExperimentalReadiumApi
import org.readium.r2.shared.util.AbsoluteUrl

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
