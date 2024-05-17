package jp.aqua.shizen.read.reader

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.commit
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.aqua.shizen.AppViewModelProvider
import jp.aqua.shizen.read.reader.presentation.ReaderScreen
import jp.aqua.shizen.read.reader.presentation.ReaderViewModel
import jp.aqua.shizen.ui.theme.ShizenTheme
import kotlinx.coroutines.launch
import org.readium.r2.shared.ExperimentalReadiumApi

class ReaderActivity : AppCompatActivity() {
    @OptIn(ExperimentalReadiumApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel: ReaderViewModel by viewModels { AppViewModelProvider.Factory }
        supportFragmentManager.fragmentFactory = viewModel.uiState.value.navigatorFactory
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.navigationBars())
            hide(WindowInsetsCompat.Type.statusBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        super.onCreate(savedInstanceState)
        setContent {
            val uiState by viewModel.uiState.collectAsState()
            val wordDialogUiState by viewModel.wordDialogUiState.collectAsState()
            val coroutineScope = rememberCoroutineScope()
            ShizenTheme {
                LaunchedEffect(uiState.containerID) {
                    supportFragmentManager.commit {
                        uiState.fragment?.let { fragment ->
                            uiState.containerID?.let { id ->
                                try {
                                    replace(id, fragment)
                                } catch (e: Exception) {
                                    Log.e("READER", e.stackTraceToString())
                                }
                            }
                        }
                    }
                }
                ReaderScreen(
                    uiState = uiState,
                    wordDialogUiState = wordDialogUiState,
                    viewModel = viewModel,
                    onCloseReader = {
                        coroutineScope.launch {
                            viewModel.updateProgression()
                            finish()
                        }
                    },
                    updateContainer = { id ->
                        Log.i("UPDATE CONTAINER", id.toString())
                        viewModel.updateContainer(id)
                    }
                )
            }
        }
    }
}
