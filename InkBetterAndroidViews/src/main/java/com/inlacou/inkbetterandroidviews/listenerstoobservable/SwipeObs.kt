package com.inlacou.inkbetterandroidviews.listenerstoobservable

import android.view.MotionEvent
import android.view.View
import androidx.core.view.MotionEventCompat
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe

class SwipeObs constructor(private val betterSpinner: View) : ObservableOnSubscribe<SwipeObs.Action> {
	
	private var mStartDragX = 0F
	
	@Throws(Exception::class)
	override fun subscribe(subscriber: ObservableEmitter<Action>) {
		betterSpinner.setOnTouchListener { v, event ->
			when (event.action and MotionEventCompat.ACTION_MASK) {
				MotionEvent.ACTION_DOWN -> { mStartDragX = event.x; false }
				MotionEvent.ACTION_UP -> {
					if(mStartDragX-event.x>100) {
						if (event.x > mStartDragX) {
							subscriber.onNext(Action.SWIPE_IN)
						} else {
							subscriber.onNext(Action.SWIPE_OUT)
						}
						true
					}else false
				}
				else -> false
			}
		}

		subscriber.setCancellable { betterSpinner.setOnTouchListener(null) }
	}
	
	enum class Action {
		SWIPE_OUT, SWIPE_IN
	}
}