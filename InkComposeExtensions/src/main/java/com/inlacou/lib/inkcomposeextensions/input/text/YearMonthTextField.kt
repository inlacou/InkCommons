package com.inlacou.lib.inkcomposeextensions.input.text

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.inlacou.inker.Inker
import com.inlacou.inkkotlincommons.YearMonth
import com.inlacou.lib.inkcomposeextensions.dialogs.YearMonthPickerDialog

@Composable
fun YearMonthTextField(
    value: YearMonth?,
    modifier: Modifier = Modifier,
    calendarToString: (YearMonth?) -> String = {
        if(it == null) ""
        else "${it.year}/${it.month.toString().padStart(2, '0')}"
    },
    label: String = "",
    onValueChange: ((yearMonth: YearMonth) -> Unit)?,
) {
    var showDialog by remember { mutableStateOf(false) }
    var internalValue by remember(value) { mutableStateOf(value) }

    val interactionSource = remember { MutableInteractionSource() }
        .also { interactionSource ->
            LaunchedEffect(interactionSource) {
                interactionSource.interactions.collect {
                    if (it is PressInteraction.Release) {
                        showDialog = true
                    } else {
                        showDialog = false
                    }
                    Inker.d { "interaction: $it | showDialog: $showDialog" }
                }
            }
        }

    if(label.isNotEmpty()) {
        OutlinedTextField(
            modifier = modifier,
            interactionSource = interactionSource,
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Unspecified, imeAction = ImeAction.Next),
            maxLines = 1,
            value = calendarToString(internalValue),
            onValueChange = {},
            readOnly = true,
        )
    } else {
        TextField(
            modifier = modifier,
            interactionSource = interactionSource,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Unspecified, imeAction = ImeAction.Next),
            maxLines = 1,
            value = calendarToString(internalValue),
            onValueChange = {},
            readOnly = true,
        )
    }

    if(showDialog) {
        YearMonthPickerDialog(
            current = internalValue ?: YearMonth.getInstance(),
            onConfirm = { yearMonth ->
                onValueChange?.invoke(yearMonth)
                showDialog = false
            },
            onCancel = {
                showDialog = false
            },
        )
    }
}