package com.inlacou.inkkotlinextensions.rx

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
	fun `doOnFirst 1,2,3 is 1F,1,2,3`() {
		val exceptedResults = listOf("1F", "1", "2", "3")
		val results = mutableListOf<String>()
		Observable.fromArray(1, 2, 3)
			.doOnFirst { Assertions.assertEquals(1, it); results.add("${it}F") }
			.doOnNext { results.add(it.toString()) }
			.test().apply {
				await(10, TimeUnit.SECONDS) //Max time it will wait
				assertNoErrors()
				assertValueCount(3) /* 3 elements, doOnFirst kind of duplicates the first. Makes the first element work on two callbacks, onNext and doOnFirst. */
				Assertions.assertEquals(exceptedResults, results)
			}
	}

	@Test
	fun `doAfterFirst 1,2,3 is 1,1F,2,3`() {
		val exceptedOnNextCalls = listOf("1", "1 doAfterFirst", "2", "3")
		val onNextCalls = mutableListOf<String>()
		Observable.fromArray(1, 2, 3)
			.doAfterFirst { Assertions.assertEquals(1, it); onNextCalls.add("$it doAfterFirst") }
			.doOnNext { onNextCalls.add(it.toString()) }
			.test().apply {
				await(10, TimeUnit.SECONDS) //Max time it will wait
				assertNoErrors()
				assertValueCount(3) /* 3 elements, doAfterFirst kind of duplicates the first. Makes the first element work on two callbacks, onNext and doAfterFirst. */
				Assertions.assertEquals(exceptedOnNextCalls, onNextCalls)
			}
	}

	@Test
	fun `doWhile fromArray 1,2,3 is 1,1F,2,3 consuming`() {
		val exceptedResults = listOf("2dw", "4dw", "6dw", "8dw", "10dw", "11", "12", "13", "14", "15", "16", "17")
		val results = mutableListOf<String>()
		Observable.fromArray(2, 4, 6, 8, 10, 11, 12, 13, 14, 15, 16, 17)
			.doWhile({ it%2==0 /*Pair items*/ }, { results.add("${it}dw") }, consume = true)
			.doOnNext { results.add(it.toString()) }
			.test().apply {
				await(10, TimeUnit.SECONDS) //Max time it will wait
				assertNoErrors()
				Assertions.assertEquals(exceptedResults, results)
			}
	}

	@Test
	fun `doWhile fromArray 1,2,3 is 1,1F,2,3 not consuming`() {
		val exceptedResults = listOf("2dw", "2", "4dw", "4", "6dw", "6", "8dw", "8", "10dw", "10", "11", "12", "13", "14", "15", "16", "17")
		val results = mutableListOf<String>()
		Observable.fromArray(2, 4, 6, 8, 10, 11, 12, 13, 14, 15, 16, 17)
			.doWhile({ it%2==0 /*Even items*/ }, { results.add("${it}dw") }, consume = false)
			.doOnNext { results.add(it.toString()) }
			.test().apply {
				await(10, TimeUnit.SECONDS) //Max time it will wait
				assertNoErrors()
				Assertions.assertEquals(exceptedResults, results)
			}
	}

}
