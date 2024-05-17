package jp.aqua.shizen.read.reader.presentation

import android.util.Log
import android.webkit.JavascriptInterface
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.viewModelScope
import jp.aqua.shizen.dictionary.word.WordDialogViewModel
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

    class JsInterface(val link: Link, val open: (String, String) -> Unit) {
        @JavascriptInterface
        fun onOpenWordDef(word: String, sentence: String) {
            val cleanedSentence = sentence.clean()
            val cleanedWord = word.clean()
            open(cleanedWord, cleanedSentence)
        }
    }

    private var bookInitData = readerRepository.bookInitData

    @OptIn(ExperimentalReadiumApi::class, ExperimentalDecorator::class)
    private val _uiState = MutableStateFlow(
        ReaderUiState(
            isShowBars = false,
            bookTitle = bookInitData.book.title,
            navigatorFactory = bookInitData.navigatorFactory
                .createFragmentFactory(
                    initialLocator = bookInitData.initialLocator,
                    initialPreferences = EpubPreferences(theme = Theme.DARK),
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
            viewModelScope.launch {
                Log.i("PAGE", uiState.fragment?.evaluateJavascript("document.body.innerHTML") ?: "")
            }
            val isShowBars = !uiState.isShowBars
            uiState.copy(isShowBars = isShowBars)
        }
    }

    fun updateContainer(id: Int) {
        if(id == _uiState.value.containerID) return
        _uiState.update { uiState ->
            uiState.copy(containerID = id)
        }
        generateFragment()
    }

    private fun generateFragment() {
        _uiState.update { uiState ->
            val fragment = (uiState.navigatorFactory.instantiate(
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
    val isShowBars: Boolean = false,
    val bookTitle: String,
    val navigatorFactory: FragmentFactory,
    val containerID: Int? = null,
    val fragment: EpubNavigatorFragment? = null
)