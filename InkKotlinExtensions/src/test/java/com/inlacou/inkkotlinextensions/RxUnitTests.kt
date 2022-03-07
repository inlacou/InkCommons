package com.inlacou.inkkotlinextensions

import com.inlacou.inkkotlincommons.rx.observables.ReInterval
import io.reactivex.rxjava3.core.Observable
import org.junit.Test
import org.junit.jupiter.api.Assertions
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RxUnitTests {

	@Test
	fun `basic - 5 elements`() {
		Observable.fromArray("A", "B", "C", "D", "E")
			.test().apply {
				assertComplete()
				assertNoErrors()
				assertValueCount(5)
			}
	}

	@Test
	fun `basic - endless`() {
		Observable.interval(25, TimeUnit.MILLISECONDS)
			.test().apply {
				awaitCount(10) /* Wait until there are 10 elements */
				assertValueCount(10)
				assertNoErrors()
			}
	}

	@Test
	fun `basic - endless - complete`() {
		Observable.interval(25, TimeUnit.MILLISECONDS)
			.doOnNext { println(it) }
			.takeUntil { it==10L /* 0 to 10, 11 elements */ }
			.test().apply {
				await(10, TimeUnit.SECONDS) //Max time it will wait
				assertNoErrors()
				assertValueCount(11) /* 0 to 10, 11 elements */
			}
	}

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
