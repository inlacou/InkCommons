package com.inlacou.inkkotlincommons.rx.observables

import com.inlacou.inkkotlincommons.rx.observables.CombineSequentialSingles.ErrorBehaviour.*
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Tries to execute the given Single list in the order given. Has three behaviours for when an error is detected.
 *
 * StopSuccessfullyOnError  - Stops with a success returning retrieved data
 * StopWronglyOnError       - Stops with an error returning error
 * AlwaysTryAll             - Execute all always, and return retrieved data with nulls instead when an error happened
 *
 */
class CombineSequentialSingles <T> internal constructor(list: List<Single<T>>, val errorBehaviour: ErrorBehaviour, val alwaysTryAllOnError: ((Throwable) -> Unit)? = null) : SingleOnSubscribe<List<T?>> {

    private var remaining: List<Single<T>> = list
    private var results: MutableList<T?> = mutableListOf()
    private var currentDisposable: Disposable? = null

    enum class ErrorBehaviour {
        StopSuccessfullyOnError,
        StopWronglyOnError,
        AlwaysTryAll
    }

    override fun subscribe(emitter: SingleEmitter<List<T?>>) {
        recursive(emitter)
        emitter.setCancellable { currentDisposable?.dispose() }
    }

    private fun recursive(emitter: SingleEmitter<List<T?>>) {
        if(remaining.isEmpty()) {
            emitter.onSuccess(results)
            return
        }
        val current = remaining.first()
        remaining = remaining.takeLast(remaining.size-1)
        currentDisposable = current.subscribe({
            results.add(it)
            recursive(emitter)
        },{
            when(errorBehaviour) {
                StopSuccessfullyOnError -> emitter.onSuccess(results)
                StopWronglyOnError -> emitter.onError(it)
                AlwaysTryAll ->  {
                    results.add(null)
                    alwaysTryAllOnError?.invoke(it)
                    recursive(emitter)
                }
            }
        })
    }

    companion object {
        fun <T> create(list: List<Single<T>>, errorBehaviour: ErrorBehaviour = AlwaysTryAll): Single<List<T?>> {
            return Single.create(CombineSequentialSingles(list, errorBehaviour))
        }
    }
}