package jp.aqua.shizen.dictionary.worddialog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.aqua.shizen.dictionary.network.image.ImageApi
import jp.aqua.shizen.dictionary.network.image.ImageRequest
import jp.aqua.shizen.dictionary.network.translation.TranslateApi
import jp.aqua.shizen.dictionary.network.translation.TranslateRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException


open class WordDialogViewModel : ViewModel() {

    private val _wordDialogUiState = MutableStateFlow(WordDialogUiState())
    val wordDialogUiState = _wordDialogUiState.asStateFlow()

    fun onOpenWordDef(word: String, sentence: String) {
        _wordDialogUiState.update { uiState ->
            uiState.copy(
                selectedWord = word.ifBlank { "Select a word" },
                selectedSentence = sentence,
                isWordSelected = true,
            )
        }
        if (word.isNotBlank()) {
            viewModelScope.launch {
                updateImage(word)
            }
            viewModelScope.launch {
                updateTranslation(word, sentence)
            }
        }
    }

    private suspend fun updateImage(word: String) =
        withContext(Dispatchers.IO) {
            try {
                _wordDialogUiState.update { uiState ->
                    val imageRequest = ImageRequest(q = word)
                    val imageResponse = ImageApi.retrofitService.image(
                        imageRequest.q,
                        imageRequest.num
                    )
                    uiState.copy(
                        selectedWordImages = imageResponse.items?.map { it.originalImageUrl }
                            ?: emptyList()
                    )
                }
            } catch (e: HttpException) {
                Log.e("updateImageApi", "Error")
                e.printStackTrace()
            }
        }

    private suspend fun updateTranslation(word: String, sentence: String) =
        withContext(Dispatchers.IO) {
            try {
                _wordDialogUiState.update { uiState ->
                    val translateRequest = TranslateRequest(
                        texts = listOf(word, sentence),
                        from = "",
                        to = "ru"
                    )
                    val translateResponse =
                        TranslateApi.retrofitService.translate(translateRequest)
                    uiState.copy(
                        translatedWord = translateResponse.translations?.get(0)?.text ?: "",
                        translatedSentence = translateResponse.translations?.get(1)?.text ?: "",
                    )
                }
            } catch (e: HttpException) {
                Log.e("updateImageApi", "Error")
                e.printStackTrace()
            }
        }


    fun onCloseWordDef() {
        _wordDialogUiState.update { uiState ->
            uiState.copy(
                isWordSelected = false,
                selectedWord = "",
                selectedSentence = "",
                translatedWord = "",
                translatedSentence = "",
                selectedWordImages = emptyList()
            )
        }
    }

    fun updateIsTranslationVisible() {
        _wordDialogUiState.update { uiState ->
            val isTranslationVisible = !uiState.isTranslationVisible
            uiState.copy(isTranslationVisible = isTranslationVisible)
        }
    }

}

data class WordDialogUiState(
    val selectedWord: String = "",
    val selectedSentence: String = "",
    val translatedWord: String = "",
    val translatedSentence: String = "",
    val selectedWordImages: List<String> = emptyList(),
    val isTranslationVisible: Boolean = false,
    val isWordSelected: Boolean = false,
)

