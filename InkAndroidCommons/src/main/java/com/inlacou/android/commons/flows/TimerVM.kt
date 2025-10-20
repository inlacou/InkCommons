package com.inlacou.android.commons.flows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inlacou.inkkotlincommons.flows.TimerFlow

class TimerVM(periodMillis: Long = 1000L) : ViewModel() {
    val timer = TimerFlow(periodMillis, viewModelScope)

    val isRunning = timer.isRunning

    fun start() = timer.start()
    fun stop() = timer.stop()

    override fun onCleared() {
        timer.clear()
        super.onCleared()
    }
}
