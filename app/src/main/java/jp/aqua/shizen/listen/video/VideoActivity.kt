package jp.aqua.shizen.listen.video

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import jp.aqua.shizen.AppViewModelProvider
import jp.aqua.shizen.listen.video.presentation.VideoScreen
import jp.aqua.shizen.listen.video.presentation.VideoViewModel
import jp.aqua.shizen.ui.theme.ShizenTheme

class VideoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            ShizenTheme {
                val context = LocalContext.current
                val viewModel: VideoViewModel by viewModels<VideoViewModel> {
                    AppViewModelProvider.Factory
                }
                val uiState by viewModel.uiState.collectAsState()
                Log.i("uistate update", uiState.tableOfContents.map { it.status }.joinToString())
                VideoScreen(
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
