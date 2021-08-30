package com.inlacou.inkkotlincommons

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EitherUnitTests {
	
	data class Person(val name: String, val surname: String, val birthYear: Int)
	data class Error(val message: String)
	val success: Either<Error, Person> = Either.Right(Person("Dalinar", "Kholin", 2800))
	val error: Either<Error, Person> = Either.Left(Error("Error"))
	
	@Test fun `error is left`() = Assertions.assertTrue(error is Either.Left)
	@Test fun `success is right`() = Assertions.assertTrue(success is Either.Right)
	
	@Test
	fun `checking and casting`() {
		val v1 = getRequestSuccess()
		val v2 = getRequestError()
		
		if(v1 is Either.Left) {
			v1.left.message
		}else if(v1 is Either.Right) {
			v1.right.name
		}
		
		if(v2 is Either.Left) {
			v2.left.message
		}else if(v2 is Either.Right) {
			v2.right.birthYear
		}
	}
	
	private fun getRequestSuccess(): Either<Error, Person> = success
	private fun getRequestError(): Either<Error, Person> = error
}
