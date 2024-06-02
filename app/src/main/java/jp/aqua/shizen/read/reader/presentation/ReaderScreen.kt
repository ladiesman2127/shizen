package jp.aqua.shizen.read.reader.presentation

import android.annotation.SuppressLint
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
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
    LaunchedEffect(uiState.fragment?.isVisible, page) {
        if (uiState.fragment?.isVisible == true) {
            uiState.fragment.evaluateJavascript(
                "console.log('LE')\n" +
                        "document.querySelectorAll('word').forEach((word) => {\n" +
                        "   console.log(word.textContent)\n" +
                        "   if(android.isKnownWord(word.textContent)) {\n" +
                        "       word.setAttribute('style', 'background-color: transparent;')\n" +
                        "   } else {\n" +
                        "       word.setAttribute('style', 'background-color: yellow;')\n" +
                        "   }\n" +
                        "})\n" +
                        "console.log('END')\n"
            )
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
                    LinearProgressIndicator(
                        progress = { uiState.progress },
                        Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    )
                }
            }
        }
    }
}