package jp.aqua.shizen.utils.scaffold

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ShizenSnackbar(data: SnackbarData) {
    Snackbar(Modifier.padding(8.dp)) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = data.visuals.message,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            if (data.visuals.message.contains("…")) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }
        }
    }
}