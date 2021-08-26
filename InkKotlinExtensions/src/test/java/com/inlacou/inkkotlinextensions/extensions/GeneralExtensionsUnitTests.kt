package com.inlacou.inkkotlinextensions.extensions

import com.inlacou.inkkotlinextensions.digitsNum
import com.inlacou.inkkotlinextensions.randomNumber
import com.inlacou.inkkotlinextensions.randomText
import org.junit.Assert
import org.junit.jupiter.api.Test

class GeneralExtensionsUnitTests {
	
	@Test
	fun random_number_digits() {
		(1 .. 100).forEach {
			assert(it>= randomNumber(it).digitsNum)
		}
	}
	
	@Test
	fun random_text_digits() {
		(1 .. 100).forEach {
			Assert.assertEquals(it, randomText(it).length)
		}
	}
	
}
