package jp.aqua.shizen.dictionary.worddialog

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import coil.compose.SubcomposeAsyncImage
import jp.aqua.shizen.R
import jp.aqua.shizen.utils.dialog.ShizenAlertDialog

const val TOGGLE_TRANSLATION_BUTTON_TAG = "TOGGLE TRANSLATION"
const val TRANSLATED_WORD = "TRANSLATED_WORD_TAG"
const val TRANSLATED_SENTENCE = "TRANSLATED_SENTENCE_TAG"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WordDialog(
    selectedWord: String,
    selectedSentence: String,
    selectedWordImages: List<String>,
    isTranslationVisible: Boolean,
    translatedWord: String,
    translatedSentence: String,
    onOpenWordDef: (String, String) -> Unit,
    onCloseWordDef: () -> Unit,
    onUpdateIsTranslationVisible: () -> Unit,
) {
    ShizenAlertDialog(
        confirmationLabel = stringResource(R.string.add),
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                val words = selectedSentence.split(' ')
                val annotatedString = buildAnnotatedString {
                    words.forEach { word ->
                        withStyle(MaterialTheme.typography.titleSmall.toSpanStyle()) {
                            append("$word ")
                        }
                        addStringAnnotation(
                            tag = "CLICKABLE",
                            annotation = word,
                            start = length - word.length,
                            end = length
                        )
                    }
                }
                Text(
                    text = selectedWord.ifBlank { "Select word" },
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleSmall
                )
                ClickableText(
                    text = annotatedString,
                    onClick = { offset ->
                        annotatedString.getStringAnnotations(
                            tag = "CLICKABLE",
                            start = offset,
                            end = offset
                        ).firstOrNull()?.let { annotation ->
                            onOpenWordDef(annotation.item, selectedSentence)
                        }
                    }
                )
            }
        },
        onConfirm = {
            // TODO
        },
        onDismiss = { onCloseWordDef() }
    ) {
        Box(
            Modifier
                .fillMaxWidth(),
        ) {
            HorizontalPager(
                state = PagerState { selectedWordImages.size }
            ) { imageIndex ->
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth(),
                    model = selectedWordImages[imageIndex],
                    contentDescription = stringResource(R.string.word_image),
                    contentScale = ContentScale.Crop,
                    loading = {
                        LinearProgressIndicator(
                            Modifier.fillMaxWidth()
                        )
                    }
                )
            }
        }
        Column(
            Modifier
                .fillMaxWidth()
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = if (isTranslationVisible) "Hide translation" else "Show translation",
            )
            IconButton(
                onClick = onUpdateIsTranslationVisible,
                modifier = Modifier.testTag(TOGGLE_TRANSLATION_BUTTON_TAG)
            ) {
                Icon(Icons.Outlined.KeyboardArrowDown, "Show translation")
            }
            if (isTranslationVisible) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(TRANSLATED_WORD),
                    value = translatedWord,
                    onValueChange = {},
                    enabled = false,
                    label = { Text(selectedWord) }
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(TRANSLATED_SENTENCE),
                    value = translatedSentence,
                    onValueChange = {},
                    enabled = false,
                    label = {
                        Text(
                            selectedSentence,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }
    }
}