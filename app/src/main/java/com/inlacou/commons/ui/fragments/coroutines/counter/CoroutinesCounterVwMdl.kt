package com.inlacou.commons.ui.fragments.coroutines.counter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class CoroutinesCounterVwMdl : ViewModel() {
    private val _sourceFlow = MutableStateFlow(0)
    val sourceFlow: StateFlow<Int> get() = _sourceFlow

    private val _transformedFlow = _sourceFlow.map { it * it }
    val transformedFlow: Flow<Int> get() = _transformedFlow

    fun increment() {
        _sourceFlow.value = _sourceFlow.value + 1
    }
    fun set(i: Int) {
        _sourceFlow.value = i
    }
}