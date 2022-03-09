package com.inlacou.inkkotlinextensions.rx

import com.inlacou.inkkotlincommons.rx.observables.ReInterval
import io.reactivex.rxjava3.core.Observable
import org.junit.Test
import org.junit.jupiter.api.Assertions
import java.util.concurrent.TimeUnit

class DoWhileUnitTests {

	@Test
	fun `doWhile with interval`() {
		val exceptedResults = listOf(
			"0p", "1p", "2p", "3p", "4p", "5p", "6p", "7p", "8p", "9p",
			"10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
			"20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
		)
		val results = mutableListOf<String>()
		Observable.interval(100, TimeUnit.MILLISECONDS)
			.doWhile({ it<10 }, { results.add("${it}p") }, consume = true)
			.doOnNext { results.add(it.toString()) }
			.test().apply {
				awaitCount(20) /* 0 to 29, first 0 to 9 are consumed by doWhile() */
				assertValueCount(20)
				assertNoErrors()
				Assertions.assertEquals(exceptedResults, results)
				println(results)
			}
	}

	@Test
	fun `doWhile with reinterval`() {
		//TODO se salta un elemento :(
		//El result es
		/*
			"0p", "1p", "2p", "3p", "4p", "5p", "6p", "7p", "8p", "9p",
			      "11", "12", "13", "14", "15", "16", "17", "18", "19",
			"20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
		 */
		val exceptedResults = listOf(
			"0p", "1p", "2p", "3p", "4p", "5p", "6p", "7p", "8p", "9p",
			"10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
			"20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
		)
		val results = mutableListOf<String>()
		val reInterval = ReInterval()
		val obs = reInterval.get()
			.doWhile({ it<10 }, { results.add("${it}p") }, consume = true)
			.doOnNext { results.add(it.toString()) }
			.test()
		reInterval.changePeriodicity(100)
		obs.apply {
			awaitCount(20) /* 0 to 29, first 0 to 9 are consumed by doWhile() */
			assertNoErrors()
			assertNotComplete()
			Assertions.assertEquals(exceptedResults, results)
			println(results)
		}
	}

	/*@Test
	fun `doWhile with takeWhile`() {
		val exceptedResults = listOf(
			"0p", "1p", "2p", "3p", "4p", "5p", "6p", "7p", "8p", "9p",
			"10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
			"20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
		)
		val results = mutableListOf<String>()
		val reInterval = ReInterval()
		val obs = reInterval.get()
			.doWhile2({ it<10 }, { results.add("${it}p") })
			.doOnNext { results.add(it.toString()) }
			.test()
		reInterval.changePeriodicity(100)
		obs.apply {
			awaitCount(20) /* 0 to 29, first 0 to 9 are consumed by doWhile() */
			assertNoErrors()
			Assertions.assertEquals(exceptedResults, results)
			println(results)
		}
	}*/


}