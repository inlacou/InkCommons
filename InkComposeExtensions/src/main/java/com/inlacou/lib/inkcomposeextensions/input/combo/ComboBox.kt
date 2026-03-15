package com.inlacou.lib.inkcomposeextensions.input.combo

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T: ComboBoxItem> ComboBox(
    label: String,
    selectedItem: T,
    items: List<T>,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val isEnabled by rememberUpdatedState { items.isNotEmpty() }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (isEnabled()) {
                expanded = !expanded
                if (!expanded) {
                    onItemSelected(selectedItem)
                }
            }
        },
        modifier = modifier,
    ) {
        OutlinedTextField(
            enabled = isEnabled(),
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = selectedItem.display,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEach { label ->
                DropdownMenuItem(
                    text = { Text(text = label.display) },
                    onClick = {
                        onItemSelected(items.first { it.display == label.display })
                        expanded = false
                    }
                )
            }
        }
    }
}