package jp.aqua.shizen.utils.scaffold

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import jp.aqua.shizen.R
import jp.aqua.shizen.navigation.ShizenScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nl.siegmann.epublib.service.MediatypeService
import kotlin.reflect.KSuspendFunction2
import kotlin.system.measureTimeMillis

@Composable
fun ShizenScreenFab(
    currentScreen: ShizenScreen?,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState,
    addBook: KSuspendFunction2<Uri, Context, Result<String>>,
    turnOnAddingFolder: () -> Unit,
    content: @Composable () -> Unit,
) {

    val context = LocalContext.current

    val bookAddLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        if (uris.isNotEmpty()) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = when (uris.size) {
                        1 -> context.getString(R.string.adding_a_book)
                        else -> context.getString(R.string.adding_books)
                    }
                )
            }
            for (uri in uris) {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                coroutineScope.launch {
                    val res = measureTimeMillis {
                        addBook(uri, context)
                    }
                    println(res)
//                        .onSuccess { bookTitle ->
//                            if (bookTitle.isNotBlank())
//                                snackbarHostState
//                                    .showSnackbar(
//                                        message = bookTitle + context.getString(
//                                            R.string.added_successfully
//                                        )
//                                    )
//                        }
//                        .onFailure {
//                            snackbarHostState
//                                .showSnackbar(
//                                    message = context.getString(
//                                        R.string.failed_to_add_book
//                                    )
//                                )
//                        }
                }
            }
        }
    }


    if (currentScreen != ShizenScreen.Settings) {
        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.inverseSurface,
            contentColor = MaterialTheme.colorScheme.inverseOnSurface,
            onClick = {
                when (currentScreen) {
                    ShizenScreen.Read -> {
                        bookAddLauncher.launch(arrayOf(MediatypeService.EPUB.name))
                    }

                    ShizenScreen.Listen -> {
                        turnOnAddingFolder()
                    }

                    ShizenScreen.Dictionary -> {
                        turnOnAddingFolder()
                    }

                    else -> Unit
                }
            },
        ) {
            content()
        }
    }
}