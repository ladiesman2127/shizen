package jp.aqua.shizen.read.reader.presentation

import android.util.Log
import android.webkit.JavascriptInterface
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.viewModelScope
import jp.aqua.shizen.dictionary.worddialog.WordDialogViewModel
import jp.aqua.shizen.read.reader.data.ReaderRepository
import jp.aqua.shizen.utils.extensions.clean
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.readium.r2.navigator.ExperimentalDecorator
import org.readium.r2.navigator.epub.EpubNavigatorFragment
import org.readium.r2.navigator.epub.EpubPreferences
import org.readium.r2.navigator.input.InputListener
import org.readium.r2.navigator.input.TapEvent
import org.readium.r2.navigator.preferences.Theme
import org.readium.r2.shared.ExperimentalReadiumApi
import org.readium.r2.shared.publication.Link
import org.readium.r2.shared.publication.Locator

@ExperimentalReadiumApi
class ReaderViewModel(
    private val readerRepository: ReaderRepository,
) : WordDialogViewModel() {

    private var knownWords = readerRepository.knownWords
    private var bookInitData = readerRepository.bookInitData

    class JsInterface(
        val link: Link,
        val knownWords: Map<String, Int>,
        val open: (String, String) -> Unit
    ) {
        @JavascriptInterface
        fun onOpenWordDef(word: String, sentence: String) {
            val cleanedSentence = sentence.clean()
            val cleanedWord = word.clean()
            open(cleanedWord, cleanedSentence)
        }

        @JavascriptInterface
        fun isKnownWord(word: String): Boolean = knownWords.containsKey(word)

        @JavascriptInterface
        fun getWordLevel(word: String): Int = knownWords[word] ?: 0
    }

    @OptIn(ExperimentalReadiumApi::class, ExperimentalDecorator::class)
    private val _uiState = MutableStateFlow(
        ReaderUiState(
            isShowBars = false,
            bookTitle = bookInitData.book.title,
            fragmentFactory = bookInitData.navigatorFactory
                .createFragmentFactory(
                    initialLocator = bookInitData.initialLocator,
                    // initialPreferences = EpubPreferences(theme = Theme.DARK),
                    paginationListener = object : EpubNavigatorFragment.PaginationListener {
                        override fun onPageChanged(
                            pageIndex: Int,
                            totalPages: Int,
                            locator: Locator
                        ) {
                            super.onPageChanged(pageIndex, totalPages, locator)
                            bookInitData = bookInitData.copy(initialLocator = locator)
                        }
                    },
                    configuration = EpubNavigatorFragment.Configuration()
                        .apply {
                            registerJavascriptInterface("android") { link ->
                                JsInterface(
                                    link = link,
                                    knownWords = knownWords,
                                    open = { word, sentence ->
                                        onOpenWordDef(word, sentence)
                                    }
                                )
                            }
                        }
                )
        )
    )

    val uiState = _uiState.asStateFlow()

    init {
        generateFragment()
    }

    suspend fun updateProgression() {
        bookInitData.initialLocator?.let { locator ->
            readerRepository.updateProgression(locator)
        }
    }


    fun updateIsShowBars() {
        _uiState.update { uiState ->
            val isShowBars = !uiState.isShowBars
            uiState.copy(isShowBars = isShowBars)
        }
    }

    fun updateContainer(id: Int) {
        if (id == _uiState.value.containerID) return
        _uiState.update { uiState ->
            uiState.copy(containerID = id)
        }
        generateFragment()
    }

    private fun generateFragment() {
        _uiState.update { uiState ->
            val fragment = (uiState.fragmentFactory.instantiate(
                ClassLoader.getSystemClassLoader(),
                EpubNavigatorFragment::class.java.name
            ) as EpubNavigatorFragment)
                .apply {
                    addInputListener(
                        object : InputListener {
                            override fun onTap(event: TapEvent): Boolean {
                                updateIsShowBars()
                                return super.onTap(event)
                            }
                        }
                    )
                }
            uiState.copy(
                fragment = fragment
            )
        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            // readerRepository.updateKnownWords(knownWords)
            super.onCleared()
        }
    }
}

data class ReaderUiState(
    val isShowBars: Boolean = false,
    val bookTitle: String,
    val fragmentFactory: FragmentFactory,
    val containerID: Int? = null,
    val fragment: EpubNavigatorFragment? = null
)