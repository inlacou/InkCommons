package com.inlacou.inkbetterandroidviews.listenerstoobservable

import android.view.View

import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe

class OnLongClickObs constructor(private val view: View, val consumeEvent: Boolean) : ObservableOnSubscribe<View> {
	@Throws(Exception::class)
	override fun subscribe(subscriber: ObservableEmitter<View>) {

		view.setOnLongClickListener { subscriber.onNext(view); consumeEvent }

		subscriber.setCancellable { view.setOnClickListener(null) }
	}
}