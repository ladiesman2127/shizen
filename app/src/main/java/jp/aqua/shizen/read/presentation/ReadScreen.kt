package jp.aqua.shizen.read.presentation

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import jp.aqua.shizen.dictionary.deck.DeckActivity
import jp.aqua.shizen.item.model.Item
import jp.aqua.shizen.item.presentation.ItemScreen
import jp.aqua.shizen.read.book.BookActivity
import jp.aqua.shizen.utils.ScreenUiState
import jp.aqua.shizen.utils.ScreenViewModel

@Composable
fun ReadScreen(
    modifier: Modifier = Modifier,
    uiState: ScreenUiState,
    viewModel: ScreenViewModel,
    items: List<Item>
) {
    val context = LocalContext.current
    ItemScreen(
        modifier = modifier.fillMaxSize(),
        uiState = uiState,
        items = items,
        onItemClick = { bookID ->
            Intent(context, BookActivity::class.java).apply {
                putExtra(Item.ID, bookID)
                context.startActivity(this)
            }
        },
        updateSelection = viewModel::updateSelection,
        turnOnSelection = viewModel::turnOnSelection,
        turnOffSelection = viewModel::turnOffSelection
    )
}

