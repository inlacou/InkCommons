package com.inlacou.inkbetterandroidviews.listenerstoobservable

import android.view.MotionEvent
import android.view.View

import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe

class OnLongTouchIncrementFiringSpeedObs constructor(private val view: View, breakpoints: List<Pair<Int, Int>>? = null) : ObservableOnSubscribe<Long> {
	
	private var breakpointsFinal = breakpoints ?: listOf(Pair(1200, 400), Pair(3000, 200), Pair(6000, 100), Pair(12000, 50), Pair(18000, 25))
	
	private var subscriber: ObservableEmitter<Long>? = null
	
	private var downTimeStamp = 0L
	private var currentIndex = 0
	
	var thread = newIntervalThread()
	
	private fun newIntervalThread() = IntervalThread(breakpointsFinal[0].first.toLong()) { thread, counter, elapsedTime ->
		subscriber.let {
			if(it!=null){
				it.onNext(counter)
				if(breakpointsFinal.size>currentIndex+1 && elapsedTime>=breakpointsFinal[currentIndex+1].first) {
					currentIndex += 1
					thread.periodicity = breakpointsFinal[currentIndex].second.toLong()
				}
				true
			}else{
				false
			}
		}
	}
	
	@Throws(Exception::class)
	override fun subscribe(subscriber: ObservableEmitter<Long>) {
		this.subscriber = subscriber
		view.setOnTouchListener { v, event ->
			when (event.actionMasked) {
				MotionEvent.ACTION_DOWN -> {
					downTimeStamp = System.currentTimeMillis()
					thread.stop()
					currentIndex = 0
					thread = newIntervalThread()
					thread.start()
					if(!view.isPressed) view.isPressed = true
					true //hold the event
				}
				MotionEvent.ACTION_UP -> {
					thread.stop()
					currentIndex = 0
					view.isPressed = false
					false //release the event
				}
				MotionEvent.ACTION_CANCEL -> {
					false
				}
				else -> {
					false
				} /*release the event*/
			}
		}
		
		subscriber.setCancellable { view.setOnTouchListener(null) }
	}
}

class IntervalThread(startingPeriodicity: Long, val callback: (thread: IntervalThread, counter: Long, elapsedTime: Long) -> Boolean) {
	
	private var counter = 1L
	private var accumulator = 0L
	var periodicity = startingPeriodicity
	
	private var running = false
	private var thread: Thread = Thread {
		while(running) {
			accumulator += periodicity
			if(callback.invoke(this, counter, accumulator)) counter++
			else running = false
			Thread.sleep(periodicity)
		}
	}

	fun start() {
		running = true
		thread.start()
	}
	
	fun stop() {
		running = false
	}
	
}