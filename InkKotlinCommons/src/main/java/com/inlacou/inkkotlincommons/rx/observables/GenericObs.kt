package com.inlacou.inkkotlincommons.rx.observables

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleEmitter
import io.reactivex.rxjava3.core.SingleOnSubscribe

class GenericObs<T> internal constructor(val action: () -> T) : SingleOnSubscribe<T> {
  
  override fun subscribe(subscriber: SingleEmitter<T>) {
    subscriber.onSuccess(action.invoke())
  }

  companion object {
    fun <T> create(action: () -> T): Single<T> {
      return Single.create(GenericObs(action))
    }
  }
}