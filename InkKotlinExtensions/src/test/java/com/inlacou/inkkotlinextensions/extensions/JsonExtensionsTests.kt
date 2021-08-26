package com.inlacou.inkkotlinextensions.extensions

import com.inlacou.inkkotlinextensions.*
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class JsonExtensionsTests {

	data class Person(
		val name: String,
		val surname: String,
	)

	val simpleItem = Person("John", "Smith")
	val simpleItemList = listOf(Person("John", "Smith"), Person("John", "Smith"))

	@Test fun `simple item to json`() = assertEquals("""{"name":"John","surname":"Smith"}""", simpleItem.toJson())
	@Test fun `simple item from json`() = assertEquals(simpleItem, simpleItem.toJson().fromJson<Person>())

}
