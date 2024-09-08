package jp.aqua.shizen.read.reader.presentation

import android.annotation.SuppressLint
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModel
import jp.aqua.shizen.dictionary.worddialog.WordDialog
import jp.aqua.shizen.dictionary.worddialog.WordDialogUiState
import jp.aqua.shizen.item.model.LoadingStatus
import jp.aqua.shizen.utils.scaffold.ShizenTopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.readium.r2.shared.ExperimentalReadiumApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalReadiumApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReaderScreen(
    viewModel: ReaderViewModel,
    uiState: ReaderUiState,
    wordDialogUiState: WordDialogUiState,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    updateContainer: (Int) -> Unit,
    onCloseReader: () -> Unit,
) {
    val page by viewModel.page.collectAsState()
    if (wordDialogUiState.isWordSelected) {
        WordDialog(
            selectedWord = wordDialogUiState.selectedWord,
            selectedSentence = wordDialogUiState.selectedSentence,
            selectedWordImages = wordDialogUiState.selectedWordImages,
            isTranslationVisible = wordDialogUiState.isTranslationVisible,
            translatedWord = wordDialogUiState.translatedWord,
            translatedSentence = wordDialogUiState.translatedSentence,
            onOpenWordDef = viewModel::onOpenWordDef,
            onCloseWordDef = viewModel::onCloseWordDef,
            onUpdateIsTranslationVisible = viewModel::updateIsTranslationVisible,
        )
    }

    Scaffold(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            if (uiState.isShowBars)
                ShizenTopBar(
                    title = uiState.bookTitle,
                    navigationIcon = {
                        IconButton(onClick = onCloseReader) {
                            Icon(
                                Icons.AutoMirrored.Rounded.ArrowBack,
                                "Close the book"
                            )
                        }
                    }
                )
        },
    ) {
        Box(Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
                factory = { context ->
                    FragmentContainerView(context).apply {
                        id = View.generateViewId()
                    }
                },
                update = { container ->
                    updateContainer(container.id)
                },
            )
            if (uiState.status == LoadingStatus.Loading) {
                Column(
                    Modifier
                        .padding(it)
                        .fillMaxWidth()
                        .clickable { viewModel.updateIsShowBars() },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.fillMaxSize())
                    LinearProgressIndicator(
                        progress = { uiState.progress },
                        Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = "2/2",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}