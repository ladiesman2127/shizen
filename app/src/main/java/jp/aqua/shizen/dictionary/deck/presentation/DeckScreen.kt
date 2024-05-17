package jp.aqua.shizen.dictionary.deck.presentation

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import jp.aqua.shizen.dictionary.deck.presentation.components.AddWordCardDialog
import jp.aqua.shizen.dictionary.wordcard.presentation.WordCard
import jp.aqua.shizen.item.model.TocEntry
import jp.aqua.shizen.item.presentation.ItemDescriptionScreen
import jp.aqua.shizen.item.presentation.ItemUiState

@Composable
fun DeckScreen(
    context: Context,
    viewModel: DeckViewModel,
    uiState: ItemUiState,
    onNavigateUp: () -> Unit
) {
    var showCard by rememberSaveable {
        mutableStateOf(false)
    }

    var cardData by remember { mutableStateOf<TocEntry?>(null) }

    if (uiState.isAdding) {
        val cardUiState by viewModel.wordCardUiState.collectAsState()
        AddWordCardDialog(
            cardUiState = cardUiState,
            updateFront = viewModel::updateFront,
            updateBack = viewModel::updateBack,
            addFrontContent = viewModel::addFrontContent,
            addBackContent = viewModel::addBackContent,
            removeFrontContent = viewModel::removeFrontContent,
            removeBackContent = viewModel::removeBackContent,
            updateFrontContent = viewModel::updateFrontContentStatus,
            updateBackContent = viewModel::updateBackContentStatus,
            swapFrontContent = viewModel::swapFrontContent,
            swapBackContent = viewModel::swapBackContent,
            onConfirm = viewModel::saveWordCard,
            onDismiss = viewModel::resetWordCardUiState,
            storeContent = viewModel::storeContent
        )
    }

    ItemDescriptionScreen(
        uiState = uiState,
        fabText = "Remember",
        isAddFab = true,
        isEditable = true,
        navigateUp = onNavigateUp,
        updateIsDescriptionExpanded = viewModel::updateIsDescriptionExpanded,
        onEdit = viewModel::updateIsEditing,
        onAdd = viewModel::updateIsAdding,
        onOpen = { it: TocEntry? ->
            it?.let {
                cardData = it
                showCard = true
            }
        },
        onDeleteContentWithUpdating = viewModel::removeContentWithUpdating,
        isContentRemovable = true
    )

    if (showCard) {
        cardData?.let { data ->
            WordCard(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(3f)
                    .padding(0.dp),
                url = data.href
            )
        }
    }
}