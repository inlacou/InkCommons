package com.inlacou.inkkotlinextensions.rx

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.annotations.CheckReturnValue
import io.reactivex.rxjava3.annotations.SchedulerSupport
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/* Utils */
/**
 * Applies [transform] to values from the [Observable] and forwards values with non-null results.
 */
inline fun <T : Any, S : Any> Observable<T>.mapNotNull(
		crossinline transform: (T) -> S?
): Observable<S> {
	return this
			.flatMap {
				val result = transform(it)
				if (result == null) {
					Observable.empty()
				} else {
					Observable.just(result)
				}
			}
}

/**
 * Used to avoid accidental multiple clicks
 */
fun <T> Observable<T>.filterRapidClicks(windowDuration: Long = 1000, timeUnit: TimeUnit = TimeUnit.MILLISECONDS): Observable<T> = throttleFirst(windowDuration, timeUnit)
fun <T> Observable<T>.debounce(time: Long): Observable<T> = debounce(time, TimeUnit.MILLISECONDS)
fun <T> Observable<T>.withPrevious(): Observable<Pair<T, T>> = buffer(2, 1).map { Pair(it[0], it[1]) }

/**
 * Sugar
 */
fun <T> Observable<T>.onNewThread(): Observable<T> = subscribeOn(Schedulers.newThread())
/**
 * Sugar
 */
fun <T> Single<T>.onNewThread(): Single<T> = subscribeOn(Schedulers.newThread())
/**
 * Sugar
 */
fun Completable.onNewThread(): Completable = subscribeOn(Schedulers.newThread())
/**
 * Sugar
 */
fun <T> Observable<T>.toNewThread(): Observable<T> = observeOn(Schedulers.newThread())
/**
 * Sugar
 */
fun <T> Single<T>.toNewThread(): Single<T> = observeOn(Schedulers.newThread())
/**
 * Sugar
 */
fun Completable.toNewThread(): Completable = observeOn(Schedulers.newThread())

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <T> Observable<T>.retryWithDelay(maxRetries: Int = 3, retryDelayMillis: Int = 1000): Observable<T>{
	return retryWhen(RetryWithDelay(maxRetries, retryDelayMillis))
}

inline fun <T> Observable<T>.tap(crossinline function: (T) -> Unit): Observable<T> {
	return this.map {
		function.invoke(it)
		it
	}
}

/**
 * Buffer emits every X second, independently of having received anything new.
 * Debounce starts after receiving the first element, and waits until the passed delay without new elements to call onNext, returns last item
 * debouncedBuffer starts after receiving the first element, and waits until the passed delay without new elements to call onNext, returns all items on last period
 *
 * The next code would never call onNext()
 *     Observable
	    .interval(100, TimeUnit.MILLISECONDS)
	    .debouncedBuffer(1000, TimeUnit.MILLISECONDS)
 * Because it is constantly generating new items that keep the debouncedBuffer from giving a result.
 *
 * Idea from here: https://blog.kaush.co/2015/01/05/debouncedbuffer-with-rxjava/ (tried to just copy it, but did not work as I expected ¯\_(ツ)_/¯)
 */
fun <T> Observable<T>.debouncedBuffer(delay: Long, unit: TimeUnit): Observable<MutableList<T>> {
	return buffer(debounce(delay, unit))
}
/* /Utils */

