package com.inlacou.inkandroidextensions

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

fun <T> Observable<T>.onUi(): Observable<T> = subscribeOn(AndroidSchedulers.mainThread())
fun <T> Single<T>.onUi(): Single<T> = subscribeOn(AndroidSchedulers.mainThread())
fun Completable.onUi(): Completable = subscribeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.toUi(): Observable<T> = observeOn(AndroidSchedulers.mainThread())
fun <T> Single<T>.toUi(): Single<T> = observeOn(AndroidSchedulers.mainThread())
fun Completable.toUi(): Completable = observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.onComputation(): Observable<T> = subscribeOn(Schedulers.computation())
fun <T> Single<T>.onComputation(): Single<T> = subscribeOn(Schedulers.computation())
fun Completable.onComputation(): Completable = subscribeOn(Schedulers.computation())

fun <T> Observable<T>.toComputation(): Observable<T> = observeOn(Schedulers.computation())
fun <T> Single<T>.toComputation(): Single<T> = observeOn(Schedulers.computation())
fun Completable.toComputation(): Completable = observeOn(Schedulers.computation())
