package jp.aqua.shizen.dictionary.deck.presentation

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import jp.aqua.shizen.dictionary.deck.data.DeckRepository
import jp.aqua.shizen.utils.KsoupUtils
import jp.aqua.shizen.item.model.LoadingStatus
import jp.aqua.shizen.item.model.TocEntry
import jp.aqua.shizen.item.presentation.ItemViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.io.File
import java.util.UUID

class DeckViewModel(
    savedStateHandle: SavedStateHandle,
    private val deckRepository: DeckRepository
) : ItemViewModel(savedStateHandle, deckRepository) {

    private val _wordCardUiState = MutableStateFlow(WordCardUiState())
    val wordCardUiState = _wordCardUiState.asStateFlow()

    private var deckDir: File? = null

    var cardDir: File? = null
        private set

    fun updateFront(front: String) {
        _wordCardUiState.update { wordCardUiState ->
            wordCardUiState.copy(front = front)
        }
    }

    fun updateBack(back: String) {
        _wordCardUiState.update { wordCardUiState ->
            wordCardUiState.copy(back = back)
        }
    }

    fun resetWordCardUiState() {
        _wordCardUiState.update {
            WordCardUiState()
        }
        updateIsAdding()
    }

    suspend fun storeContent(
        uri: Uri,
        context: Context,
    ) {
        cardDir?.let {
            deckRepository.storeContent(uri, context, it)
        }
    }

    fun saveWordCard() {
        viewModelScope.launch {
            deckDir = deckDir ?: File(uiState.value.href?.ifEmpty { return@launch })
            with(_wordCardUiState.value) {
                cardDir = File(deckDir, "/${UUID.randomUUID()}").apply { mkdir() }
                val cardIndexFile = deckRepository.storeIndexFile(cardDir!!, front, back)
                val newCardData = TocEntry(
                    title = KsoupUtils.textContent(front),
                    href = cardIndexFile.path,
                    date = DateTime().toDate().time
                )
                super.addContent(newCardData)
                updateItem()
            }
            updateIsAdding()
        }
    }

    fun addFrontContent(content: TocEntry) {
        _wordCardUiState.update { wordCardUiState ->
            val newFrontTableOfContents = wordCardUiState.frontTableOfContents.plus(content)
            val newFront = wordCardUiState.front + content.href
            wordCardUiState.copy(
                frontTableOfContents = newFrontTableOfContents,
                front = newFront
            )
        }
    }

    fun addBackContent(content: TocEntry) {
        _wordCardUiState.update { wordCardUiState ->
            val newBackTableOfContents = wordCardUiState.backTableOfContents.plus(content)
            val newBack = wordCardUiState.back + content.href
            wordCardUiState.copy(
                backTableOfContents = newBackTableOfContents,
                back = newBack
            )
        }
    }

    fun updateFrontContentStatus(
        content: TocEntry,
        status: LoadingStatus = LoadingStatus.Success
    ) {
        _wordCardUiState.update { wordCardUiState ->
            val newFrontTableOfContents =
                wordCardUiState.frontTableOfContents.toMutableList().apply {
                    val index = indexOf(content)
                    add(index, removeAt(index).copy(status = status))
                }
            wordCardUiState.copy(
                frontTableOfContents = newFrontTableOfContents
            )
        }
    }

    fun updateBackContentStatus(
        content: TocEntry,
        status: LoadingStatus = LoadingStatus.Success
    ) {
        _wordCardUiState.update { wordCardUiState ->
            val newBackTableOfContents =
                wordCardUiState.backTableOfContents.toMutableList().apply {
                    val index = indexOf(content)
                    add(index, removeAt(index).copy(status = status))
                }
            wordCardUiState.copy(
                backTableOfContents = newBackTableOfContents,
            )
        }
    }

    fun removeFrontContent(content: TocEntry) {
        _wordCardUiState.update { wordCardUiState ->
            val newFrontTableOfContents = wordCardUiState.frontTableOfContents.minus(content)
            val newFront = wordCardUiState.front.replaceFirst(content.href, "")
            wordCardUiState.copy(
                frontTableOfContents = newFrontTableOfContents,
                front = newFront
            )
        }
    }

    fun removeBackContent(content: TocEntry) {
        _wordCardUiState.update { wordCardUiState ->
            val newBackTableOfContents = wordCardUiState.backTableOfContents.minus(content)
            val newBack = wordCardUiState.back.replace(content.href, "")
            wordCardUiState.copy(
                backTableOfContents = newBackTableOfContents,
                back = newBack
            )
        }
    }

    fun swapFrontContent(from: Int, to: Int) {
        _wordCardUiState.update { wordCardUiState ->
            val contentFrom = wordCardUiState.frontTableOfContents[from].href
            val contentTo = wordCardUiState.frontTableOfContents[to].href
            val newFrontTableOfContents = wordCardUiState.frontTableOfContents.toMutableList()
                .apply { add(to, removeAt(from)) }
            val fromStartIndex = wordCardUiState.front.indexOf(contentFrom)
            val fromEndIndex = fromStartIndex + contentFrom.length
            val toStartIndex = wordCardUiState.front.indexOf(contentTo)
            val removedContentFront =
                wordCardUiState.front.removeRange(fromStartIndex, fromEndIndex)
            val newFront =
                StringBuilder().append(removedContentFront).insert(toStartIndex, contentFrom)
                    .toString()
            wordCardUiState.copy(
                frontTableOfContents = newFrontTableOfContents,
                front = newFront
            )
        }
    }

    fun swapBackContent(from: Int, to: Int) {
        _wordCardUiState.update { wordCardUiState ->
            val contentFrom = wordCardUiState.backTableOfContents[from].href
            val contentTo = wordCardUiState.backTableOfContents[to].href
            val newBackTableOfContents = wordCardUiState.backTableOfContents.toMutableList()
                .apply { add(to, removeAt(from)) }
            val fromStartIndex = wordCardUiState.back.indexOf(contentFrom)
            val fromEndIndex = fromStartIndex + contentFrom.length
            val toStartIndex = wordCardUiState.back.indexOf(contentTo)
            val removedContentBack = wordCardUiState.back.removeRange(fromStartIndex, fromEndIndex)
            val newBack =
                StringBuilder().append(removedContentBack).insert(toStartIndex, contentFrom)
                    .toString()
            wordCardUiState.copy(
                backTableOfContents = newBackTableOfContents,
                back = newBack
            )
        }
    }

}

data class WordCardUiState(
    val front: String = "",
    val frontTableOfContents: List<TocEntry> = emptyList(),
    val back: String = "",
    val backTableOfContents: List<TocEntry> = emptyList()
)