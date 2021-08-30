package com.inlacou.inkkotlinextensions.rx

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Function
import java.util.concurrent.TimeUnit

/**
 * Example: Observable.just(1).retryWhen(RetryWithDelay(3, 1000)).subscribe()
 */
class RetryWithDelay(private val maxRetries: Int, private val retryDelayMillis: Int) : Function<Observable<out Throwable>, Observable<*>> {
	private var retryCount: Int = 0

	override fun apply(attempts: Observable<out Throwable>): Observable<*> {
		return attempts
				.flatMap { throwable ->
					if (++retryCount <= maxRetries) {
						// When this Observable calls onNext, the original
						// Observable will be retried (i.e. re-subscribed).
						Observable.timer(retryDelayMillis.toLong(), TimeUnit.MILLISECONDS)
					} else { // Max retries hit. Just pass the error along.
						Observable.error(throwable)
					}
				}
	}
}