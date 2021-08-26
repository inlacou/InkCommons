package com.inlacou.inkbetterandroidviews.listenerstoobservable

import android.view.MotionEvent
import android.view.View

import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe

class OnTouchObs constructor(private val view: View) : ObservableOnSubscribe<Triple<MotionEvent, Float, Float>> {

	var initialX = 0.0f
	var initialY = 0.0f
	
	/**
	 * @return Triple(motionEvent, xDiff, yDiff)
	 */
	@Throws(Exception::class)
	override fun subscribe(subscriber: ObservableEmitter<Triple<MotionEvent, Float, Float>>) {
		view.setOnTouchListener { v, event ->
			if(event.action==MotionEvent.ACTION_DOWN) {
				initialX = event.x
				initialY = event.y
			}
			val xDiff = event.x-initialX
			val yDiff = event.y-initialY
			subscriber.onNext(Triple(event, xDiff, yDiff))
			true
		}

		subscriber.setCancellable { view.setOnTouchListener(null) }
	}
}