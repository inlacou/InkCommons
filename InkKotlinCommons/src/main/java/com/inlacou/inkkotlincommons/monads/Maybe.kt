package com.inlacou.inkkotlincommons

import io.reactivex.rxjava3.core.Observable

data class Maybe<T>(val value: T?) {
	fun <R> map(func: (T) -> R): Maybe<R> = if(value==null) Maybe(null) else Maybe(func(this.value))
	fun <R> flatMap(func: (T) -> Maybe<R>): Maybe<R> = if(value==null) Maybe(null) else func(this.value)
}

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

fun <T, R> T?.maybeMap(callback: (T) -> R?): R? = if(this==null) null else callback(this)

fun main() {
	println("------------------------------ MAYBE MONAD")
	/* MAYBE MONAD */
	maybes("Joe")
	println("---------------")
	maybes("Frank")
	println("---------------")
	maybes("Laura")

	println("------------------------------ Any? as Maybe")
	/* MAYBE MONAD */
	nullables("Joe")
	println("---------------")
	nullables("Frank")
	println("---------------")
	nullables("Laura")

	println("------------------------------ LET")
	/* LET */
	lets("Joe")
	println("---------------")
	lets("Frank")
	println("---------------")
	lets("Laura")

	println("------------------------------ IF-ELSEs")
	/* IF-ELSEs */
	ifElses("Joe")
	println("---------------")
	ifElses("Frank")
	println("---------------")
	ifElses("Laura")

	println("------------------------------ ASYNC: OBSERVABLES")
	/* ASYNC: OBSERVABLES */
	Observable.concat(listOf(
		async_observables("Joe"),
		Observable.just("---------------"),
		async_observables("Frank"),
		Observable.just("---------------"),
		async_observables("Laura"),
	)).subscribe({ println(it) }, { println(it) })
}

/** Obviously, could return a Maybe<Country> */
private fun maybes(name: String): Country?
  = getUserM(name)
	.flatMap { getFamilyM(it.familySurname) }.also { println("$name's family is ${it.value}") }
	.flatMap { getHouseM(it.houseDirection) }.also { println("$name's house is ${it.value}") }
	.flatMap { getCityM(it.cityName) }.also { println("$name's city is ${it.value}") }
	.flatMap { getCountryM(it.countryName) }.also { println("$name's country is ${it.value}") }.value

private fun nullables(name: String): Country?
  = getUser(name)
	.maybeMap { getFamily(it.familySurname) }.also { println("$name's family is $it") }
	.maybeMap { getHouse(it.houseDirection) }.also { println("$name's house is $it") }
	.maybeMap { getCity(it.cityName) }.also { println("$name's city is $it") }
	.maybeMap { getCountry(it.countryName) }.also { println("$name's country is $it") }

private fun lets(name: String): Country? {
	getUser(name)?.let {
		getFamily(it.familySurname)?.also { println("$name's family is $it") }?.let {
			getHouse(it.houseDirection)?.also { println("$name's house is $it") }?.let {
				getCity(it.cityName)?.also { println("$name's city is $it") }?.let {
					return getCountry(it.countryName)?.also { println("$name's country is $it") }
				}
			}
		}
	}
	return null
}

private fun ifElses(name: String): Country? {
	val user = getUser(name)
	println("$name's user is $user")
	if(user!=null) {
		val family = getFamily(user.name)
		println("$name's family is $family")
		if(family!=null) {
			val house = getHouse(family.houseDirection)
			println("$name's house is $house")
			if(house!=null) {
				val city = getCity(house.cityName)
				println("$name's city is $city")
				if(city!=null) {
					val country = getCountry(city.countryName)
					println("$name's country is $country")
					return country
				}
			}
		}
	}
	return null
}

private fun async_observables(name: String): Observable<Maybe<Country>>
	= getUserMObs(name)
		.flatMapMaybeMonad { println("$name's family is ${it.familySurname}"); getFamilyMObs(it.familySurname) }
		.flatMapMaybeMonad { println("$name's house is ${it.houseDirection}"); getHouseMObs(it.houseDirection) }
		.flatMapMaybeMonad { println("$name's city is ${it.cityName}"); getCityMObs(it.cityName) }
		.flatMapMaybeMonad { println("$name's country is ${it.countryName}"); getCountryMObs(it.countryName) }

private val countries = listOf(Country("United States"))
private val cities = listOf(City("Washington DC", "United States"))
private val houses = listOf(House("White House", "Washington DC"))
private val families = listOf(Family("Biden", "White House"), Family("Jones", "False Street 123"))
private val persons = listOf(Person("Joe", "Biden"), Person("Laura", "Jones"))

private fun getUserMObs(name: String): Observable<Maybe<Person>> = Observable.just(Maybe(persons.find { it.name==name }))
private fun getFamilyMObs(surname: String): Observable<Maybe<Family>> = Observable.just(Maybe(families.find { it.surname==surname }))
private fun getHouseMObs(direction: String): Observable<Maybe<House>> = Observable.just(Maybe(houses.find { it.direction==direction }))
private fun getCityMObs(name: String): Observable<Maybe<City>> = Observable.just(Maybe(cities.find { it.name==name }))
private fun getCountryMObs(name: String): Observable<Maybe<Country>> = Observable.just(Maybe(countries.find { it.name==name }))

private fun getUserMAsync(name: String, result: (Maybe<Person>) -> Unit): Unit = result.invoke(Maybe(persons.find { it.name==name }))
private fun getFamilyMAsync(surname: String, result: (Maybe<Family>) -> Unit): Unit = result.invoke(Maybe(families.find { it.surname==surname }))
private fun getHouseMAsync(direction: String, result: (Maybe<House>) -> Unit): Unit = result.invoke(Maybe(houses.find { it.direction==direction }))
private fun getCityMAsync(name: String, result: (Maybe<City>) -> Unit): Unit = result.invoke(Maybe(cities.find { it.name==name }))
private fun getCountryMAsync(name: String, result: (Maybe<Country>) -> Unit): Unit = result.invoke(Maybe(countries.find { it.name==name }))

private fun getUserM(name: String): Maybe<Person> = Maybe(persons.find { it.name==name })
private fun getFamilyM(surname: String): Maybe<Family> = Maybe(families.find { it.surname==surname })
private fun getHouseM(direction: String): Maybe<House> = Maybe(houses.find { it.direction==direction })
private fun getCityM(name: String): Maybe<City> = Maybe(cities.find { it.name==name })
private fun getCountryM(name: String): Maybe<Country> = Maybe(countries.find { it.name==name })

private fun getUserAsync(name: String, result: (Person?) -> Unit): Unit = result.invoke(persons.find { it.name==name })
private fun getFamilyAsync(surname: String, result: (Family?) -> Unit): Unit = result.invoke(families.find { it.surname==surname })
private fun getHouseAsync(direction: String, result: (House?) -> Unit): Unit = result.invoke(houses.find { it.direction==direction })
private fun getCityAsync(name: String, result: (City?) -> Unit): Unit = result.invoke(cities.find { it.name==name })
private fun getCountryAsync(name: String, result: (Country?) -> Unit): Unit = result.invoke(countries.find { it.name==name })

private fun getUser(name: String): Person? = persons.find { it.name==name }
private fun getFamily(surname: String): Family? = families.find { it.surname==surname }
private fun getHouse(direction: String): House? = houses.find { it.direction==direction }
private fun getCity(name: String): City? = cities.find { it.name==name }
private fun getCountry(name: String): Country? = countries.find { it.name==name }

private data class Country(val name: String)
private data class City(val name: String, val countryName: String)
private data class House(val direction: String, val cityName: String)
private data class Family(val surname: String, val houseDirection: String)
private data class Person(val name: String, val familySurname: String)
