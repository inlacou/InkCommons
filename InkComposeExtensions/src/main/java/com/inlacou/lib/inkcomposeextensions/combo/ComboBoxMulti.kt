import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.inlacou.lib.inkcomposeextensions.combo.ComboBoxItem

// TODO not tested as of 30-09-2025
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T: ComboBoxItem> MultiComboBox(
    labelText: String,
    options: List<T>,
    onOptionsChosen: (List<T>) -> Unit,
    modifier: Modifier = Modifier,
    initialSelectedItems: List<T> = emptyList(),
) {
    var expanded by remember { mutableStateOf(false) }
    // when no options available, I want ComboBox to be disabled
    val isEnabled by rememberUpdatedState { options.isNotEmpty() }
    var selectedOptionsList  = remember { mutableStateListOf<T>()}

    //Initial setup of selected ids
    initialSelectedItems.forEach {
        selectedOptionsList.add(it)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (isEnabled()) {
                expanded = !expanded
                if (!expanded) {
                    onOptionsChosen(options.filter { it in selectedOptionsList }.toList())
                }
            }
        },
        modifier = modifier,
    ) {
        val selectedSummary = when (selectedOptionsList.size) {
            0 -> ""
            1 -> selectedOptionsList.first().display
            else -> "Selected nº: ${selectedOptionsList.size}"
        }
        TextField(
            enabled = isEnabled(),
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = selectedSummary,
            onValueChange = {},
            label = { Text(text = labelText) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                onOptionsChosen(options.filter { it in selectedOptionsList }.toList())
            },
        ) {
            for (option in options) {
                
                //use derivedStateOf to evaluate if it is checked
                var checked = remember {
                    derivedStateOf { option in selectedOptionsList}
                }.value

                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = checked,
                                onCheckedChange = { newCheckedState ->
                                    if (newCheckedState) {
                                        selectedOptionsList.add(option)
                                    } else {
                                        selectedOptionsList.remove(option)
                                    }
                                },
                            )
                            Text(text = option.display)
                        }
                    },
                    onClick = {
                        if (!checked) {
                            selectedOptionsList.add(option)
                        } else {
                            selectedOptionsList.remove(option)
                        }
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}