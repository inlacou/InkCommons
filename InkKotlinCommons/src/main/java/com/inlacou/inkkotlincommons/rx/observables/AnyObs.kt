package com.inlacou.inkkotlincommons.rx.observables

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleEmitter
import io.reactivex.rxjava3.core.SingleOnSubscribe

class AnyObs <T> private constructor(private val work: () -> T) : SingleOnSubscribe<T> {
  
  override fun subscribe(emitter: SingleEmitter<T>) {
    emitter.onSuccess(work.invoke())
  }
  
  companion object {
    fun <T> create(work: () -> T): Single<T> {
      return Single.create(AnyObs(work))
    }
  }
}