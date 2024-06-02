package jp.aqua.shizen.read.reader.presentation

import android.util.Log
import android.webkit.JavascriptInterface
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.viewModelScope
import jp.aqua.shizen.dictionary.worddialog.WordDialogViewModel
import jp.aqua.shizen.item.model.LoadingStatus
import jp.aqua.shizen.read.reader.data.BookInitData
import jp.aqua.shizen.read.reader.data.ReaderRepository
import jp.aqua.shizen.utils.extensions.clean
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.readium.r2.navigator.epub.EpubNavigatorFragment
import org.readium.r2.navigator.input.InputListener
import org.readium.r2.navigator.input.TapEvent
import org.readium.r2.shared.ExperimentalReadiumApi
import org.readium.r2.shared.publication.Link
import org.readium.r2.shared.publication.Locator

@ExperimentalReadiumApi
class ReaderViewModel(
    private val readerRepository: ReaderRepository,
) : WordDialogViewModel() {

    private fun setStatus(status: LoadingStatus) {
        _uiState.update { uiState ->
            uiState.copy(status = status)
        }
    }

    private fun setProgress(progress: Float) {
        _uiState.update { uiState ->
            uiState.copy(progress = progress)
        }
    }

    private var knownWords: MutableMap<String, Int> = readerRepository
        .knownWords
        .words
        .toMutableMap()
    private var bookInitData: BookInitData = readerRepository.bookInitData
    private val _ready = MutableStateFlow(false)
    val ready = _ready.asStateFlow()

    class JsInterface(
        // Данные текущего HTML - файла
        val link: Link,
        // Известные слова
        var knownWords: MutableMap<String, Int>,
        // Событие нажатия на слово
        val open: (String, String) -> Unit,
        val loading: () -> Unit,
        val success: () -> Unit,
        val progress: (Float) -> Unit
    ) {

        @JavascriptInterface
        fun setStatusLoading() {
            loading()
        }

        @JavascriptInterface
        fun setStatusSuccess() {
            success()
        }

        @JavascriptInterface
        fun setLoadingProgress(progress: String) {
            Log.i("Progress Update Interface", progress.toString())
            progress(progress.toFloat())
        }

        @JavascriptInterface
        fun onOpenWordDef(word: String, sentence: String) {
            val cleanedSentence = sentence.clean()
            val cleanedWord = word.clean()
            open(cleanedWord, cleanedSentence)
        }

        @JavascriptInterface
        fun isKnownWord(word: String): Boolean {
            return knownWords.containsKey(word)
        }

        @JavascriptInterface
        fun setNewWord(word: String) {
            knownWords.put(word, 0)
        }

        @JavascriptInterface
        fun getWordLevel(word: String): Int = knownWords[word] ?: 0
    }

    var page = MutableStateFlow(-1)

    @OptIn(ExperimentalReadiumApi::class)
    private val _uiState = MutableStateFlow(
        ReaderUiState(
            isShowBars = false,
            bookTitle = bookInitData.book.title,
            fragmentFactory = bookInitData.navigatorFactory
                .createFragmentFactory(
                    initialLocator = bookInitData.initialLocator,
                    paginationListener = object : EpubNavigatorFragment.PaginationListener {
                        override fun onPageChanged(
                            pageIndex: Int,
                            totalPages: Int,
                            locator: Locator
                        ) {
                            super.onPageChanged(pageIndex, totalPages, locator)
                            bookInitData = bookInitData.copy(initialLocator = locator)
                        }

                        override fun onPageLoaded() {
                            super.onPageLoaded()
                            page.update { page.value + 1 }
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
                                    },
                                    loading = {
                                        setStatus(LoadingStatus.Loading)
                                    },
                                    success = {
                                        setStatus(LoadingStatus.Success)
                                    },
                                    progress = {
                                        setProgress(it)
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

    suspend fun updateWords() {
        readerRepository.updateKnownWords(knownWords)
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
}

data class ReaderUiState(
    val status: LoadingStatus = LoadingStatus.Loading,
    val progress: Float = 0.0f,
    val isShowBars: Boolean = false,
    val bookTitle: String,
    val fragmentFactory: FragmentFactory,
    val containerID: Int? = null,
    val fragment: EpubNavigatorFragment? = null
)