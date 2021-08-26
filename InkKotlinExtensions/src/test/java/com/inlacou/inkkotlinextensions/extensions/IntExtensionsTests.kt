package com.inlacou.inkkotlinextensions.extensions

import com.inlacou.inkkotlinextensions.*
import org.junit.Assert.assertEquals
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
	//Non-intuitive cases
	@Test fun `11 change range from 0-22 to 0-15 is 7`() = assertEquals(7, 11.changeValueFromOneRangeToAnother(0, 22, 0, 15))
	@Test fun `12 change range from 0-22 to 0-15 is 8`() = assertEquals(8, 12.changeValueFromOneRangeToAnother(0, 22, 0, 15))

}
