package jp.aqua.shizen.utils.scaffold

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Search
import jp.aqua.shizen.R
import jp.aqua.shizen.utils.sheet.FilterSheet
import jp.aqua.shizen.navigation.ShizenScreen
import jp.aqua.shizen.utils.ScreenUiState
import jp.aqua.shizen.utils.dialog.ShizenAlertDialog
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KSuspendFunction0

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ScreenTopBar(
    currentScreen: ShizenScreen,
    uiState: ScreenUiState,
    onClearSelection: () -> Unit,
    onRemoveSelectedItems: KSuspendFunction0<Unit>,
    onSelectAll: () -> Unit,
    onClearSearchQuery: () -> Unit,
    onUpdateFilter: (String, String) -> Unit,
    onUpdateSort: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onUpdateIsInSearch: (Boolean) -> Unit
) {

    var openRemoveDialog by rememberSaveable { mutableStateOf(false) }
    var openFilterSheet by rememberSaveable { mutableStateOf(false) }

    if (openRemoveDialog) {
        ShizenAlertDialog(
            onDismiss = { openRemoveDialog = false },
            onConfirm = {
                openRemoveDialog = false
                runBlocking {
                    onRemoveSelectedItems()
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = stringResource(R.string.remove_books)
                )
            },
            title = {
                Text(
                    stringResource(R.string.remove_selected),
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            confirmationLabel = "Delete",
        ) {
            Text(
                text = "Are you sure you want to delete selected?",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }

    if (openFilterSheet) {
        FilterSheet(
            onDismiss = { openFilterSheet = false },
            uiState = uiState,
            onUpdateSort = onUpdateSort,
            onUpdateFilter = onUpdateFilter,
        )
    }

    ShizenTopBar(
        title = currentScreen.title,
        actions = {
            if (uiState.isInSearch) {
                DockedSearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    query = uiState.searchQuery,
                    onQueryChange = { onSearchQueryChange(it) },
                    onSearch = { onSearchQueryChange(it) },
                    leadingIcon = {
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Outlined.Search, null
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = onClearSearchQuery) {
                            Icon(
                                Icons.Outlined.Clear, null
                            )
                        }
                    },
                    active = false,
                    onActiveChange = { onUpdateIsInSearch(it) }
                ) {}
            } else if (uiState.isInSelection) {
                IconButton(
                    onClick = onSelectAll
                ) {
                    Icon(
                        painter = painterResource(R.drawable.twotone_done_all_24),
                        tint = Color.Green,
                        contentDescription = stringResource(R.string.select_all_items)
                    )
                }
                IconButton(onClick = onClearSelection) {
                    Icon(
                        painter = painterResource(R.drawable.outline_remove_done_24),
                        tint = Color.Red,
                        contentDescription = stringResource(R.string.clear_selection)
                    )
                }
                IconButton(onClick = { openRemoveDialog = true }) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        tint = Color.Red,
                        contentDescription = stringResource(R.string.remove_selected)
                    )
                }
            } else if (currentScreen != ShizenScreen.Settings) {
                IconButton(onClick = { openFilterSheet = true }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_sort_24),
                        contentDescription = stringResource(R.string.sort)
                    )
                }

                IconButton(onClick = { onUpdateIsInSearch(true) }) {
                    Icon(
                        imageVector = EvaIcons.Fill.Search,
                        contentDescription = stringResource(R.string.search)
                    )
                }
            }
        },
    )
}
