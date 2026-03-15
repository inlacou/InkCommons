package com.inlacou.lib.inkcomposeextensions.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WarningDialog(
    onAccept: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Warning",
    text: String,
    positiveButtonText: String,
    negativeButtonText: String = "Cancel",
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onCancel,
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = {
            Button(onClick = onAccept) {
                Text(positiveButtonText)
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text(negativeButtonText)
            }
        }
    )
}