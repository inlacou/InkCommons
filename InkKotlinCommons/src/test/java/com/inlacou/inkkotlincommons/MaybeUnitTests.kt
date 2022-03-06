package com.inlacou.inkkotlincommons

import com.inlacou.inkkotlincommons.monads.Maybe
import org.junit.jupiter.api.Assertions
import org.junit.Test

class MaybeUnitTests {
	
	data class Person(val name: String, val surname: String, val birthYear: Int)
	
	val isNull = Maybe<Person>(null)
	val isNotNull = Maybe(Person("Dalinar", "Kholin", 2800))
	
	@Test
	fun test() {
		Assertions.assertNotNull(isNotNull.value)
		Assertions.assertNull(isNull.value)
	}
	
}
