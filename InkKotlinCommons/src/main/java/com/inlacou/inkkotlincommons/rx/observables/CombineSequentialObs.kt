package com.inlacou.inkkotlincommons.rx.observables

import com.inlacou.inkkotlincommons.rx.observables.CombineSequentialObs.ErrorBehaviour.*
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Tries to execute the given Observable list in the order given. Has three behaviours for when an error is detected.
 *
 * StopSuccessfullyOnError  - Stops with a success returning retrieved data
 * StopWronglyOnError       - Stops with an error returning error
 * AlwaysTryAll             - Execute all always, and return retrieved data with nulls instead when an error happened
 *
 */
class CombineSequentialObs <T: Any> internal constructor(list: List<Observable<T>>, val errorBehaviour: ErrorBehaviour, val alwaysTryAllOnError: ((Throwable) -> Unit)? = null) : ObservableOnSubscribe<List<T?>> {

    private var remaining: List<Observable<T>> = list
    private var results: MutableList<T?> = mutableListOf()
    private var currentDisposable: Disposable? = null

    enum class ErrorBehaviour {
        StopSuccessfullyOnError,
        StopWronglyOnError,
        AlwaysTryAll
    }

    override fun subscribe(emitter: ObservableEmitter<List<T?>>) {
        recursive(emitter)
        emitter.setCancellable { currentDisposable?.dispose() }
    }

    private fun recursive(emitter: ObservableEmitter<List<T?>>) {
        if(remaining.isEmpty()) {
            emitter.onNext(results)
            emitter.onComplete()
            return
        }
        val current = remaining.first()
        remaining = remaining.takeLast(remaining.size-1)
        currentDisposable = current.subscribe({
            results.add(it)
        },{
            when(errorBehaviour) {
                StopSuccessfullyOnError -> {
                    emitter.onNext(results)
                    emitter.onComplete()
                }
                StopWronglyOnError -> {
                    emitter.onError(it)
                    emitter.onComplete()
                }
                AlwaysTryAll ->  {
                    results.add(null)
                    alwaysTryAllOnError?.invoke(it)
                    recursive(emitter)
                }
            }
        }, {
            recursive(emitter)
        })
    }

    companion object {
        fun <T: Any> create(list: List<Observable<T>>, errorBehaviour: ErrorBehaviour = AlwaysTryAll): Observable<List<T?>> {
            return Observable.create(CombineSequentialObs(list, errorBehaviour))
        }
    }
}