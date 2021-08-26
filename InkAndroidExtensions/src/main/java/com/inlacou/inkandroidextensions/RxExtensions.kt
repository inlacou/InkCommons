package com.inlacou.inkandroidextensions

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

fun <T> Observable<T>.onUi(): Observable<T> = subscribeOn(AndroidSchedulers.mainThread())
fun <T> Single<T>.onUi(): Single<T> = subscribeOn(AndroidSchedulers.mainThread())
fun Completable.onUi(): Completable = subscribeOn(AndroidSchedulers.mainThread())
fun <T> Observable<T>.toUi(): Observable<T> = observeOn(AndroidSchedulers.mainThread())
fun <T> Single<T>.toUi(): Single<T> = observeOn(AndroidSchedulers.mainThread())
fun Completable.toUi(): Completable = observeOn(AndroidSchedulers.mainThread())
