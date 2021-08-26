package com.inlacou.inkbetterandroidviews.listenerstoobservable

import android.view.View

import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe

class OnClickObs constructor(private val view: View) : ObservableOnSubscribe<View> {
	@Throws(Exception::class)
	override fun subscribe(subscriber: ObservableEmitter<View>) {

		view.setOnClickListener { subscriber.onNext(view) }

		subscriber.setCancellable { view.setOnClickListener(null) }
	}
}