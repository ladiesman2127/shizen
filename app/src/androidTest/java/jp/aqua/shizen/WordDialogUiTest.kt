package jp.aqua.shizen

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import jp.aqua.shizen.dictionary.worddialog.TOGGLE_TRANSLATION_BUTTON_TAG
import jp.aqua.shizen.dictionary.worddialog.TRANSLATED_SENTENCE
import jp.aqua.shizen.dictionary.worddialog.TRANSLATED_WORD
import jp.aqua.shizen.dictionary.worddialog.WordDialog
import jp.aqua.shizen.dictionary.worddialog.WordDialogViewModel
import org.junit.Rule
import org.junit.Test

class WordDialogUiTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun word_dialog_loads() {
        val selectedWord = "Lorem"
        val selectedSentence = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
        val viewModel = WordDialogViewModel()
        viewModel.onOpenWordDef(selectedWord, selectedSentence)
        composeTestRule.setContent {
            val uiState = viewModel.wordDialogUiState.collectAsState()
            WordDialog(
                selectedWord = uiState.value.selectedWord,
                selectedSentence = uiState.value.selectedSentence,
                selectedWordImages = uiState.value.selectedWordImages,
                isTranslationVisible = uiState.value.isTranslationVisible,
                translatedWord = uiState.value.translatedWord,
                translatedSentence = uiState.value.translatedSentence,
                onOpenWordDef = viewModel::onOpenWordDef,
                onCloseWordDef = viewModel::onCloseWordDef,
                onUpdateIsTranslationVisible = viewModel::updateIsTranslationVisible
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag(TOGGLE_TRANSLATION_BUTTON_TAG).performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag(TRANSLATED_WORD).assertExists()
        composeTestRule.onNodeWithTag(TRANSLATED_SENTENCE).assertExists()
    }
}
