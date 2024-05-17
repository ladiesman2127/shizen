package jp.aqua.shizen.listen.components

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import jp.aqua.shizen.R
import jp.aqua.shizen.utils.dialog.ShizenAlertDialog
import jp.aqua.shizen.utils.ScreenUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction0

@Composable
fun AddFolderDialog(
    modifier: Modifier = Modifier,
    uiState: ScreenUiState,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    turnOffFolderAdding: () -> Unit,
    saveFolder: KSuspendFunction0<Unit>,
    updateFolderCover: (String) -> Unit,
    updateIsCoverError: (Boolean) -> Unit,
    updateFolderTitle: (String) -> Unit
) {
    val context = LocalContext.current
    ShizenAlertDialog(
        modifier = modifier,
        title = {
            Text(
                text = uiState.folderTitle.ifEmpty { "Title" },
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        },
        onDismiss = { turnOffFolderAdding() },
        onConfirm = {
            coroutineScope.launch {
                saveFolder()
                turnOffFolderAdding()
            }
        },
        confirmationLabel = "Save",
    ) {
        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) {
            it?.let {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                updateFolderCover(it.toString())
            }
        }
        if (uiState.folderCover != null) {
            Row(Modifier.fillMaxWidth()) {
                Spacer(Modifier.weight(1f))
                Box(
                    Modifier
                        .weight(3f)
                        .aspectRatio(2 / 3f)
                        .clip(RoundedCornerShape(3.dp))
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { launcher.launch(arrayOf("image/*")) },
                        model = uiState.folderCover,
                        contentDescription = "Folder cover",
                        contentScale = ContentScale.Crop,
                        onError = { updateIsCoverError(true) },
                        onSuccess = { updateIsCoverError(false) },
                    )
                }
                Spacer(Modifier.weight(1f))
            }
        }
        Column(
            Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.folderTitle.ifEmpty { "" },
                singleLine = true,
                onValueChange = { updateFolderTitle(it) },
                label = { Text("Title") },
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.folderCover ?: "",
                onValueChange = { updateFolderCover(it) },
                singleLine = true,
                label = { Text("Cover") },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            launcher.launch(arrayOf("image/*"))
                        }
                    ) {
                        Icon(
                            painterResource(R.drawable.outline_attach_file_24),
                            stringResource(R.string.add_cover),
                        )
                    }
                },
                isError = uiState.isCoverError == true
            )
        }
    }
}