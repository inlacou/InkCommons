package com.inlacou.inkkotlinextensions.rx

import io.reactivex.rxjava3.annotations.CheckReturnValue
import io.reactivex.rxjava3.annotations.SchedulerSupport
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/* Utils */
inline fun <reified R> Flowable<*>.filterIsInstance(): Flowable<R> = filter { it is R }.map { it as R }
inline fun <reified R> Maybe<*>.filterIsInstance(): Maybe<R> = filter { it is R }.map { it as R }
inline fun <reified R> Observable<*>.filterIsInstance(): Observable<R> = filter { it is R }.map { it as R }
inline fun <reified R> Single<*>.filterIsInstance(): Maybe<R> = filter { it is R }.map { it as R }

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
/**
 * Gives pairs of items
 */
fun <T> Observable<T>.withPrevious(): Observable<Pair<T, T>> = buffer(2, 1).map { Pair(it[0], it[1]) }
fun <T> Observable<T>.takeUntil(time: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS): Observable<MutableList<T>> = buffer(time, timeUnit).takeUntil { true }

fun <T> Observable<T>.onErrorDoAndResumeNext(returnItem: T, action: ((Throwable) -> Unit)?) {
	onErrorResumeNext {
		action?.invoke(it)
		ObservableSource { it.onNext(returnItem) }
	}
}

fun <T> Observable<T>.onErrorPrintAndResumeNext(returnItem: T, action: ((Throwable) -> Unit)?) {
	onErrorDoAndResumeNext(returnItem) {
		it.printStackTrace()
		action?.invoke(it)
	}
}

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

/**
 * do @param work while @param whileCondition and then continues normal.
 * For example we can handle all even ints in a sequence until an odd one appears, and further even items will not be handled:
 *      2,4,6,8,10,11,12,13,14,15,16
 *      would take only for the @param work
 *      2,4,6,8,10, and not 12, 14, or 16
 * @param consume determines if the elements captured with the doWhile will also continue to be used later. USE true WITH CAUTION.
 */
fun <T> Observable<T>.doWhile(whileCondition: (T) -> Boolean, work: (T) -> Unit, consume: Boolean = true): Observable<T> {
	return takeUntil { !whileCondition.invoke(it) }
		.skipLast(1)
		.doOnNext { work.invoke(it) }
		.skipWhile { if(consume) whileCondition.invoke(it) else false }
		.concatWith(skipWhile { whileCondition.invoke(it) })
}

fun <T> Observable<T>.doWhile2(whileCondition: (T) -> Boolean, work: (T) -> Unit): Observable<T> {
	return takeWhile(whileCondition)
		.doOnNext(work)
		.skipWhile(whileCondition)
		.concatWith(skipWhile(whileCondition))
}

fun <T> Observable<T>.doOnFirst(onFirstAction: (T) -> Unit): Observable<T> =
	take(1)
		.doOnNext { onFirstAction.invoke(it) }
		.concatWith(skip(1))

fun <T> Flowable<T>.doOnFirst(onFirstAction: (T) -> Unit): Flowable<T> =
	take(1)
		.doOnNext { onFirstAction.invoke(it) }
		.concatWith(skip(1))

fun <T> Observable<T>.doAfterFirst(afterFirstAction: (T) -> Unit): Observable<T> =
	take(1)
		.doAfterNext { afterFirstAction.invoke(it) }
		.concatWith(skip(1))

fun <T> Flowable<T>.doAfterFirst(afterFirstAction: (T) -> Unit): Flowable<T> =
	take(1)
		.doAfterNext { afterFirstAction.invoke(it) }
		.concatWith(skip(1))
