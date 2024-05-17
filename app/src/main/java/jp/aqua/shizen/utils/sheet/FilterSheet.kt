package jp.aqua.shizen.utils.sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.ArrowDown
import compose.icons.evaicons.outline.ArrowUp
import jp.aqua.shizen.R
import jp.aqua.shizen.utils.ScreenUiState
import jp.aqua.shizen.item.model.Item
import jp.aqua.shizen.utils.list.ShizenListItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterSheet(
    onDismiss: () -> Unit,
    uiState: ScreenUiState?,
    onUpdateFilter: (String, String) -> Unit,
    onUpdateSort: (String) -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 26.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            if (uiState?.allFilterMap?.isNotEmpty() == true)
                Text("Filter", style = MaterialTheme.typography.headlineSmall)
            uiState?.allFilterMap?.entries?.forEach { filter ->
                Text(
                    when (filter.key) {
                        Item.STATUS -> stringResource(R.string.status)
                        Item.AUTHOR -> stringResource(R.string.author)
                        Item.LANGUAGE -> stringResource(R.string.language)
                        else -> ""
                    }
                )
                FlowRow(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    filter.value.forEach { field ->
                        field?.let {
                            FilterChip(
                                selected = uiState.selectedFilterMap[filter.key]!!.contains(field),
                                onClick = { onUpdateFilter(filter.key, field) },
                                label = {
                                    Text(
                                        field
                                            .trim(' ')
                                            .trim(',')
                                            .replace(Regex("(?<=[a-z])(?=[A-Z])"), " ")
                                    )
                                }
                            )
                        }
                    }
                }
            }
            Text("Sort", style = MaterialTheme.typography.headlineSmall)
            uiState?.allSortFields?.forEach { sortField ->
                ShizenListItem(
                    modifier = Modifier.clickable { onUpdateSort(sortField) },
                    headlineContent = {
                        Text(
                            text = when (sortField) {
                                Item.TITLE -> stringResource(R.string.title)
                                Item.CREATION_DATE -> stringResource(R.string.date)
                                else -> ""
                            }
                        )
                    },
                    trailingContent = {
                        if (uiState.sortField == sortField) {
                            if (uiState.sortMode == "ASC")
                                Icon(
                                    imageVector = EvaIcons.Outline.ArrowUp,
                                    contentDescription = "Sorted by ASC"
                                )
                            else
                                Icon(
                                    imageVector = EvaIcons.Outline.ArrowDown,
                                    contentDescription = "Sorted by DESC"
                                )
                        }
                    }
                )
            }
        }
    }
}