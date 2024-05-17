package jp.aqua.shizen.dictionary.presentation

import jp.aqua.shizen.dictionary.deck.data.DeckRepository
import jp.aqua.shizen.utils.ScreenViewModel

class DictionaryViewModel(
    private val deckRepository: DeckRepository
) : ScreenViewModel(deckRepository) {
    override suspend fun addFolder() {
        with(_uiState.value) {
            deckRepository.addDeck(
                folderTitle,
                folderCover,
            )
        }
    }
}