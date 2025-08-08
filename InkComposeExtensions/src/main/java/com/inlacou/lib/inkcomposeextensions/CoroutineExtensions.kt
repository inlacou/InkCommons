package com.inlacou.lib.inkcomposeextensions


import kotlinx.coroutines.*

/**
 * Switch current Dispatcher to Coroutine's Main Dispatcher
 *
 * @param action is a lambda which should be passed from caller side.
 *  It is an extension on CoroutineScope and return's a value of type T
 * @param T is a generic type of what you want the function to return at the end
 *
 * @return T
 */
suspend fun <T> switchToUi(action: CoroutineScope.() -> T) =
    withContext(Dispatchers.Main, action)

/**
 * Switch current Dispatcher to Coroutine's IO Dispatcher
 *
 * @param block is a lambda which should be passed from caller side.
 *  It is an extension on CoroutineScope and return's a value of type T
 * @param T is a generic type of what you want the function to return at the end
 *
 * @return T
 */
suspend fun <T> switchToIO(block: suspend CoroutineScope.() -> T) =
    withContext(Dispatchers.IO, block)

/**
 * Switch current Dispatcher to Coroutine's Default Dispatcher
 *
 * @param block is a lambda which should be passed from caller side.
 *  It is an extension on CoroutineScope and return's a value of type T
 * @param T is a generic type of what you want the function to return at the end
 *
 * @return T
 */
suspend fun <T> switchToBG(block: suspend CoroutineScope.() -> T) =
    withContext(Dispatchers.Default) { block() }

suspend fun <T> switchToBG(
    handler: CoroutineExceptionHandler,
    block: suspend CoroutineScope.() -> T,
) =
    withContext(handler + Dispatchers.Default) { block() }

val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

/**
 * Switch current Dispatcher to given Coroutine's Dispatcher.
 *
 * @param dispatcher is a Coroutine's Dispatcher to which will switch current Dispatcher .
 * @param block is a lambda which should be passed from caller side.
 *  It is an extension on CoroutineScope and return's a value of type T
 * @param T is a generic type of what you want the function to return at the end
 *
 * @return T
 */
suspend fun <T> switchTo(dispatcher: CoroutineDispatcher, block: suspend CoroutineScope.() -> T) =
    withContext(dispatcher) { block() }

suspend fun <T> switchTo(
    handler: CoroutineExceptionHandler,
    dispatcher: CoroutineDispatcher,
    block: suspend CoroutineScope.() -> T,
) = withContext(handler + dispatcher) { block() }

/**
 * Launch lambda with Coroutine's IO Dispatcher + Coroutine's Job.
 * @param block is a lambda which should be passed from caller side.
 */
fun launch(block: suspend () -> Unit) {
    val backgroundJob = Job()
    CoroutineScope(Dispatchers.IO + backgroundJob).launch {
        block.invoke()
        cancel()
    }
}
