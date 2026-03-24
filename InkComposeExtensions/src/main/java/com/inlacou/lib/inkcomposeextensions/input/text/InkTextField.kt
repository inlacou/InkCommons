package com.inlacou.lib.inkcomposeextensions.input.text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.util.Calendar

@Composable fun InkTextField(
    value: Double,
    modifier: Modifier = Modifier,
    label: String = "",
    onValueChange: ((value: Double) -> Unit)?,
) = DoubleTextField(value, modifier, label, onValueChange)

@Composable fun InkTextField(
    value: Int,
    modifier: Modifier = Modifier,
    label: String = "",
    onValueChange: ((value: Int) -> Unit)?,
) = IntegerTextField(value, modifier, label, onValueChange)

@Composable fun InkTextField(
    value: String,
    modifier: Modifier = Modifier,
    label: String = "",
    onValueChange: ((value: String) -> Unit?)?,
) = StringTextField(value, modifier, label, onValueChange)

@Composable fun InkTextField(
    year: Int,
    month: Int,
    modifier: Modifier = Modifier,
    label: String = "",
    onValueChange: ((year: Int, month: Int) -> Unit)?,
) = YearMonthTextField(
    value = Calendar.getInstance().apply { set(Calendar.YEAR, year); set(Calendar.MONTH, month) },
    modifier = modifier,
    label = label,
    onValueChange = onValueChange)
