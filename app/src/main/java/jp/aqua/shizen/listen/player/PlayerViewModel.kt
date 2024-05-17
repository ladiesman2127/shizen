package jp.aqua.shizen.listen.player

import android.media.audiofx.Equalizer
import androidx.annotation.OptIn
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import jp.aqua.shizen.item.data.ItemRepository
import jp.aqua.shizen.item.model.Item
import jp.aqua.shizen.dictionary.word.WordDialogViewModel
import jp.aqua.shizen.item.model.TocEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
class PlayerViewModel(
    savedStateHandle: SavedStateHandle,
    val player: Player,
    private val _videoRepository: ItemRepository
) : WordDialogViewModel() {

    private val _videoID = checkNotNull(savedStateHandle.get<Int>(Item.ID))
    private val _episode = savedStateHandle.get<Int?>("EPISODE") ?: 0

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                _videoRepository.getItem(_videoID)
                    .filterNotNull()
                    .first()
                    .toPlayerUiState(_episode)
            }
            player.prepare()
            player.setMediaItems(
                _uiState.value.allEpisodes.map { MediaItem.fromUri(it.href) },
                _episode,
                0
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }

}

data class PlayerUiState(
    val allEpisodes: List<TocEntry> = emptyList(),
    val currentEpisodeTitle: String = "",
    val currentEpisodeUri: String = "",
)

fun Item.toPlayerUiState(episodeIndex: Int): PlayerUiState {
    return PlayerUiState(
        allEpisodes = tableOfContents,
        currentEpisodeTitle = tableOfContents[episodeIndex].title,
        currentEpisodeUri = tableOfContents[episodeIndex].href
    )
}