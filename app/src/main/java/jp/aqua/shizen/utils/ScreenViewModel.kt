package jp.aqua.shizen.utils

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.aqua.shizen.item.data.ItemRepository
import jp.aqua.shizen.item.model.Item
import jp.aqua.shizen.item.model.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


open class ScreenViewModel(private val itemRepository: ItemRepository) : ViewModel() {
    // Состояние U
    protected val _uiState = MutableStateFlow(ScreenUiState())
    val uiState = _uiState.asStateFlow()
        // Методы
    init {
        viewModelScope.launch {
            itemRepository.getAllItems().collect { items ->
                _uiState.update { uiState ->
                    uiState.copy(
                        allFilterMap = mapOf(
                            Item.STATUS to Status.entries.map { it.name },
                            Item.AUTHOR to items.flatMap { it.authors ?: emptyList() }.distinct(),
                            Item.LANGUAGE to items.map { it.language }.distinct(),
                        ),

                        selectedFilterMap = mapOf(
                            Item.STATUS to mutableListOf(),
                            Item.AUTHOR to mutableListOf(),
                            Item.LANGUAGE to mutableListOf(),
                        ),

                        allSortFields = listOf(
                            Item.TITLE,
                            Item.CREATION_DATE
                        ),

                        items = uiState.applyFilterAndSort(itemRepository)
                    )
                }
            }
        }
    }
    fun turnOnSelection(item: Item) {
        _uiState.update { uiState ->
            uiState.copy(
                isInSelection = true,
                selectedItems = listOf(item)
            )
        }
    }
    fun selectAll(allItems: List<Item>) {
        _uiState.update { uiState ->
            uiState.copy(selectedItems = allItems)
        }
    }
    fun clearSelection() {
        _uiState.update { uiState ->
            uiState.copy(
                selectedItems = emptyList()
            )
        }
    }
    suspend fun removeSelected() {
        if (_uiState.value.isInSelection) {
            for (item in _uiState.value.selectedItems) {
                itemRepository.deleteItem(item)
            }
            _uiState.update { uiState ->
                uiState.copy(
                    selectedItems = emptyList(),
                    isInSelection = false,
                )
            }
        }
    }
    fun updateSelection(item: Item) {
        _uiState.update { uiState ->
            val prevSelectedItems = uiState.selectedItems
            uiState.copy(
                selectedItems = if (item in prevSelectedItems)
                    prevSelectedItems.minusElement(item)
                else
                    prevSelectedItems.plusElement(item)
            )
        }
        if (_uiState.value.selectedItems.isEmpty())
            turnOffSelection()
    }
    fun turnOffSelection() {
        _uiState.update { uiState ->
            uiState.copy(isInSelection = false, selectedItems = emptyList())
        }
    }
    fun updateSort(sortField: String) {
        _uiState.update { uiState ->
            val sortMode = if (uiState.sortField == sortField) {
                if (uiState.sortMode == "ASC")
                    "DESC"
                else
                    "ASC"
            } else {
                uiState.sortMode
            }

            uiState.copy(
                sortMode = sortMode,
                sortField = sortField,
                items = uiState.applyFilterAndSort(
                    itemRepository = itemRepository,
                    sortField = sortField,
                    sortMode = sortMode
                )
            )
        }
    }
    fun updateFilter(field: String, value: String) {
        _uiState.value.selectedFilterMap[field]?.let { filterValues ->
            _uiState.update { uiState ->
                val newFilterMap = uiState.selectedFilterMap.toMutableMap()
                newFilterMap[field] = when (value) {
                    in filterValues -> filterValues.minusElement(value)
                    else -> filterValues.plusElement(value)
                }
                uiState.copy(
                    selectedFilterMap = newFilterMap,
                    items = uiState.applyFilterAndSort(
                        itemRepository = itemRepository,
                        filterMap = newFilterMap
                    )
                )
            }
        }
    }
    fun turnOnFolderAdding() {
        _uiState.update { uiState ->
            uiState.copy(isInAddingFolder = true)
        }
    }
    fun turnOffFolderAdding() {
        _uiState.update { uiState ->
            uiState.copy(
                isInAddingFolder = false,
                folderTitle = "",
                folderCover = null,
                isCoverError = false
            )
        }
    }
    fun updateFolderTitle(title: String) {
        _uiState.update { uiState ->
            uiState.copy(folderTitle = title)
        }
    }
    fun updateFolderCover(cover: String?) {
        _uiState.update { uiState ->
            uiState.copy(folderCover = cover)
        }
    }
    fun updateIsCoverError(isCoverError: Boolean?) {
        _uiState.update { uiState ->
            uiState.copy(isCoverError = isCoverError)
        }
    }
    open suspend fun addBook(uri: Uri, context: Context): Result<String> {
        return Result.failure(Exception())
    }
    fun updateSearchQuery(query: String) {
        _uiState.update { uiState ->
            uiState.copy(
                searchQuery = query,
                items = uiState.applyFilterAndSort(
                    itemRepository = itemRepository,
                    searchQuery = query
                )
            )
        }
    }
    fun updateIsInSearch(isInSearch: Boolean) {
        _uiState.update { uiState ->
            uiState.copy(
                isInSearch = isInSearch,
                items = uiState.applyFilterAndSort(itemRepository)
            )
        }
    }
    open suspend fun addFolder() {}
    fun clearSearchQuery() {
        _uiState.update { uiState ->
            if (uiState.searchQuery.isEmpty()) {
                uiState.copy(
                    searchQuery = "",
                    items = uiState.applyFilterAndSort(
                        itemRepository = itemRepository,
                        searchQuery = ""
                    ),
                    isInSearch = false
                )
            } else {
                uiState.copy(
                    searchQuery = "",
                    items = uiState.applyFilterAndSort(
                        itemRepository = itemRepository,
                        searchQuery = ""
                    )
                )
            }
        }
    }
}
data class ScreenUiState(
    // Список элементов текущего экрана
    val items: Flow<List<Item>> = flowOf(),
    // Режим поиска вкл/выкл
    val isInSearch: Boolean = false,
    // Поисковой запрос
    val searchQuery: String = "",
    // Режим выборки
    val isInSelection: Boolean = false,
    // Выбранные элементы
    val selectedItems: List<Item> = listOf(),
    // Все параметры фильтрации
    val allFilterMap: Map<String, List<String?>> = mapOf(),
    // Выбранные параметры фильтрации
    val selectedFilterMap: Map<String, List<String>> = mapOf(),
    // Все поля сортировки
    val allSortFields: List<String> = emptyList(),
    // Выбранные поля сортировки
    val sortField: String = Item.CREATION_DATE,
    // Режим сортировки
    val sortMode: String = "ASC",
    // Режим добавления папки / доски
    val isInAddingFolder: Boolean? = null,
    // Название папки / доски
    val folderTitle: String = "",
    // Обложка папки / доски
    val folderCover: String? = null,
    // Статус обложки
    val isCoverError: Boolean? = null,
) {
    fun applyFilterAndSort(
        itemRepository: ItemRepository,
        filterMap: Map<String, List<String>?>? = null,
        sortField: String? = null,
        sortMode: String? = null,
        searchQuery: String? = null
    ): Flow<List<Item>> {
        val mFilterMap = filterMap ?: this.selectedFilterMap
        val mSortField = sortField ?: this.sortField
        val mSortMode = sortMode ?: this.sortMode
        val mSearchQuery = searchQuery ?: this.searchQuery
        return itemRepository.getAllItems().map { items ->
            val sortedBooks = when (mSortMode) {
                "ASC" -> items.sortedBy { item ->
                    when (mSortField) {
                        Item.TITLE -> item.title
                        Item.CREATION_DATE -> item.creation.toString()
                        else -> item.title
                    }
                }

                else -> items.sortedByDescending { item ->
                    when (mSortField) {
                        Item.TITLE -> item.title
                        Item.CREATION_DATE -> item.creation.toString()
                        else -> item.title
                    }
                }
            }

            val res = sortedBooks.filter { item ->
                val authorFilter = (item.authors?.let {
                    mFilterMap[Item.AUTHOR]?.any(it::contains)
                } ?: true || mFilterMap[Item.AUTHOR]?.isEmpty() ?: true)
                val statusFilter =
                    (mFilterMap[Item.STATUS]?.contains(item.status.name) ?: true ||
                            mFilterMap[Item.STATUS]?.isEmpty() ?: true)
                val languageFilter =
                    (mFilterMap[Item.LANGUAGE]?.contains(item.language) ?: true ||
                            mFilterMap[Item.LANGUAGE]?.isEmpty() ?: true)
                val searchFilter = mSearchQuery.isEmpty() || item.doesMatchSearchQuery(mSearchQuery)
                authorFilter && statusFilter && languageFilter && searchFilter
            }
            res
        }
    }
}
