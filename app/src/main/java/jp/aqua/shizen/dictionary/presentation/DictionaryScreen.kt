package jp.aqua.shizen.dictionary.presentation

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import jp.aqua.shizen.dictionary.deck.DeckActivity
import jp.aqua.shizen.item.model.Item
import jp.aqua.shizen.item.presentation.ItemScreen
import jp.aqua.shizen.utils.ScreenUiState
import jp.aqua.shizen.utils.ScreenViewModel

@Composable
fun DictionaryScreen(
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
        onItemClick = { deckID ->
            Intent(context, DeckActivity::class.java).apply {
                putExtra(Item.ID, deckID)
                context.startActivity(this)
            }
        },
        updateSelection = viewModel::updateSelection,
        turnOnSelection = viewModel::turnOnSelection,
        turnOffSelection = viewModel::turnOffFolderAdding
    )
}