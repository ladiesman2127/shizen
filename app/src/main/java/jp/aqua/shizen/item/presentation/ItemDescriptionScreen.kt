package jp.aqua.shizen.item.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.ArrowBack
import jp.aqua.shizen.R
import jp.aqua.shizen.item.model.TocEntry
import jp.aqua.shizen.item.presentation.components.ItemDescription
import jp.aqua.shizen.item.presentation.components.ItemDescriptionHeader
import jp.aqua.shizen.utils.SwipeToDeleteContainer
import jp.aqua.shizen.utils.extensions.isAtTop
import jp.aqua.shizen.utils.list.ShizenListItem
import jp.aqua.shizen.utils.scaffold.ShizenTopBar
import kotlinx.coroutines.launch
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDescriptionScreen(
    modifier: Modifier = Modifier,
    uiState: ItemUiState,
    fabText: String,
    isEditable: Boolean = false,
    isAddFab: Boolean = false,
    navigateUp: () -> Unit,
    onEdit: () -> Unit = {},
    onAdd: () -> Unit = {},
    updateIsDescriptionExpanded: () -> Unit,
    onOpen: (TocEntry?) -> Unit = {},
    onOpenIndexed: (Int?) -> Unit = {},
    updateCover: () -> Unit = {},
    onDeleteContentWithUpdating: (TocEntry) -> Unit = {},
    isContentRemovable: Boolean = false,
) {
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val snackbarHostState = SnackbarHostState()
    Scaffold(
        modifier = modifier,
        topBar = {
            val isAtTop by lazyListState.isAtTop(threshold = 40.dp)
            val alpha by animateFloatAsState(targetValue = if (isAtTop) 0f else 1f, label = "")
            val containerColor by animateColorAsState(
                targetValue = MaterialTheme.colorScheme.primary.copy(alpha = alpha),
                label = ""
            )
            val contentColor by animateColorAsState(
                targetValue = MaterialTheme.colorScheme.onPrimary.copy(alpha = alpha),
                label = ""
            )
            Surface(color = containerColor) {
                ShizenTopBar(
                    title = uiState.title,
                    navigationIcon = {
                        IconButton(onClick = navigateUp) {
                            Icon(
                                imageVector = EvaIcons.Fill.ArrowBack,
                                contentDescription = stringResource(R.string.close_the_book_description),
                                tint =
                                if (isAtTop)
                                    MaterialTheme.colorScheme.onBackground
                                else
                                    MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = containerColor,
                        scrolledContainerColor = containerColor,
                        titleContentColor = contentColor,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = contentColor,
                    ),
                    actions = {
                        if (isEditable) {
                            IconButton(onClick = onEdit) {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = "Edit item",
                                    tint =
                                    if (isAtTop)
                                        MaterialTheme.colorScheme.onBackground
                                    else
                                        MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                if (isAddFab) {
                    FloatingActionButton(onClick = onAdd) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = stringResource(R.string.add_content)
                        )
                    }
                }
                FloatingActionButton(
                    onClick = {
                        when (uiState.tableOfContents.size) {
                            0 -> {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Add content",
                                        withDismissAction = true
                                    )
                                }
                            }

                            else -> {
                                onOpen(null)
                                onOpenIndexed(null)
                            }
                        }

                    } // TODO CONTINUATION!!
                ) {
                    Row(
                        modifier = Modifier.padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Filled.PlayArrow,
                            contentDescription = stringResource(R.string.resume_or_start)
                        )
                        AnimatedVisibility(
                            !lazyListState.canScrollBackward ||
                                    !lazyListState.canScrollForward
                        ) {
                            Text(fabText, Modifier.padding(start = 5.dp))
                        }
                    }
                }
            }
        },
    ) { innerPadding ->
        LazyColumn(state = lazyListState) {
            item(contentType = { 0 }, key = "HEADER") {
                ItemDescriptionHeader(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    uiState = uiState,
                    innerPadding = innerPadding,
                    updateCover = updateCover,
                )
            }
            item(contentType = { 1 }, key = "DESCRIPTION") {
                ItemDescription(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 10.dp, start = 16.dp, end = 16.dp),
                    uiState = uiState,
                    updateIsDescriptionExpanded = updateIsDescriptionExpanded
                )
            }
            itemsIndexed(
                uiState.tableOfContents,
                contentType = { _, _ -> 2 },
                key = { _, item -> item.id }
            ) { index, item ->
                SwipeToDeleteContainer(
                    item = item,
                    onDelete = { onDeleteContentWithUpdating(it) },
                    isRemovable = isContentRemovable
                ) {
                    ShizenListItem(
                        modifier = Modifier
                            .clickable {
                                onOpen(item)
                                onOpenIndexed(index)
                            },
                        headlineContent = {
                            Text(
                                text = item.title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    )
                }
            }
        }
    }
}

