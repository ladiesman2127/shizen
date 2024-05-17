package jp.aqua.shizen.read.reader.presentation

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import jp.aqua.shizen.dictionary.word.WordDialog
import jp.aqua.shizen.dictionary.word.WordDialogUiState
import jp.aqua.shizen.utils.scaffold.ShizenTopBar
import org.readium.r2.shared.ExperimentalReadiumApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalReadiumApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReaderScreen(
    viewModel: ReaderViewModel,
    uiState: ReaderUiState,
    wordDialogUiState: WordDialogUiState,
    updateContainer: (Int) -> Unit,
    onCloseReader: () -> Unit,
) {
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
    }
}