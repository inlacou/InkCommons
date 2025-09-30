package com.inlacou.lib.inkcomposeextensions.combo

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

@Composable
fun <T: ComboBoxItem> DropdownBox(
    label: String,
    selectedItem: T,
    items: List<T>,
    padding: Dp = 20.dp,
    onItemSelected: (T) -> Unit
) {
    var mExpanded by remember { mutableStateOf(false) }

    // This value is used to assign to the DropDown the same width as the ComboBox
    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}

    val icon =
        if (mExpanded) Icons.Filled.KeyboardArrowUp
        else Icons.Filled.KeyboardArrowDown

    Column(Modifier.padding(padding)) {
        OutlinedTextField(
            value = selectedItem.display,
            onValueChange = {
                onItemSelected(items.first { item -> item.display == it })
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { mTextFieldSize = it.size.toSize() }
                .clickable { mExpanded = !mExpanded },
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors().copy(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledIndicatorColor = MaterialTheme.colorScheme.outline,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                // For Icons
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant),
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
            items.forEach { label ->
                DropdownMenuItem(
                    text = { Text(text = label.display) },
                    onClick = {
                        onItemSelected(items.first { it.display == label.display })
                        mExpanded = false
                    }
                )
            }
        }
    }
}