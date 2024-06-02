package jp.aqua.shizen.item.presentation

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import jp.aqua.shizen.item.model.Item
import jp.aqua.shizen.item.presentation.components.ItemCard
import jp.aqua.shizen.utils.ScreenUiState
import kotlin.reflect.KFunction1

@Composable
fun ItemScreen(
    // Отвечает за изменение стиля composable - функции
    modifier: Modifier = Modifier,
    // Список itemise
    items: List<Item>,
    // Состояние экрана
    uiState: ScreenUiState,
    // Callback, срабатывающий при нажатии на Item
    onItemClick: (Int?) -> Unit,
    // Функция для обновления выбранных элементов
    updateSelection: KFunction1<Item, Unit>,
    // Функция для включения выборки
    turnOnSelection: KFunction1<Item, Unit>,
    // Функция для включения выборки
    turnOffSelection: () -> Unit,
) {
    // Context - класс, дающий доступ к глобальному состоянию приложения
    val context = LocalContext.current
    // Состояние листа
    val lazyListState = rememberLazyGridState()

    // Отключение выборки при возврате
    BackHandler(enabled = uiState.isInSelection) { turnOffSelection() }

    // Вертикальная сетка
    LazyVerticalGrid(
        modifier = modifier,
        // Количество колонок зависит от текущей ориентации экрана
        columns = GridCells.Fixed(
            if (context.resources.configuration.orientation == ORIENTATION_PORTRAIT)
                3
            else
                6
        ),
        // Передача состояния листа
        state = lazyListState,
        // Отступы
        contentPadding = PaddingValues(8.dp),
    ) {
         // Отображение предметов
        items(items) { item ->
            ItemCard(
                title = item.title,
                cover = item.cover,
                selected = uiState.selectedItems.contains(item),
                labeled = true,
                modifier = Modifier.pointerInput(item, uiState.isInSelection) {
                    detectTapGestures(
                        onTap = {
                            if (uiState.isInSelection)
                                updateSelection(item)
                            else {
                                onItemClick(item.id)
                            }
                        },
                        onLongPress = {
                            turnOnSelection(item)
                        }
                    )
                },
            )
        }
    }
}

