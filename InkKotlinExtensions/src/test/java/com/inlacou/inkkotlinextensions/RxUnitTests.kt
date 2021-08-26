package com.inlacou.inkkotlinextensions

import io.reactivex.rxjava3.core.Observable
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RxUnitTests {

	@Test
	fun `basic - 5 elements`() {
		val observable = Observable.fromArray("A", "B", "C", "D", "E")

		observable.test().apply {
			assertComplete()
			assertNoErrors()
			assertValueCount(5)
			//wtf assertNever("1")
		}
	}
	
	@Test
	fun `basic - endless`() {
		val observable = Observable.interval(25, TimeUnit.MILLISECONDS)

		observable.test().apply {
			awaitCount(10) /* Wait until there are 10 elements */
			assertValueCount(10)
			assertNoErrors()
		}
	}
	
	@Test
	fun `basic - endless - complete`() {
		val observable = Observable.interval(25, TimeUnit.MILLISECONDS)

		observable.takeUntil { it==10L /* 0 to 10, 11 elements */ }.test().apply {
			await(10, TimeUnit.SECONDS) //Max time it will wait
			assertNoErrors()
			assertValueCount(11) /* 0 to 10, 11 elements */
		}
	}
}
