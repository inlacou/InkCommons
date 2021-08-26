package com.inlacou.inkkotlinextensions

import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

	@Test fun `addition is correct (for sanity)`() = assertEquals(4, 2 + 2)
	
	@Test fun `ranges until 0 is not in 0 until 10`() = assertEquals("0 is not in 0 until 10", true, 0 in 0 until 10)
	@Test fun `ranges until 10 is not in 0 until 10`() = assertEquals("10 is not in 0 until 10", false, 10 in 0 until 10)
	@Test fun `ranges until 5 is in 0 until 10`() = assertEquals("5 is in 0 until 10", true, 5 in 0 until 10)
	@Test fun `ranges until 15 is not in 0 until 10`() = assertEquals("15 is not in 0 until 10", true, 5 in 0 until 10)
	
	@Test fun `ranges dots 0 is in 0 dotdot 10`() = assertEquals("0 is in 0 .. 10", true, 0 in 0 .. 10)
	@Test fun `ranges dots 10 is in 0 dotdot 10`() = assertEquals("5 is in 0 .. 10", true, 5 in 0 .. 10)
	@Test fun `ranges dots 5 is in 0 dotdot 10`() = assertEquals("10 is in 0 .. 10", true, 10 in 0 .. 10)
	@Test fun `ranges dots 15 is not in 0 dotdot 10`() = assertEquals("15 is not in 0 .. 10", false, 15 in 0 .. 10)

}
