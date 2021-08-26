package com.inlacou.inkkotlinextensions.extensions

import com.inlacou.inkkotlincommons.rx.observables.AnyObs
import com.inlacou.inkkotlinextensions.rx.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.lang.Exception
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RxExtensionsUnitTests {
	
	@Test
	fun tap() {
		var index = 0
		
		Observable.fromArray("A", "B", "C", "D", "E").tap {
			when(index) {
				0 -> Assertions.assertEquals("A", it)
				1 -> Assertions.assertEquals("B", it)
				2 -> Assertions.assertEquals("C", it)
				3 -> Assertions.assertEquals("D", it)
				else -> Assertions.assertEquals("E", it)
			}
			index++
		}.tap { //Second time to ensure that the values are the same and in the same position.
			when(index) {
				0 -> Assertions.assertEquals("A", it)
				1 -> Assertions.assertEquals("B", it)
				2 -> Assertions.assertEquals("C", it)
				3 -> Assertions.assertEquals("D", it)
				else -> Assertions.assertEquals("E", it)
			}
			index++
		}.test()
	}
	
	@Test
	fun debounce() {
		val subject = PublishSubject.create<Long>()
		
		Observable.interval(125, TimeUnit.MILLISECONDS).tap { subject.onNext(it*125) }.subscribe()
		Observable.interval(150, TimeUnit.MILLISECONDS).tap { subject.onNext(it*150) }.subscribe()
		
		subject
			.debounce(105).tap { println(it) }.takeUntil { true /*Stop at first*/ }.test().apply {
				await(10, TimeUnit.SECONDS)
				assertValue(500L) /*Stops because of the gap between 500 and 600*/
				assertNoErrors()
				assertValueCount(1)
			}
	}
	
	@Test
	fun debouncedBuffer() {
		val subject = PublishSubject.create<Long>()
		
		Observable.interval(125, TimeUnit.MILLISECONDS).tap { subject.onNext(it*125) }.subscribe()
		Observable.interval(150, TimeUnit.MILLISECONDS).tap { subject.onNext(it*150) }.subscribe()
		
		subject
			.debouncedBuffer(105, TimeUnit.MILLISECONDS).tap { println(it) }.takeUntil { true /*Stop at first*/ }.test().apply {
				await(10, TimeUnit.SECONDS)
				assertValue(mutableListOf(0L, 0L, 125L, 150L, 250L, 300L, 375L, 450L, 500L)) /*Stops because of the gap between 500 and 600*/
				assertNoErrors()
				assertValueCount(1)
			}
	}
	
	@Test
	fun anyObs() {
		AnyObs.create { "Hola" }.toObservable().test().apply {
			await(10, TimeUnit.SECONDS)
			assertValue("Hola")
			assertNoErrors()
			assertValueCount(1)
		}
	}
	
	@Test
	fun mapNotNull() {
		Observable.fromArray(0, 1, 2, 3, 4).mapNotNull { if(it%2==0) null else it }.test().apply {
			await(10, TimeUnit.SECONDS)
			assertValues(1, 3)
			assertNoErrors()
			assertValueCount(2)
		}
		Observable.fromArray(0, 1, 2, 3, 4).mapNotNull { if(it%2==0) it else null }.test().apply {
			await(10, TimeUnit.SECONDS)
			assertValues(0, 2, 4)
			assertNoErrors()
			assertValueCount(3)
		}
	}
	
	@Test
	fun `retry once`() {
		var failed = false
		AnyObs.create {
			if(failed) "hola"
			else {
				failed = true
				throw Exception("will fail once")
			}
		}.toObservable().retry(1).test().apply {
			await(10, TimeUnit.SECONDS)
			assertValues("hola")
			assertValueCount(1)
		}
	}
	
	@Test
	fun `retry twice`() {
		var fails = 0
		AnyObs.create {
			if(fails==3) "hola"
			else {
				fails++
				throw Exception()
			}
		}.toObservable().retry(3).test().apply {
			await(10, TimeUnit.SECONDS)
			assertValues("hola")
			assertValueCount(1)
		}
	}
	
	@Test
	fun `retry with delay 5-250ms`() {
		var failTimeStamp: Long? = null
		AnyObs.create {
			if(System.currentTimeMillis()-(failTimeStamp ?: 0)>=1000) "hola"
			else {
				if(failTimeStamp==null) failTimeStamp = System.currentTimeMillis()
				throw Exception()
			}
		}.toObservable().retryWithDelay(5, 250).test().apply {
			await(10, TimeUnit.SECONDS)
			assertValues("hola")
			assertValueCount(1)
		}
	}
	
	@Test
	fun `retry with delay default`() {
		var failTimeStamp: Long? = null
		AnyObs.create {
			if(System.currentTimeMillis()-(failTimeStamp ?: 0)>=2000) "hola"
			else {
				if(failTimeStamp==null) failTimeStamp = System.currentTimeMillis()
				throw Exception()
			}
		}.toObservable().retryWithDelay().test().apply {
			await(10, TimeUnit.SECONDS)
			assertValues("hola")
			assertValueCount(1)
		}
	}
	
	@Test
	fun `retry with delay 2`() {
		var failTimeStamp: Long? = null
		AnyObs.create {
			if(System.currentTimeMillis()-(failTimeStamp ?: 0)>=1000) "hola"
			else {
				if(failTimeStamp==null) failTimeStamp = System.currentTimeMillis()
				throw Exception()
			}
		}.toObservable().retryWithDelay(2).test().apply {
			await(10, TimeUnit.SECONDS)
			assertValues("hola")
			assertValueCount(1)
		}
	}
	
	@Test
	fun `retry with delay 250ms`() {
		var failTimeStamp: Long? = null
		AnyObs.create {
			if(System.currentTimeMillis()-(failTimeStamp ?: 0)>=700) "hola"
			else {
				if(failTimeStamp==null) failTimeStamp = System.currentTimeMillis()
				throw Exception()
			}
		}.toObservable().retryWithDelay(retryDelayMillis = 250).test().apply {
			await(10, TimeUnit.SECONDS)
			assertValues("hola")
			assertValueCount(1)
		}
	}
}
