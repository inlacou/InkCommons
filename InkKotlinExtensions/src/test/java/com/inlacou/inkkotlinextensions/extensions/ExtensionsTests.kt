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
class ExtensionsTests {

	val booleanNullable: Boolean? = null

	@Test fun `true inverse is false`() = assertEquals(false, true.inverse())
	@Test fun `false inverse is true`() = assertEquals(true, false.inverse())
	@Test fun `null inverse is null`() = assertEquals(null, booleanNullable?.inverse())

	@Test fun `systemDecimalSeparator is dot`() = assertEquals(".", systemDecimalSeparator)
	@Test fun `systemThousandSeparator is ,`() = assertEquals(",", systemThousandSeparator)

	@Test fun `randomNumber 1 has 1 digit`() = repeat(10) { val rando = randomNumber(1); assertTrue("$rando has ${rando.digitsNum}", rando.digitsNum<=1) }
	@Test fun `randomNumber 2 has 2 digit`() = repeat(10) { val rando = randomNumber(2); assertTrue("$rando has ${rando.digitsNum}", rando.digitsNum<=2) }
	@Test fun `randomNumber 3 has 3 digit`() = repeat(10) { val rando = randomNumber(3); assertTrue("$rando has ${rando.digitsNum}", rando.digitsNum<=3) }
	@Test fun `randomNumber 4 has 4 digit`() = repeat(10) { val rando = randomNumber(4); assertTrue("$rando has ${rando.digitsNum}", rando.digitsNum<=4) }
	@Test fun `randomNumber 5 has 5 digit`() = repeat(10) { val rando = randomNumber(5); assertTrue("$rando has ${rando.digitsNum}", rando.digitsNum<=5) }

	@Test fun `randomText 1 has 1 digit`() = assertEquals(1, randomText(1).length)
	@Test fun `randomText 2 has 2 digit`() = assertEquals(2, randomText(2).length)
	@Test fun `randomText 3 has 3 digit`() = assertEquals(3, randomText(3).length)
	@Test fun `randomText 4 has 4 digit`() = assertEquals(4, randomText(4).length)
	@Test fun `randomText 5 has 5 digit`() = assertEquals(5, randomText(5).length)

	//TODO move to android  @Test fun `colorToHex WHITE is FFFFFF`() = assertEquals("#FFFFFF", Color.WHITE.colorToHex())
	//TODO move to android @Test fun `colorToHex BLACK is 000000`() = assertEquals("#000000", Color.BLACK.colorToHex())

	@Test fun `0 millis is 00 00 00`() = assertEquals("00:00:00", 0L.millisToTimeLabel())
	@Test fun `1000 millis is 00 00 01`() = assertEquals("00:00:01", 1000L.millisToTimeLabel())
	@Test fun `60000 millis is 00 01 00`() = assertEquals("00:01:00", 60000L.millisToTimeLabel())
	@Test fun `300000 millis is 00 05 00`() = assertEquals("00:05:00", 300000L.millisToTimeLabel())
	@Test fun `3000000 millis is 00 50 00`() = assertEquals("00:50:00", 3000000L.millisToTimeLabel())
	@Test fun `6000000 millis is 01 40 00`() = assertEquals("01:40:00", 6000000L.millisToTimeLabel())

	@Test fun `char 100 is d`() = assertEquals('d', 100.toChar())
	@Test fun `char 101 is e`() = assertEquals('e', 101.toChar())
	@Test fun `char 102 is f`() = assertEquals('f', 102.toChar())

	@Test fun `char 1 is A (custom)`() = assertEquals("A", 1.toCharr())
	@Test fun `char 2 is B (custom)`() = assertEquals("B", 2.toCharr())
	@Test fun `char 3 is C (custom)`() = assertEquals("C", 3.toCharr())
}
