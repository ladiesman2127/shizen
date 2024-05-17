package jp.aqua.shizen.read.reader.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.aqua.shizen.ui.theme.ShizenTheme

@Composable
fun WordButton(word: String) {
    TextButton(onClick = { /*TODO*/ }) {
        Text(word)
    }
}

@Preview
@Composable
fun WordButtonPreview() {
    ShizenTheme {
        WordButton("Optimus")
    }
}