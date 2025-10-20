package com.inlacou.lib.inkcomposeextensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseViewModel(
    private val dispatchers: InkDispatchers
) : ViewModel() {

    protected fun <T> Flow<T>.flowOnUI() = flowOn(dispatchers.mainDispatcher)

    protected fun <T> Flow<T>.flowOnBackground() = flowOn(dispatchers.defaultDispatcher)

    protected fun <T> Flow<T>.flowOnIO() = flowOn(dispatchers.ioDispatcher)

    companion object {
        /**
         * Run an action on BaseViewModel's mainDispatcher
         *
         * @param action is a lambda which should be passed from caller side.
         *  It is an extension on CoroutineScope and should not return a value
         *
         * @return Job like `launch` function does normally
         */
        fun BaseViewModel.launchOnUI(
            action: suspend CoroutineScope.() -> Unit
        ) = viewModelScope.launch(dispatchers.mainDispatcher) { action() }

        fun BaseViewModel.launchOnUI(
            handler: CoroutineExceptionHandler,
            action: suspend CoroutineScope.() -> Unit
        ) = viewModelScope.launch(handler + dispatchers.mainDispatcher) { action() }

        /**
         * Run an action on BaseViewModel's defaultDispatcher
         *
         * @param action is a lambda which should be passed from caller side.
         *  It is an extension on CoroutineScope and should not return a value
         *
         * @return Job like `launch` function does normally
         */
        fun BaseViewModel.launchOnBackground(
            action: suspend CoroutineScope.() -> Unit
        ) = viewModelScope.launch(dispatchers.defaultDispatcher) { action() }

        fun BaseViewModel.launchOnBackground(
            handler: CoroutineExceptionHandler,
            action: suspend CoroutineScope.() -> Unit
        ) = viewModelScope.launch(handler + dispatchers.defaultDispatcher) { action() }

        /**
         * Run an action on Coroutine's ioDispatcher
         * (perform Input/Output operations: network calls, work with files, etc.)
         *
         * @param action is a lambda which should be passed from caller side.
         *  It is an extension on CoroutineScope and should not return a value
         *
         * @return Job like `launch` function does normally
         */
        fun BaseViewModel.launchOnIO(
            action: suspend CoroutineScope.() -> Unit
        ) = viewModelScope.launch(dispatchers.ioDispatcher) { action() }

        fun BaseViewModel.launchOnIO(
            handler: CoroutineExceptionHandler,
            action: suspend CoroutineScope.() -> Unit
        ) = viewModelScope.launch(handler + dispatchers.ioDispatcher) { action() }

        /**
         * Switch current Dispatcher to BaseViewModel's mainDispatcher
         *
         * @param action is a lambda which should be passed from caller side.
         *  It is an extension on CoroutineScope and return's a value of type T
         * @param T is a generic type of what you want the function to return at the end
         *
         * @return T
         */
        suspend fun <T> BaseViewModel.switchToUI(
            action: suspend CoroutineScope.() -> T
        ) = switchTo(dispatchers.mainDispatcher) { action() }

        /**
         * Switch current Dispatcher to BaseViewModel's defaultDispatcher
         *
         * @param block is a lambda which should be passed from caller side.
         *  It is an extension on CoroutineScope and return's a value of type T
         * @param T is a generic type of what you want the function to return at the end
         *
         * @return T
         */
        suspend fun <T> BaseViewModel.switchToBackground(
            block: suspend CoroutineScope.() -> T
        ) = switchTo(dispatchers.defaultDispatcher) { block() }

        suspend fun <T> BaseViewModel.switchToBackground(
            handler: CoroutineExceptionHandler,
            block: suspend CoroutineScope.() -> T
        ) = switchTo(handler, dispatchers.defaultDispatcher) { block() }

        /**
         * Switch current Dispatcher to BaseViewModel's ioDispatcher
         *
         * @param block is a lambda which should be passed from caller side.
         *  It is an extension on CoroutineScope and return's a value of type T
         * @param T is a generic type of what you want the function to return at the end
         *
         * @return T
         */
        suspend fun <T> BaseViewModel.switchToIO(
            block: suspend CoroutineScope.() -> T
        ) = switchTo(dispatchers.ioDispatcher) { block() }

        /**
         * Run an action on Coroutine's Main Dispatcher and await for result
         *
         * @param action is a lambda which should be passed from caller side.
         *  It is an extension on CoroutineScope and return's a value of type T
         * @param T is a generic type of what you want the function to return at the end
         *
         * @return Deferred<T> like `async` function does normally
         */
        fun <T> BaseViewModel.asyncOnUi(action: suspend CoroutineScope.() -> T) =
            viewModelScope.async(dispatchers.mainDispatcher) { action() }

        /**
         * Run an action on Coroutine's Default Dispatcher and await for result
         *  (perform operations which want to use CPU for calculations)
         *
         * @param action is a lambda which should be passed from caller side.
         *  It is an extension on CoroutineScope and return's a value of type T
         * @param T is a generic type of what you want the function to return at the end
         *
         * @return Deferred<T> like `async` function does normally
         */
        fun <T> BaseViewModel.asyncOnBackground(
            action: suspend CoroutineScope.() -> T
        ) = viewModelScope.async(dispatchers.defaultDispatcher) { action() }

        /**
         * Run an action on Coroutine's IO Dispatcher and await for result
         *  (perform Input/Output operations: network calls, work with files, etc.)
         *
         * @param action is a lambda which should be passed from caller side.
         *  It is an extension on CoroutineScope and return's a value of type T
         * @param T is a generic type of what you want the function to return at the end
         *
         * @return Deferred<T> like `async` function does normally
         */
        fun <T> BaseViewModel.asyncOnIO(action: suspend CoroutineScope.() -> T) =
            viewModelScope.async(dispatchers.ioDispatcher) { action() }
    }
}