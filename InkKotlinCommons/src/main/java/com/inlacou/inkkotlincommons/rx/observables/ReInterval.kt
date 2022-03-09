package com.inlacou.inkkotlincommons.rx.observables

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * Works somewhat like Observable.interval(time, timeUnit) but allows you to change the interval when you want.
 * It must be a class because it needs to have properties and methods.
 */
class ReInterval {

	private val newInterval: PublishSubject<Long> = PublishSubject.create()
	private var disp: Disposable? = null
	private var index = 0L

	/**
	 * Must call changePeriodicity with initial periodicity after subscription is made!
	 */
	// When you send a new value to the subject, it will be switchMapped to a new interval obs
	fun get(): Observable<Long> = newInterval

	fun changePeriodicity(newPeriodicity: Long){
		disp?.dispose()
		disp = Observable.interval(newPeriodicity, TimeUnit.MILLISECONDS).map { index++ }.subscribe({ newInterval.onNext(it) },{ newInterval.onError(it) })
	}
}