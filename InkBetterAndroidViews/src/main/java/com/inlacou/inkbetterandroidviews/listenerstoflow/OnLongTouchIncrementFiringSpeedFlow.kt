package com.inlacou.inkbetterandroidviews.listenerstoflow

import android.view.MotionEvent
import android.view.View

import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class OnLongTouchIncrementFiringSpeedFlow constructor(private val view: View, breakpoints: List<Pair<Int, Int>>? = null) {
	
	private var breakpointsFinal = breakpoints ?: listOf(Pair(1200, 400), Pair(3000, 200), Pair(6000, 100), Pair(12000, 50), Pair(18000, 25))
	
	private var subscriber: ProducerScope<Long>? = null
	
	private var downTimeStamp = 0L
	private var currentIndex = 0
	
	var thread = newIntervalThread()
	
	private fun newIntervalThread() = IntervalThread(breakpointsFinal[0].first.toLong()) { thread, counter, elapsedTime ->
		subscriber.let {
			if(it!=null){
				it.trySend(counter)
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

	fun create() = callbackFlow<Long> {
		subscriber = this
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

		awaitClose { view.setOnTouchListener(null) }
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