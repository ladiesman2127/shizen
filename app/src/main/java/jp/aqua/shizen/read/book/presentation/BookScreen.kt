package jp.aqua.shizen.read.book.presentation

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import jp.aqua.shizen.item.presentation.ItemDescriptionScreen
import jp.aqua.shizen.item.presentation.ItemUiState
import jp.aqua.shizen.read.reader.ReaderActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BookScreen(
    context: Context,
    viewModel: BookViewModel,
    uiState: ItemUiState,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onNavigateUp: () -> Unit
) {
    val knownWords by viewModel.getKnownWords().collectAsState(emptyList())
    ItemDescriptionScreen(
        uiState = uiState,
        navigateUp = onNavigateUp,
        onOpen = { tocEntry ->
            coroutineScope.launch {
                viewModel.updateCurrentBook(uiState.id, tocEntry, knownWords)
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