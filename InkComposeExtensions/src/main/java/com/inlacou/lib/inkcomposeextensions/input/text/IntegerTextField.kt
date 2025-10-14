package com.inlacou.lib.inkcomposeextensions.input.text

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun IntegerTextField(
    value: Int,
    label: String = "",
    onValueChange: (value: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val onValueChange: (String) -> Unit = { it.toIntOrNull()?.let { onValueChange.invoke(it) } }

    if(label.isNotEmpty()) {
        OutlinedTextField(
            modifier = modifier,
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            maxLines = 1,
            value = value.toString(),
            onValueChange = onValueChange,
        )
    } else {
        TextField(
            modifier = modifier,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            maxLines = 1,
            value = value.toString(),
            onValueChange = onValueChange,
        )
    }
}