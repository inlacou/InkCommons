package com.inlacou.lib.inkcomposeextensions.input.text

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun DoubleTextField(
    value: Double,
    onValueChange: (value: Double) -> Unit
) {
    TextField(
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
        value = value.toString(),
        onValueChange = { it.toDoubleOrNull()?.let { onValueChange.invoke(it) } },
    )
}