package com.inlacou.inkkotlincommons.monads

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

data class Maybe<T>(val value: T?) {
	fun <R> map(func: (T) -> R): Maybe<R> = if(value==null) Maybe(null) else Maybe(func(this.value))
	fun <R> flatMap(func: (T) -> Maybe<R>): Maybe<R> = if(value==null) Maybe(null) else func(this.value)
}

fun <T, R> T?.maybeMap(func: (T) -> R?): R? = if(this==null) null else func(this)

/**
 * Allows beautifully non-nested sintax when working with Observable<Maybe<T>>.
 * Like this:
 * getUserMaybeObs(name) /*returns Observable<Maybe<Person>>*/
 *   .flatMapMaybeMonad { person -> getFamilyMaybeObs(person.familySurname) /*returns Observable<Maybe<Family>>*/ }
 *   .flatMapMaybeMonad { family -> getHouseMaybeObs(family.houseDirection) /*returns Observable<Maybe<House>>*/ }
 *   .flatMapMaybeMonad { house -> getCityMaybeObs(house.cityName) /*returns Observable<Maybe<City>>*/ }
 *   .flatMapMaybeMonad { city -> getCountryMaybeObs(city.countryName) /*returns Observable<Maybe<Country>>*/ }
 *
 *   if Maybe contains a null, ends with an Observable<Maybe<Null>>.
 *   if Maybe contains something, continues chain with observable provided on callback.
 */
fun <T, R> Observable<Maybe<T>>.flatMapMaybeMonad(callback: (T) -> Observable<Maybe<R>>): Observable<Maybe<R>>
  = flatMap { if(it.value==null) Observable.just(Maybe(null)) else callback(it.value) }

/**
 * Allows beautifully non-nested sintax when working with Observable<Maybe<T>>.
 * Like this:
 * getUserMaybeObs(name) /*returns Observable<Maybe<Person>>*/
 *   .flatMapMaybeMonad { person -> getFamilyMaybeObs(person.familySurname) /*returns Observable<Maybe<Family>>*/ }
 *   .flatMapMaybeMonad { family -> getHouseMaybeObs(family.houseDirection) /*returns Observable<Maybe<House>>*/ }
 *   .flatMapMaybeMonad { house -> getCityMaybeObs(house.cityName) /*returns Observable<Maybe<City>>*/ }
 *   .flatMapMaybeMonad { city -> getCountryMaybeObs(city.countryName) /*returns Observable<Maybe<Country>>*/ }
 *
 *   if Maybe contains a null, ends with an Observable<Maybe<Null>>.
 *   if Maybe contains something, continues chain with observable provided on callback.
 */
fun <T, R> Single<Maybe<T>>.flatMapMaybeMonad(callback: (T) -> Single<Maybe<R>>): Single<Maybe<R>>
  = flatMap { if(it.value==null) Single.just(Maybe(null)) else callback(it.value) }

/**
 * Allows beautifully non-nested sintax when working with Observable<Maybe<T>>.
 * Like this:
 * getUserMaybeObs(name) /*returns Observable<Maybe<Person>>*/
 *   .flatMapMaybeMonad { person -> getFamilyMaybeObs(person.familySurname) /*returns Observable<Maybe<Family>>*/ }
 *   .flatMapMaybeMonad { family -> getHouseMaybeObs(family.houseDirection) /*returns Observable<Maybe<House>>*/ }
 *   .flatMapMaybeMonad { house -> getCityMaybeObs(house.cityName) /*returns Observable<Maybe<City>>*/ }
 *   .flatMapMaybeMonad { city -> getCountryMaybeObs(city.countryName) /*returns Observable<Maybe<Country>>*/ }
 *
 *   if Maybe contains a null, ends with an Observable<Maybe<Null>>.
 *   if Maybe contains something, continues chain with observable provided on callback.
 */
fun <T, R> io.reactivex.rxjava3.core.Maybe<Maybe<T>>.flatMapMaybeMonad(callback: (T) -> io.reactivex.rxjava3.core.Maybe<Maybe<R>>): io.reactivex.rxjava3.core.Maybe<Maybe<R>>
  = flatMap { if(it.value==null) io.reactivex.rxjava3.core.Maybe.just(Maybe(null)) else callback(it.value) }
