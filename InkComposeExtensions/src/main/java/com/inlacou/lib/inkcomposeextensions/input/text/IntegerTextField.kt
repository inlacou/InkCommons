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
fun IntegerTextField(
    value: Int,
    modifier: Modifier = Modifier,
    label: String = "",
    onValueChange: ((value: Int) -> Unit)?,
) {
    var internalValue by remember(value) { mutableStateOf(value.toString()) }

    val myOnValueChange: (String) -> Unit = {
        if(it.isEmpty()) internalValue = it
        it.toIntOrNull()?.let { doubleValue ->
            internalValue = it
            onValueChange?.invoke(doubleValue)
        }
    }

    if(label.isNotEmpty()) {
        OutlinedTextField(
            modifier = modifier,
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            maxLines = 1,
            value = internalValue,
            onValueChange = myOnValueChange,
            readOnly = onValueChange == null,
        )
    } else {
        TextField(
            modifier = modifier,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            maxLines = 1,
            value = internalValue,
            onValueChange = myOnValueChange,
            readOnly = onValueChange == null,
        )
    }
}