package com.inlacou.lib.inkcomposeextensions
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Coroutine's Dispatcher provider interface.
 */
interface InkDispatchers {
    val ioDispatcher: CoroutineDispatcher
    val mainDispatcher: CoroutineDispatcher
    val defaultDispatcher: CoroutineDispatcher
}
