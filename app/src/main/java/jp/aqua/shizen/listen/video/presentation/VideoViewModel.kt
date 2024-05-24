package jp.aqua.shizen.listen.video.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import jp.aqua.shizen.item.data.ItemRepository
import jp.aqua.shizen.item.model.TocEntry
import jp.aqua.shizen.item.presentation.ItemViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VideoViewModel(
    savedStateHandle: SavedStateHandle,
    videoRepository: ItemRepository
) : ItemViewModel(savedStateHandle, videoRepository) {
    override fun addContent(content: TocEntry) {
        viewModelScope.launch {
            super.addContent(content)
            delay(500)
            addContentAfterProcessing(content)
        }
    }
}