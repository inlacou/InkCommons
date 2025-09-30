package com.inlacou.lib.inkcomposeextensions.input.text

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun DoubleTextField(
    value: Double,
    onValueChange: (value: Double) -> Unit
) {
    var internalValue by remember { mutableStateOf(value.toString()) }

    TextField(
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
        maxLines = 1,
        value = internalValue,
        onValueChange = {
            it.toDoubleOrNull()?.let { doubleValue ->
                internalValue = it
                onValueChange.invoke(doubleValue)
            }
        },
    )
}