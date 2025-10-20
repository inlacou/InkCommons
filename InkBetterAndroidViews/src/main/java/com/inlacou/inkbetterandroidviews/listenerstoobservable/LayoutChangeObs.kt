package com.inlacou.inkbetterandroidviews.listenerstoobservable

import android.view.View
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe

class LayoutChangeObs constructor(private val view: View) : ObservableOnSubscribe<Triple<View, LayoutChangeObs.Dimensions, LayoutChangeObs.Dimensions>> {
	
	@Throws(Exception::class)
	override fun subscribe(subscriber: ObservableEmitter<Triple<View, Dimensions, Dimensions>>) {
		val listener = { view: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int ->
			subscriber.onNext(Triple(view, Dimensions(left, top, right, bottom), Dimensions(oldLeft, oldTop, oldRight, oldBottom)))
		}
		view.addOnLayoutChangeListener(listener)
		subscriber.setCancellable { view.removeOnLayoutChangeListener(listener) }
	}

	data class Dimensions(
			val left: Int,
			val top: Int,
			val right: Int,
			val bottom: Int)
}