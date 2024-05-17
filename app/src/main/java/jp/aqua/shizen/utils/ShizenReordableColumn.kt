package jp.aqua.shizen.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import jp.aqua.shizen.R
import jp.aqua.shizen.item.model.LoadingStatus
import jp.aqua.shizen.item.model.TocEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableColumn

@Composable
fun ShizenReorderableColumn(
    items: List<TocEntry>,
    isItemsRemovable: Boolean,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onRemove: (TocEntry) -> Unit,
    onSwap: (Int, Int) -> Unit,
) {
    ReorderableColumn(
        modifier = Modifier.fillMaxWidth(),
        list = items,
        onSettle = { fromIndex, toIndex ->
            onSwap(fromIndex, toIndex)
        }
    ) { _, item, _ ->
        key(item.id) {
            var isRemoved by remember { mutableStateOf(false) }
            AnimatedVisibility(
                visible = !isRemoved,
                exit = shrinkVertically(
                    animationSpec = tween(durationMillis = 500),
                    shrinkTowards = Alignment.Top
                ) + fadeOut()
            ) {
                Card(
                    onClick = {},
                    shape = RectangleShape,
                    modifier = Modifier.padding(vertical = 5.dp)
                ) {
                    Row(
                        Modifier.padding(8.dp).animateContentSize(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            modifier = Modifier.weight(9f),
                            text = item.title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (isItemsRemovable && item.status == LoadingStatus.Success) {
                            IconButton(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    coroutineScope.launch {
                                        isRemoved = true
                                        delay(500)
                                        onRemove(item)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    tint = Color.Red,
                                    contentDescription = stringResource(R.string.delete_content)
                                )
                            }
                        }
                        IconButton(
                            modifier = Modifier
                                .weight(1f)
                                .draggableHandle(),
                            onClick = {},
                        ) {
                            Icon(
                                painterResource(R.drawable.baseline_reorder_24),
                                contentDescription = "Reorder"
                            )
                        }
                    }
                    if (item.status == LoadingStatus.Loading) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}