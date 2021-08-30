package com.inlacou.inkkotlincommons

import com.inlacou.inkkotlincommons.rx.observables.AnyObs
import com.inlacou.inkkotlincommons.rx.observables.CombineSequentialObs
import com.inlacou.inkkotlincommons.rx.observables.CombineSequentialSingles
import io.reactivex.rxjava3.core.Observable
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

class RxUnitTests {
	
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
	fun combineSequentialObs() {
		CombineSequentialObs.create(listOf(
			AnyObs.create { "Hola" }.toObservable(),
			Observable.timer(1, TimeUnit.SECONDS).map { "Que tal" },
			Observable.timer(1, TimeUnit.SECONDS).map { "Cómo estás" },
			AnyObs.create { "Adios" }.toObservable(),
		), CombineSequentialObs.ErrorBehaviour.StopSuccessfullyOnError).test().apply {
			await(10, TimeUnit.SECONDS)
			assertValue(listOf("Hola", "Que tal", "Cómo estás", "Adios"))
			assertNoErrors()
			assertValueCount(1)
		}
	}
	
	@Test
	fun combineSequentialSingles() {
		CombineSequentialSingles.create(listOf(
			AnyObs.create { "Hola" },
			Observable.timer(1, TimeUnit.SECONDS).map { "Que tal" }.single(""),
			Observable.timer(1, TimeUnit.SECONDS).map { "Cómo estás" }.single(""),
			AnyObs.create { "Adios" },
		), CombineSequentialSingles.ErrorBehaviour.StopSuccessfullyOnError).test().apply {
			await(10, TimeUnit.SECONDS)
			assertValue(listOf("Hola", "Que tal", "Cómo estás", "Adios"))
			assertNoErrors()
			assertValueCount(1)
		}
	}
	
}
