package jp.aqua.shizen.dictionary.deck.presentation.components

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import jp.aqua.shizen.R
import jp.aqua.shizen.dictionary.deck.presentation.WordCardUiState
import jp.aqua.shizen.item.model.TocEntry
import jp.aqua.shizen.utils.ShizenReorderableColumn
import jp.aqua.shizen.utils.dialog.ShizenFullScreenDialog
import jp.aqua.shizen.utils.extensions.toHtmlAudio
import jp.aqua.shizen.utils.extensions.toHtmlImage
import jp.aqua.shizen.utils.extensions.toHtmlVideo
import jp.aqua.shizen.utils.getFileName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction2


@Composable
fun AddWordCardDialog(
    cardUiState: WordCardUiState,
    updateFront: (String) -> Unit,
    updateBack: (String) -> Unit,
    addFrontContent: (TocEntry) -> Unit,
    addBackContent: (TocEntry) -> Unit,
    updateFrontContent: (TocEntry) -> Unit,
    updateBackContent: (TocEntry) -> Unit,
    removeFrontContent: (TocEntry) -> Unit,
    removeBackContent: (TocEntry) -> Unit,
    swapFrontContent: (Int, Int) -> Unit,
    swapBackContent: (Int, Int) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    storeContent: KSuspendFunction2<Uri, Context, Unit>,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    val context = LocalContext.current

    val addContentLauncherFront = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
        onResult = { contents ->
            for (contentUri in contents) {
                val storeContentJob = coroutineScope.launch {
                    storeContent(contentUri, context)
                }
                val type = context.contentResolver.getType(contentUri)?.substringBefore('/')
                val contentFileName = contentUri.getFileName(context)
                var content: TocEntry? = null
                when (type) {
                    "image" -> {
                        content =
                            TocEntry(title = contentFileName, href = contentFileName.toHtmlImage())
                    }

                    "video" -> {
                        content =
                            TocEntry(title = contentFileName, href = contentFileName.toHtmlVideo())
                    }

                    "audio" -> {
                        content =
                            TocEntry(title = contentFileName, href = contentFileName.toHtmlAudio())
                    }
                }
                content?.let {
                    addFrontContent(it)
                    storeContentJob.invokeOnCompletion { _ ->
                        updateFrontContent(it)
                    }
                }
            }
        }
    )

    val addContentLauncherBack = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
        onResult = { contents ->
            for (contentUri in contents) {
                val storeContentJob = coroutineScope.launch {
                    storeContent(contentUri, context)
                }
                val type = context.contentResolver.getType(contentUri)?.substringBefore('/')
                val contentFileName = contentUri.getFileName(context)
                var content: TocEntry? = null
                when (type) {
                    "image" -> {
                        content = TocEntry(
                            title = contentFileName,
                            href = contentFileName.toHtmlImage()
                        )
                    }

                    "video" -> {
                        content = TocEntry(
                            title = contentFileName,
                            href = contentFileName.toHtmlVideo()
                        )
                    }

                    "audio" -> {
                        content = TocEntry(
                            title = contentFileName,
                            href = contentFileName.toHtmlAudio()
                        )
                    }
                }
                content?.let {
                    addBackContent(it)
                    storeContentJob.invokeOnCompletion { _ ->
                        updateBackContent(it)
                    }
                }
            }
        }
    )

    ShizenFullScreenDialog(
        title = "Add card",
        onDismiss = onDismiss,
        onConfirm = {
            coroutineScope.launch { onConfirm() }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                label = { Text("Card front") },
                value = cardUiState.front,
                onValueChange = { updateFront(it) },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            addContentLauncherFront.launch(
                                arrayOf(
                                    "image/*",
                                    "video/*",
                                    "audio/*"
                                )
                            )
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.outline_attach_file_24),
                            "Add content to the card"
                        )
                    }
                }
            )
            ShizenReorderableColumn(
                items = cardUiState.frontTableOfContents,
                isItemsRemovable = true,
                onRemove = removeFrontContent,
                onSwap = swapFrontContent
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                label = { Text("Card back") },
                value = cardUiState.back,
                onValueChange = { updateBack(it) },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            addContentLauncherBack.launch(
                                arrayOf(
                                    "image/*",
                                    "video/*",
                                    "audio/*"
                                )
                            )
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.outline_attach_file_24),
                            "Add content to the card"
                        )
                    }
                }
            )
            ShizenReorderableColumn(
                items = cardUiState.backTableOfContents,
                isItemsRemovable = true,
                onRemove = removeBackContent,
                onSwap = swapBackContent
            )
        }
    }
}

