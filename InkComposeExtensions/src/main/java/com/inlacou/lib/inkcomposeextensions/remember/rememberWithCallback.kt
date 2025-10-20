package com.inlacou.lib.inkcomposeextensions.remember

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember

@Composable
fun rememberWithCallback(
    getter: () -> Int,
    setter: (Int) -> Unit
): MutableState<Int> {
    val state = remember { mutableIntStateOf(getter()) }

    LaunchedEffect(state.intValue) { setter(state.intValue) }

    return state
}
