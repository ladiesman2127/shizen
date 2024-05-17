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
    modifier: Modifier = Modifier,
    items: List<Item>,
    uiState: ScreenUiState,
    onItemClick: (Int?) -> Unit,
    updateSelection: KFunction1<Item, Unit>,
    turnOnSelection: KFunction1<Item, Unit>,
    turnOffSelection: () -> Unit,
) {
    val context = LocalContext.current
    val lazyListState = rememberLazyGridState()

    BackHandler(enabled = uiState.isInSelection) { turnOffSelection() }

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(
            if (context.resources.configuration.orientation == ORIENTATION_PORTRAIT)
                3
            else
                6
        ),
        state = lazyListState,
        contentPadding = PaddingValues(8.dp),
    ) {
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
