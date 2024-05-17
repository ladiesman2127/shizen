@file:Suppress("DEPRECATION")

package jp.aqua.shizen.dictionary.wordcard.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewStateWithHTMLData
import java.io.File


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WordCard(
    modifier: Modifier = Modifier,
    url: String
) {
    val baseUrl = "file://${url.substringBeforeLast('/')}/"
    val webViewState = rememberWebViewStateWithHTMLData(
        String(File(url).readBytes()),
        baseUrl,
        "UTF-8",
        "text/html",
        baseUrl
    )

    val bgColor = MaterialTheme.colorScheme.primaryContainer.toArgb()
    Dialog(onDismissRequest = { /*TODO*/ }) {

        WebView(
            state = webViewState,
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp)
                .size(500.dp)
                .clip(RoundedCornerShape(5.dp)),
            captureBackPresses = false,
            onCreated = { view ->
                view.settings.javaScriptEnabled = true
                view.settings.allowFileAccess = true
                view.setBackgroundColor(bgColor)
            },
        )
    }
}