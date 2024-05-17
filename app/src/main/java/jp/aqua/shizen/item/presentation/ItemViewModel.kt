package jp.aqua.shizen.item.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.aqua.shizen.item.data.ItemRepository
import jp.aqua.shizen.item.model.Item
import jp.aqua.shizen.item.model.LoadingStatus
import jp.aqua.shizen.item.model.Status
import jp.aqua.shizen.item.model.TocEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class ItemViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemRepository: ItemRepository
) : ViewModel() {

    protected var _uiState = MutableStateFlow(ItemUiState())
    val uiState = _uiState.asStateFlow()

    private var _itemID: Int = checkNotNull(savedStateHandle[Item.ID])

    init {
        viewModelScope.launch {
            _uiState.update {
                itemRepository.getItem(_itemID)
                    .filterNotNull()
                    .first()
                    .toItemUiState()
            }
        }
    }

    suspend fun updateItem() = itemRepository.updateItem(_uiState.value.toItem())

    fun updateTitle(title: String) {
        _uiState.update { uiState ->
            uiState.copy(title = title)
        }
    }

    fun updateCover(cover: String?) {
        _uiState.update { uiState ->
            uiState.copy(cover = cover)
        }
    }

    fun updateLanguage(language: String) {
        _uiState.update { uiState ->
            uiState.copy(language = language)
        }
    }

    open fun addContent(content: TocEntry) {
        _uiState.update { uiState ->
            val newTableOfContents = uiState.tableOfContents
                .toMutableList()
                .apply { add(content) }
            uiState.copy(tableOfContents = newTableOfContents)
        }
    }

    fun removeContent(content: TocEntry) {
        _uiState.update { uiState ->
            val newTableOfContents = uiState.tableOfContents
                .toMutableList()
                .apply { remove(content) }
            uiState.copy(tableOfContents = newTableOfContents)
        }
    }

    fun removeContentWithUpdating(content: TocEntry) {
        _uiState.update { uiState ->
            val newTableOfContents = uiState.tableOfContents.minusElement(content)
            uiState.copy(tableOfContents = newTableOfContents)
        }
        viewModelScope.launch {
            updateItem()
        }
    }

    fun updateIsEditing() {
        _uiState.update { uiState ->
            val isEditing = !uiState.isEditing
            uiState.copy(isEditing = isEditing)
        }
    }

    open fun updateIsAdding() {
        _uiState.update { uiState ->
            val isAdding = !uiState.isAdding
            uiState.copy(isAdding = isAdding)
        }
    }

    fun updateIsStatusExpanded() {
        _uiState.update { uiState ->
            val isStatusExpanded = !uiState.isStatusExpanded
            uiState.copy(isStatusExpanded = isStatusExpanded)
        }
    }

    fun updateStatus(status: Status) {
        _uiState.update { uiState ->
            uiState.copy(status = status)
        }
    }

    fun updateIsDescriptionExpanded() {
        _uiState.update { uiState ->
            val isDescriptionExpanded = !uiState.isDescriptionExpanded
            uiState.copy(isDescriptionExpanded = isDescriptionExpanded)
        }
    }

    fun onSwap(from: Int, to: Int) {
        _uiState.update { uiState ->
            val newTableOfContents = uiState.tableOfContents.toMutableList().apply {
                add(to, removeAt(from))
            }
            uiState.copy(tableOfContents = newTableOfContents)
        }
    }

    fun addContentAfterProcessing(tocEntry: TocEntry) {
        _uiState.update { uiState ->
            val newTableOfContents = uiState.tableOfContents.toMutableList().apply {
                val index = indexOf(tocEntry)
                add(index, removeAt(index).copy(status = LoadingStatus.Success))
            }
            uiState.copy(tableOfContents = newTableOfContents)
        }
    }

}

data class ItemUiState(
    val id: Int? = null,
    val isEditing: Boolean = false,
    val isAdding: Boolean = false,
    val title: String = "",
    val cover: String? = null,
    val href: String = "",
    val language: String = "",
    val authors: List<String>? = null,
    val tableOfContents: List<TocEntry> = emptyList(),
    val creation: Long = 0L,
    val progression: String = "{}",
    val isStatusExpanded: Boolean = false,
    val allStatuses: List<Status> = Status.entries,
    val status: Status = Status.NotStarted,
    val isDescriptionExpanded: Boolean = false,
    val description: String? = null,
)

fun Item.toItemUiState(): ItemUiState {
    return ItemUiState(
        id = id,
        title = title,
        cover = cover,
        href = href,
        language = language,
        authors = authors,
        tableOfContents = tableOfContents,
        description = description,
        status = status,
        progression = progression,
        creation = creation
    )
}

fun ItemUiState.toItem(): Item {
    return Item(
        id = id,
        title = title,
        cover = cover,
        href = href,
        language = language,
        authors = authors,
        progression = progression,
        tableOfContents = tableOfContents,
        description = description,
        status = status,
        creation = creation
    )
}