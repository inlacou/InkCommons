package com.inlacou.lib.inkcomposeextensions.input.combo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

/**
 * More custom, instead of relying on [ExposedDropdownMenuBox]
 */
@Composable
fun <T: ComboBoxItem> CustomComboBox(
    label: String,
    selectedItem: T,
    items: List<T>,
    modifier: Modifier = Modifier,
    onItemSelected: (T) -> Unit
) {
    var mExpanded by remember { mutableStateOf(false) }

    // This value is used to assign to the DropDown the same width as the ComboBox
    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}

    val icon =
        if (mExpanded) Icons.Filled.KeyboardArrowUp
        else Icons.Filled.KeyboardArrowDown

    Column(modifier) {
        OutlinedTextField(
            value = selectedItem.getDisplay(),
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { mTextFieldSize = it.size.toSize() }
                .clickable { mExpanded = !mExpanded },
            enabled = false,
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = "contentDescription",
                    modifier = Modifier.clickable { mExpanded = !mExpanded },
                )
            }
        )

        DropdownMenu(
            expanded = mExpanded,
            onDismissRequest = { mExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item.getDisplay()) },
                    onClick = {
                        onItemSelected(items.first { it == item })
                        mExpanded = false
                    }
                )
            }
        }
    }
}