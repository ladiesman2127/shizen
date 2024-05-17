package jp.aqua.shizen.listen.video.presentation

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import jp.aqua.shizen.R
import jp.aqua.shizen.item.model.Item
import jp.aqua.shizen.item.model.TocEntry
import jp.aqua.shizen.item.presentation.ItemDescriptionScreen
import jp.aqua.shizen.item.presentation.ItemUiState
import jp.aqua.shizen.listen.player.PlayerActivity
import jp.aqua.shizen.utils.ShizenReorderableColumn
import jp.aqua.shizen.utils.dialog.ShizenFullScreenDialog
import jp.aqua.shizen.utils.getFileName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun VideoScreen(
    context: Context,
    viewModel: VideoViewModel,
    uiState: ItemUiState,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onNavigateUp: () -> Unit
) {
    val addContentLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        uris.forEachIndexed { index, uri ->
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            val name = uri.getFileName(context)
            val uriString = uri.toString()
            coroutineScope.launch {
                viewModel.addContent(
                    content = TocEntry(title = name, href = uriString)
                )
                viewModel.updateItem()
            }
        }
    }

    val coverChangeLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            viewModel.updateCover(it.toString())
            coroutineScope.launch {
                viewModel.updateItem()
            }
        }
    }

    Log.i("TOC ELEMENT", uiState.tableOfContents.size.toString())

    if (!uiState.isEditing) {
        ItemDescriptionScreen(
            uiState = uiState,
            navigateUp = onNavigateUp,
            isAddFab = true,
            fabText = "Listen",
            onAdd = {
                addContentLauncher.launch(arrayOf("video/*"))
            },
            onOpenIndexed = { episode ->
                Intent(context, PlayerActivity::class.java).apply {
                    putExtra(Item.ID, uiState.id)
                    putExtra("EPISODE", episode)
                    context.startActivity(this)
                }
            },
            isEditable = true,
            onEdit = viewModel::updateIsEditing,
            updateIsDescriptionExpanded = viewModel::updateIsDescriptionExpanded,
            updateCover = { coverChangeLauncher.launch(arrayOf("image/*")) },
            onDeleteContentWithUpdating = viewModel::removeContentWithUpdating,
            isContentRemovable = true
        )
    } else {
        ShizenFullScreenDialog(
            title = uiState.title,
            onDismiss = { viewModel.updateIsEditing() },
            onConfirm = {
                coroutineScope.launch {
                    viewModel.updateItem()
                    viewModel.updateIsEditing()
                }
            }
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(10.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    value = uiState.title,
                    onValueChange = { viewModel.updateTitle(it) },
                    label = { Text("Title") },
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.cover ?: "",
                    onValueChange = { viewModel.updateCover(it) },
                    singleLine = true,
                    label = { Text("Cover") },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                coverChangeLauncher.launch(arrayOf("image/*"))
                            }
                        ) {
                            Icon(
                                painterResource(R.drawable.outline_attach_file_24),
                                stringResource(R.string.update_cover),
                            )
                        }
                    }
                )

                ExposedDropdownMenuBox(
                    expanded = uiState.isStatusExpanded,
                    onExpandedChange = {
                        viewModel.updateIsStatusExpanded()
                    },
                ) {
                    OutlinedTextField(
                        value = uiState.status.name,
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = uiState.isStatusExpanded
                            )
                        },
                        label = { Text("Status") },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = uiState.isStatusExpanded,
                        onDismissRequest = {
                            viewModel.updateIsStatusExpanded()
                        }
                    ) {
                        uiState.allStatuses.forEach { status ->
                            DropdownMenuItem(
                                text = {
                                    Text(status.name)
                                },
                                onClick = {
                                    viewModel.updateStatus(status)
                                    viewModel.updateIsStatusExpanded()
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.language,
                    onValueChange = { viewModel.updateLanguage(it) },
                    label = { Text("Language") },
                )

                ShizenReorderableColumn(
                    items = uiState.tableOfContents,
                    isItemsRemovable = true,
                    onRemove = viewModel::removeContent,
                    onSwap = viewModel::onSwap
                )

                TextButton(
                    onClick = { addContentLauncher.launch(arrayOf("video/*")) }
                ) {
                    Text(stringResource(R.string.add_content))
                }
            }
        }
    }
}
