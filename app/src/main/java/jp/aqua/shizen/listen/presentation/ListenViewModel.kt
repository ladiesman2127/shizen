package jp.aqua.shizen.listen.presentation

import jp.aqua.shizen.utils.ScreenViewModel
import jp.aqua.shizen.listen.video.data.VideoRepository


class ListenViewModel(
    private val videoRepository: VideoRepository
) : ScreenViewModel(videoRepository) {
    override suspend fun addFolder(){
        with(_uiState.value) {
            videoRepository.addVideo(
                folderTitle,
                folderCover,
            )
        }
    }

}
