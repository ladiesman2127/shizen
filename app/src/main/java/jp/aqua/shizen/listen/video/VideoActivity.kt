package jp.aqua.shizen.listen.video

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.aqua.shizen.AppViewModelProvider
import jp.aqua.shizen.R
import jp.aqua.shizen.item.model.Item
import jp.aqua.shizen.item.presentation.ItemDescriptionScreen
import jp.aqua.shizen.listen.player.PlayerActivity
import jp.aqua.shizen.listen.video.presentation.VideoScreen
import jp.aqua.shizen.listen.video.presentation.VideoViewModel
import jp.aqua.shizen.ui.theme.ShizenTheme
import jp.aqua.shizen.utils.dialog.ShizenFullScreenDialog
import jp.aqua.shizen.utils.getFileName
import kotlinx.coroutines.launch

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
