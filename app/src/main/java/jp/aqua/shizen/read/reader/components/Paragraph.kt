package jp.aqua.shizen.read.reader.components
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle

@Composable
fun Paragraph(
    paragraph: String,
) {
    val words = paragraph.split(" ")
    words.forEach { word ->
        WordButton(word)
    }
}