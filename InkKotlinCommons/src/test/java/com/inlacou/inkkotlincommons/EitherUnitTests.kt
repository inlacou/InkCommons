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
		val mSuccess = getRequestSuccess()
		val mError = getRequestError()
		
		if(mSuccess is Either.Left) {
			mSuccess.left.message
		}else if(mSuccess is Either.Right) {
			Assertions.assertEquals((success as Either.Right).right.name, mSuccess.right.name)
		}
		
		if(mError is Either.Left) {
			mError.left.message
			Assertions.assertEquals((error as Either.Left).left.message, mError.left.message)
		}else if(mError is Either.Right) {
			mError.right.birthYear
		}
	}
	
	private fun getRequestSuccess(): Either<Error, Person> = success
	private fun getRequestError(): Either<Error, Person> = error
}
