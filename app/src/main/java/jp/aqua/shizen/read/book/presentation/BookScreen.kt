package jp.aqua.shizen.read.book.presentation

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import jp.aqua.shizen.item.presentation.ItemDescriptionScreen
import jp.aqua.shizen.item.presentation.ItemUiState
import jp.aqua.shizen.read.reader.ReaderActivity
import jp.aqua.shizen.read.reader.data.BookInitData
import jp.aqua.shizen.utils.dialog.ShizenFullScreenDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.readium.r2.shared.util.Try

@Composable
fun BookScreen(
    context: Context,
    viewModel: BookViewModel,
    uiState: ItemUiState,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onNavigateUp: () -> Unit
) {
    ItemDescriptionScreen(
        uiState = uiState,
        navigateUp = onNavigateUp,
        onOpen = { tocEntry ->
            coroutineScope.launch {
                viewModel.updateCurrentBook(uiState.id, tocEntry)
                    ?.onSuccess {
                        Log.i("Current book updated successfully", it.book.title)
                        Intent(context, ReaderActivity::class.java).apply {
                            context.startActivity(this)
                        }
                    }
                    ?.onFailure {
                        Log.e("Error in update current book", it.message)
                    }
            }
        },
        updateIsDescriptionExpanded = viewModel::updateIsDescriptionExpanded,
        fabText = "Read"
    )
}