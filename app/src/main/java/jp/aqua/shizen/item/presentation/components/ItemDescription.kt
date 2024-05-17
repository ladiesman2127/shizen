package jp.aqua.shizen.item.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import jp.aqua.shizen.R
import jp.aqua.shizen.item.presentation.ItemUiState

@Composable
fun ItemDescription(
    modifier: Modifier = Modifier,
    uiState: ItemUiState,
    updateIsDescriptionExpanded: () -> Unit
) {
    Column(modifier = modifier) {
        uiState.description?.let { description ->
            Text(
                text = description,
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                maxLines = if (uiState.isDescriptionExpanded) Int.MAX_VALUE else 2,
                textAlign = TextAlign.Justify,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = updateIsDescriptionExpanded) {
                    Icon(
                        imageVector = if (uiState.isDescriptionExpanded) {
                            Icons.Filled.KeyboardArrowUp
                        } else {
                            Icons.Filled.KeyboardArrowDown
                        },
                        contentDescription = "Hide or show full description"
                    )
                }
            }
        }
        Text(
            text = stringResource(R.string.book_contents),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}


@Composable
fun ExpandableText(text: String, isExpanded: Boolean) {

}