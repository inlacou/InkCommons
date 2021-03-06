package com.inlacou.inkkotlinextensions.extensions

import com.inlacou.inkkotlinextensions.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class IntExtensionsTests {

	@Test fun `1 has 1 digit`() = assertEquals(1, 1.digitsNum)
	@Test fun `12 has 2 digit`() = assertEquals(2, 12.digitsNum)
	@Test fun `123 has 3 digit`() = assertEquals(3, 123.digitsNum)
	@Test fun `1234 has 4 digit`() = assertEquals(4, 1234.digitsNum)
	@Test fun `12345 has 5 digit`() = assertEquals(5, 12345.digitsNum)

	@Test fun `12345 to min digits 0 is 12345`() = assertEquals("12345", 12345.toStringMinDigits(0))
	@Test fun `1     to min digits 0 is 1`() = assertEquals("1", 1.toStringMinDigits(0))
	@Test fun `0     to min digits 1 is 0`() = assertEquals("0", 0.toStringMinDigits(1))
	@Test fun `10    to min digits 1 is 10`() = assertEquals("10", 10.toStringMinDigits(1))
	@Test fun `0     to min digits 2 is 00`() = assertEquals("00", 0.toStringMinDigits(2))
	@Test fun `1     to min digits 2 is 01`() = assertEquals("01", 1.toStringMinDigits(2))
	@Test fun `10    to min digits 2 is 10`() = assertEquals("10", 10.toStringMinDigits(2))
	@Test fun `0     to min digits 3 is 000`() = assertEquals("000", 0.toStringMinDigits(3))
	@Test fun `1     to min digits 3 is 001`() = assertEquals("001", 1.toStringMinDigits(3))
	@Test fun `10    to min digits 3 is 010`() = assertEquals("010", 10.toStringMinDigits(3))
	@Test fun `0     to min digits 4 is 0000`() = assertEquals("0000", 0.toStringMinDigits(4))
	@Test fun `1     to min digits 4 is 0001`() = assertEquals("0001", 1.toStringMinDigits(4))
	@Test fun `10    to min digits 4 is 0010`() = assertEquals("0010", 10.toStringMinDigits(4))

	//Intuitive cases
	@Test fun `10 change range from 0-20 to 0-10 is 5`() = assertEquals(5, 10.changeValueFromOneRangeToAnother(0, 20, 0, 10))
	@Test fun `10 change range from 0-30 to 0-10 is 3`() = assertEquals(3, 10.changeValueFromOneRangeToAnother(0, 30, 0, 10))
	@Test fun `10 change range from 0-20 to 0-10 is dot5f (float)`() = assertEquals(.5f, 10.changeValueFromOneRangeToAnother(0, 20, 0f, 1f))
	@Test fun `10 change range from 0-30 to 0-10 is dot3f (limit to one decimal) (float)`() = assertEquals(.3f, 10.changeValueFromOneRangeToAnother(0, 30, 0f, 1f).roundDecimals(1))
	@Test fun `10 change range from 0-20 to 0-10 is dot5f (float(2))`() = assertEquals(0.5f, 10f.changeValueFromOneRangeToAnother(0f, 20f, 0f, 1f))
	@Test fun `10 change range from 0-30 to 0-10 is fot3f (limit to one decimal) (float(2))`() = assertEquals(0.3f, 10f.changeValueFromOneRangeToAnother(0f, 30f, 0f, 1f).roundDecimals(1))
	//Non-intuitive cases
	@Test fun `11 change range from 0-22 to 0-15 is 7`() = assertEquals(7, 11.changeValueFromOneRangeToAnother(0, 22, 0, 15))
	@Test fun `12 change range from 0-22 to 0-15 is 8`() = assertEquals(8, 12.changeValueFromOneRangeToAnother(0, 22, 0, 15))

	@Test fun `12 is between 10 and 20`() = assertTrue(12.between(10, 20))
	@Test fun `12 is between 0 and 15`() = assertTrue(12.between(0, 15))
	@Test fun `12 is between 11 and 13`() = assertTrue(12.between(11, 13))
	@Test fun `12 is between 12 and 12`() = assertTrue(12.between(12, 12))

	@Test fun `12 is between 10 with range 10`() = assertTrue(12.betweenRange(10, 10))
	@Test fun `12 is between 15 with range 5`() = assertTrue(12.betweenRange(15, 5))
	@Test fun `12 is between 11 with range 1`() = assertTrue(12.betweenRange(11, 1))
	@Test fun `12 is between 12 with range 0`() = assertTrue(12.betweenRange(12, 0))

	@Test fun `12 is between 10 with range 10 (floats)`() = assertTrue(12f.betweenRange(10f, 10f))
	@Test fun `12 is between 15 with range 5 (floats)`() = assertTrue(12f.betweenRange(15f, 5f))
	@Test fun `12 is between 11 with range 1 (floats)`() = assertTrue(12f.betweenRange(11f, 1f))
	@Test fun `12 is between 12 with range 0 (floats)`() = assertTrue(12f.betweenRange(12f, 0f))
	
	@Test fun `5 of 10 is percentage 50%`() = assertEquals(0.5f, 5.toPercentage(10))
	@Test fun `5 of 15 is percentage 33%`() = assertEquals("0.33", 5.toPercentage(15).toString().take(4))

}
