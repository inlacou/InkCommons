package com.inlacou.lib.inkcomposeextensions.input.combo

import androidx.compose.runtime.Composable

interface ComboBoxItem {
    @Composable
    fun getDisplay(): String
}
