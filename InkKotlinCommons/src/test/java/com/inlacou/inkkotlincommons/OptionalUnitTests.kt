package com.inlacou.inkkotlincommons

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class OptionalUnitTests {
	
	data class Person(val name: String, val surname: String, val birthYear: Int)
	
	val isNull = Optional<Person>(null)
	val isNotNull = Optional(Person("Dalinar", "Kholin", 2800))
	
	@Test
	fun test() {
		Assertions.assertNotNull(isNotNull.value)
		Assertions.assertNull(isNull.value)
	}
	
}
