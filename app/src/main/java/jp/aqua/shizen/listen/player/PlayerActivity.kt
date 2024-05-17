package jp.aqua.shizen.listen.player

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.util.UnstableApi
import jp.aqua.shizen.AppViewModelProvider
import jp.aqua.shizen.listen.player.presentation.PlayerScreen
import jp.aqua.shizen.ui.theme.ShizenTheme

class PlayerActivity : ComponentActivity() {
    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.displayCutout())
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        enableEdgeToEdge()
        setContent {
            ShizenTheme {
                val viewModel: PlayerViewModel by viewModels<PlayerViewModel> { AppViewModelProvider.Factory }
                val wordDialogUiState by viewModel.wordDialogUiState.collectAsState()
                PlayerScreen(
                    viewModel = viewModel,
                    wordDialogUiState = wordDialogUiState,
                )
            }
        }
    }
}