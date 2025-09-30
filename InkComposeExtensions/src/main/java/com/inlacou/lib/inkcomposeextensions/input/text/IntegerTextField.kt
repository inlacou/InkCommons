package com.inlacou.lib.inkcomposeextensions.input.text

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun IntegerTextField(
    value: Int,
    onValueChange: (value: Int) -> Unit
) {
    TextField(
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
        value = value.toString(),
        onValueChange = { it.toIntOrNull()?.let { onValueChange.invoke(it) } },
    )
}