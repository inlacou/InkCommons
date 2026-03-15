package com.inlacou.lib.inkcomposeextensions.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun TextInputDialog(
    onAccept: (String) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    title: String,
    inputLabel: String = "",
    positiveButtonText: String,
    negativeButtonText: String = "Cancel",
) {
    var input by remember { mutableStateOf("") }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onCancel,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                label = { Text(inputLabel) },
                value = input,
                onValueChange = { input = it },
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = { onAccept(input.trim()) }) {
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
