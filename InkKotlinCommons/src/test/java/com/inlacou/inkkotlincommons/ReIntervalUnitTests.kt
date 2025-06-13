package com.inlacou.inkkotlincommons

import com.inlacou.inkkotlincommons.rx.observables.ReInterval
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

class ReIntervalUnitTests {

	@Test
	fun `reinterval - sequence`() {
		val reInterval = ReInterval()
		var step = 1
		var acc = step
		var revert = false
		val intervalTimes = listOf(2L, 4L, 6L, 8L, 10L, 8L, 6L, 4L, 2L)
		val multiplier = 100L
		var timeStamp = System.currentTimeMillis()

		val obs = reInterval.get()
			.map {
				val perceivedInterval = (System.currentTimeMillis()-timeStamp)/multiplier
				timeStamp = System.currentTimeMillis()
				if(revert) acc-- else acc++
				Assertions.assertEquals(intervalTimes[step-1], perceivedInterval)
				step++
				val newIntervalTime = acc*2L*multiplier
				if(step%5==0) revert = !revert
				println("reinterval | step: $step (${perceivedInterval} units) | newInterval: ${newIntervalTime}ms")
				newIntervalTime
			}
			.doOnNext { reInterval.changePeriodicity(it) }
			.take(intervalTimes.size.toLong())
			.test()
		reInterval.changePeriodicity(2*multiplier)
		obs.apply {
			awaitCount(intervalTimes.size) /*Await by item number*/
			await(intervalTimes.map { it*multiplier }.reduce { acc, l -> acc+l }, TimeUnit.SECONDS) /*Or await by time*/
			assertValueCount(intervalTimes.size)
			println("values: ${values()}")
			repeat(intervalTimes.size-1) { assertValueAt(it, intervalTimes[it+1]*multiplier) /*On each step we map to the next value on our interval list*/ }
			assertNoErrors() /*Assert that we have ended normally, and not because of any error*/
		}
	}
}