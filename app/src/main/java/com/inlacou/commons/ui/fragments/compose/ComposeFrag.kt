package com.inlacou.commons.ui.fragments.compose

import ComboBoxMulti
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.inlacou.commons.ui.fragments.coroutines.counter.CoroutinesCounterFrag
import com.inlacou.inker.Inker
import com.inlacou.lib.inkcomposeextensions.dialogs.TextInputDialog
import com.inlacou.lib.inkcomposeextensions.dialogs.WarningDialog
import com.inlacou.lib.inkcomposeextensions.input.combo.ComboBox
import com.inlacou.lib.inkcomposeextensions.input.combo.ComboBoxItem
import com.inlacou.lib.inkcomposeextensions.input.text.DoubleTextField
import com.inlacou.lib.inkcomposeextensions.input.text.InkTextField
import com.inlacou.lib.inkcomposeextensions.input.text.IntegerTextField
import com.inlacou.lib.inkcomposeextensions.input.text.YearMonthTextField
import java.util.Calendar

class ComposeFrag : Fragment() {

    companion object {
        @JvmOverloads
        fun create(model: Int = 0): ComposeFrag {
            val fragment = ComposeFrag()
            val args = Bundle()
            args.putInt("model", model)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    // Put the composables you want to test here
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Testing Jetpack Compose UI inside Fragment")

                        var dialog by remember { mutableStateOf("") }

                        var doubleValue by remember { mutableDoubleStateOf(0.0) }
                        Text("Double value: $doubleValue", color = Color.White)
                        InkTextField(doubleValue, label = "label", onValueChange = { doubleValue = it })

                        var intValue by remember { mutableIntStateOf(0) }
                        Text("Int value: $intValue", color = Color.White)
                        InkTextField(intValue, onValueChange = { intValue = it })

                        var monthValue by remember { mutableIntStateOf(Calendar.getInstance().get(Calendar.MONTH)) }
                        var yearValue by remember { mutableIntStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
                        Text("Year/Month value: $yearValue/${(monthValue+1).toString().padStart(2, '0')}", color = Color.White)
                        InkTextField(
                            yearValue, monthValue,
                            onValueChange = { year, month ->
                                Inker.d { "selected year=$year month=$month" }
                                yearValue = year; monthValue = month
                            }
                        )

                        val cities = listOf(
                            "Bilbao",
                            "Berango",
                            "Vitoria",
                            "Erandio",
                            "Astrabu",
                            "Donostia",
                        ).map {
                            object : ComboBoxItem {
                                @Composable
                                override fun getDisplay(): String = it
                            }
                        }

                        var selectedItem by remember { mutableStateOf(cities.first()) }
                        Text("Selected item: ${selectedItem.getDisplay()}", color = Color.White)
                        ComboBox(
                            label = "Cities",
                            items = cities,
                            selectedItem = selectedItem,
                            onItemSelected = { selectedItem = it }
                        )

                        var selectedItems by remember { mutableStateOf(emptyList<ComboBoxItem>()) }
                        Text("Selected items: $selectedItems", color = Color.White)
                        ComboBoxMulti(
                            label = "Cities (multi)",
                            items = cities,
                            selectedItems = selectedItems,
                            onItemsSelected = {
                                Inker.d { "selectedItems: $it" }
                                selectedItems = it
                            }
                        )

                        Button(
                            onClick = {
                                dialog = "input"
                            }
                        ) {
                            Text("Show TextInputDialog")
                        }

                        Button(
                            onClick = {
                                dialog = "warning"
                            }
                        ) {
                            Text("Show WarningDialog")
                        }

                        when(dialog) {
                            "input" -> TextInputDialog(
                                onAccept = { Inker.d { "onAccept: $it" }; dialog = "" },
                                onCancel = { Inker.d { "onCancel" }; dialog = "" },
                                title = "Title",
                                positiveButtonText = "Accept"
                            )
                            "warning" -> WarningDialog(
                                onAccept = { Inker.d { "onAccept" }; dialog = "" },
                                onCancel = { Inker.d { "onCancel" }; dialog = "" },
                                text = "Are you sure?",
                                positiveButtonText = "Accept"
                            )
                        }
                    }
                }
            }
        }
    }
}