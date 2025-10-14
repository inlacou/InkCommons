package com.inlacou.lib.inkcomposeextensions.input.text

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun StringTextField(
    value: String,
    label: String = "",
    onValueChange: (value: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var internalValue by remember { mutableStateOf(value) }

    val onValueChange: (String) -> Unit = {
        internalValue = it
        onValueChange.invoke(it)
    }

    if(label.isNotEmpty()) {
        OutlinedTextField(
            modifier = modifier,
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            maxLines = 1,
            value = internalValue,
            onValueChange = onValueChange,
        )
    } else TextField(
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
        maxLines = 1,
        value = internalValue,
        onValueChange = onValueChange,
    )
}