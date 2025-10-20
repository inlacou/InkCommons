package com.inlacou.inkkotlincommons.flows

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class TimerFlow(
    private val periodMillis: Long = 1000L,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) {
    private var tickerJob: Job? = null
    private val _ticks = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val ticks: SharedFlow<Unit> = _ticks.asSharedFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    /** Start emitting ticks every [periodMillis] milliseconds. */
    fun start() {
        if (_isRunning.value) return
        _isRunning.value = true

        tickerJob = scope.launch {
            while (isActive && _isRunning.value) {
                _ticks.emit(Unit)
                delay(periodMillis)
            }
        }
    }

    /** Stop emitting ticks. */
    fun stop() {
        _isRunning.value = false
        tickerJob?.cancel()
        tickerJob = null
    }

    /** Cancel all coroutines in the internal scope. */
    fun clear() {
        scope.cancel()
    }
}