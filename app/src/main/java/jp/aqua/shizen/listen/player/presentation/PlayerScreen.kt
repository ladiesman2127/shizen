package jp.aqua.shizen.listen.player.presentation

import android.annotation.SuppressLint
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import jp.aqua.shizen.dictionary.worddialog.WordDialog
import jp.aqua.shizen.dictionary.worddialog.WordDialogUiState
import jp.aqua.shizen.listen.player.PlayerViewModel
import jp.aqua.shizen.utils.extensions.getText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel,
    wordDialogUiState: WordDialogUiState,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {


    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }



    if (wordDialogUiState.isWordSelected) {
        WordDialog(
            selectedWord = wordDialogUiState.selectedWord,
            selectedSentence = wordDialogUiState.selectedSentence,
            selectedWordImages = wordDialogUiState.selectedWordImages,
            isTranslationVisible = wordDialogUiState.isTranslationVisible,
            translatedWord = wordDialogUiState.translatedWord,
            translatedSentence = wordDialogUiState.translatedSentence,
            onOpenWordDef = viewModel::onOpenWordDef,
            onCloseWordDef = {
                viewModel.onCloseWordDef()
                lifecycle = Lifecycle.Event.ON_RESUME
            },
            onUpdateIsTranslationVisible = viewModel::updateIsTranslationVisible
        )
    }



    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                this.player = viewModel.player
                this.subtitleView?.setUserDefaultStyle()
                this.setShowSubtitleButton(true)
                this.subtitleView?.setOnClickListener {
                    onPause()
                    player?.pause()
                    val selectedWord = ""
                    val selectedSentence = this.player?.currentCues?.cues
                        ?.map {
                            it.text ?: runBlocking {
                                return@runBlocking it.bitmap?.getText()
                            }
                        }
                        ?.joinToString(" ") ?: ""
                    if (selectedSentence.isNotBlank() && selectedSentence != "null") {
                        coroutineScope.launch {
                            viewModel.onOpenWordDef(selectedWord, selectedSentence)
                        }
                    }
                }
                this.subtitleView?.setOnLongClickListener { _ ->
                    if (!isControllerFullyVisible) {
                        this.showController()
                    } else {
                        this.hideController()
                    }
                    true
                }
            }
        },
        update = { playerView ->
            when (lifecycle) {
                Lifecycle.Event.ON_PAUSE -> {
                    playerView.onPause()
                    playerView.player?.pause()
                }

                Lifecycle.Event.ON_RESUME -> {
                    playerView.onResume()
                }

                else -> Unit
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
