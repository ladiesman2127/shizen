package jp.aqua.shizen.listen.presentation

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import jp.aqua.shizen.item.model.Item
import jp.aqua.shizen.item.presentation.ItemScreen
import jp.aqua.shizen.listen.video.VideoActivity
import jp.aqua.shizen.utils.ScreenUiState
import jp.aqua.shizen.utils.ScreenViewModel

@Composable
fun ListenScreen(
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
        onItemClick = { videoID ->
            Intent(context, VideoActivity::class.java).apply {
                putExtra(Item.ID, videoID)
                context.startActivity(this)
            }
        },
        updateSelection = viewModel::updateSelection,
        turnOnSelection = viewModel::turnOnSelection,
        turnOffSelection = viewModel::turnOffSelection
    )
}