package jp.aqua.shizen.utils.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ShizenAlertDialog(
    modifier: Modifier = Modifier,
    confirmationLabel: String = "Confirm",
    dismissLabel: String = "Cancel",
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
    icon: @Composable () -> Unit = {},
    title: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Card {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 15.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                icon()
                title()
                content()
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(dismissLabel)
                    }
                    TextButton(onClick = onConfirm) {
                        Text(confirmationLabel)
                    }
                }
            }
        }
    }
}

