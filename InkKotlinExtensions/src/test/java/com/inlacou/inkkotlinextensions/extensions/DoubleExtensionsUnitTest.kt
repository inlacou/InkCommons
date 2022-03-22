package com.inlacou.inkkotlinextensions.extensions

import com.inlacou.inkkotlinextensions.*
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import java.math.RoundingMode
import kotlin.math.roundToInt

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@TestMethodOrder(MethodOrderer.MethodName::class)
class DoubleExtensionsUnitTest {

	@Test
	fun round_to_int(){
		//This test is here to warn me if this changes
		assertEquals(1, 1.0.roundToInt())
		assertEquals(1, 1.1.roundToInt())
		assertEquals(1, 1.2.roundToInt())
		assertEquals(1, 1.3.roundToInt())
		assertEquals(1, 1.4.roundToInt())
		assertEquals(1, 1.49.roundToInt())
		assertEquals(2, 1.5.roundToInt()) //Mostly this line
		assertEquals(2, 1.51.roundToInt())
		assertEquals(2, 1.6.roundToInt())
		assertEquals(2, 1.7.roundToInt())
		assertEquals(2, 1.8.roundToInt())
		assertEquals(2, 1.9.roundToInt())
		assertEquals(2, 2.0.roundToInt())
	}
	
	@Test
	fun number_of_decimals(){
		assertEquals(1, 2.toDouble().decimalsNumber())
		assertEquals(1, 2.0.decimalsNumber())
		assertEquals(1, 2.1.decimalsNumber())
		assertEquals(2, 2.12.decimalsNumber())
		assertEquals(3, 2.123.decimalsNumber())
	}

	@Test
	fun decimals(){
		assertEquals(0, 2.toDouble().decimals())
		assertEquals(0, 2.0.decimals())
		assertEquals(1, 2.1.decimals())
		assertEquals(12, 2.12.decimals())
		assertEquals(123, 2.123.decimals())
	}

	@Test
	fun scrap_decimals(){
		assertEquals("2.2", 2.2.scrapDecimals())
		assertEquals("2", 2.0.scrapDecimals())
		assertEquals("2.12", 2.12.scrapDecimals())
		assertEquals("2.12", 2.121.scrapDecimals())
		assertEquals("2.12", 2.1212.scrapDecimals())
		assertEquals("2.121", 2.121.scrapDecimals(3))
		assertEquals("2.121", 2.1212.scrapDecimals(3))
		assertEquals("2.121", 2.121.scrapDecimals(4))
		assertEquals("2.1212", 2.1212.scrapDecimals(4))
	}

	@Test
	fun addition_isCorrect() {
		assertEquals(true, 2.2.compareDoubleLimitDecimals(2.2, 10))
		assertEquals(true, 2.2.compareDoubleLimitDecimals(2.23, 1))
		assertEquals(true, 2.23.compareDoubleLimitDecimals(2.23, 1))
		assertEquals(true, 2.23.compareDoubleLimitDecimals(2.2, 1))
		assertEquals(false, 2.23.compareDoubleLimitDecimals(2.2, 2))
		assertEquals(false, 2.23.compareDoubleLimitDecimals(2.24, 2))
		assertEquals(false, 2.23.compareDoubleLimitDecimals(2.0, 2))
		assertEquals(true, 2.0.compareDoubleLimitDecimals(2.0, 2))
		assertEquals(false, 2.0.compareDoubleLimitDecimals(2.24, 2))
		assertEquals(true, 2.2345678.compareDoubleLimitDecimals(2.2345678, 2))
		assertEquals(true, 2.2345678.compareDoubleLimitDecimals(2.2345678, 3))
		assertEquals(true, 2.2345678.compareDoubleLimitDecimals(2.2345678, 4))
		assertEquals(true, 2.2345678.compareDoubleLimitDecimals(2.2345678, 5))
		assertEquals(true, 2.2345678.compareDoubleLimitDecimals(2.2345678, 6))
		assertEquals(true, 2.2345678.compareDoubleLimitDecimals(2.2345678, 7))
	}
	
	@Test
	fun compare_with_limited_decimals(){
		assertEquals(true, 2.0.compareDoubleLimitDecimals(2.0, 2))
		assertEquals(true, 2.22.compareDoubleLimitDecimals(2.22, 2))
		assertEquals(true, 2.22.compareDoubleLimitDecimals(2.2222, 2))
		assertEquals(true, 2.22.compareDoubleLimitDecimals(2.2223, 2))
		assertEquals(true, 2.22.compareDoubleLimitDecimals(2.2233, 2))
	}
	
	@Test
	fun invertFloatOnRange() {
		assertEquals(1.2f, (-0.2f).invert(0, 1))
		assertEquals(1.5f, (-0.5f).invert(0, 1))
		assertEquals(0.0f, (1.0f).invert(0, 1))
		assertEquals(1.0f, (0.0f).invert(0, 1))
		assertEquals(0.5f, (0.5f).invert(0, 1))
		assertEquals(0.3f, (0.7f).invert(0, 1))
		assertEquals(2.2f, (0.8f).invert(1, 2))
		assertEquals(0.5f, (2.5f).invert(1, 2))
		assertEquals(2.0f, (1.0f).invert(1, 2))
		assertEquals(1.0f, (2.0f).invert(1, 2))
		assertEquals(1.5f, (1.5f).invert(1, 2))
		assertEquals(1.3f, (1.7f).invert(1, 2))
		assertEquals(1.2, (-0.2).invert(0, 1), 0.01) //A delta of 0.01 masks rounding errors that shouldn't even happen but happen
		assertEquals(1.5, (-0.5).invert(0, 1), 0.01)
		assertEquals(0.0, (1.0).invert(0, 1), 0.01)
		assertEquals(1.0, (0.0).invert(0, 1), 0.01)
		assertEquals(0.5, (0.5).invert(0, 1), 0.01)
		assertEquals(0.3, (0.7).invert(0, 1), 0.01)
		assertEquals(2.2, (0.8).invert(1, 2), 0.01)
		assertEquals(0.5, (2.5).invert(1, 2), 0.01)
		assertEquals(2.0, (1.0).invert(1, 2), 0.01)
		assertEquals(1.0, (2.0).invert(1, 2), 0.01)
		assertEquals(1.5, (1.5).invert(1, 2), 0.01)
		assertEquals(1.3, (1.7).invert(1, 2), 0.01)
	}
	
	@Test fun `percentage  100 of max 200 is  0_5f`() = assertEquals(0.5f, 100f.percentageOf(200f))
	@Test fun `percentage  200 of max 200 is    1f`() = assertEquals(1f, 200f.percentageOf(200f))
	@Test fun `percentage -100 of max 200 is -0_5f`() = assertEquals(-0.5f, (-100f).percentageOf(200f))
	@Test fun `percentage -200 of max 200 is   -1f`() = assertEquals(-1f, (-200f).percentageOf(200f))
	
	@Test fun `percentage  100 of max 200 min -100 is  0_5f`() = assertEquals(0.5f, 100f.percentageOf(200f, -100f))
	@Test fun `percentage  200 of max 200 min -100 is    1f`() = assertEquals(1f, 200f.percentageOf(200f, -100f))
	@Test fun `percentage  -50 of max 200 min -100 is -0_5f`() = assertEquals(-0.5f, (-50f).percentageOf(200f, -100f))
	@Test fun `percentage -100 of max 200 min -100 is   -1f`() = assertEquals(-1f, (-100f).percentageOf(200f, -100f))
	
	@Test fun `percentage    0 of max 200 min -100 with current  0_5f is  0_5f`() = assertEquals(0.5f, (0f).percentageOf(0.5f, 200f, -100f))
	@Test fun `percentage    0 of max 200 min -100 with current    1f is    1f`() = assertEquals(1f, (0f).percentageOf(1f, 200f, -100f))
	@Test fun `percentage    0 of max 200 min -100 with current -0_5f is -0_5f`() = assertEquals(-0.5f, (0f).percentageOf(-0.5f, 200f, -100f))
	@Test fun `percentage    0 of max 200 min -100 with current   -1f is   -1f`() = assertEquals(1f, (0f).percentageOf(1f, 200f, -100f))
	@Test fun `percentage  100 of max 200 min -100 with current  0_5f is 0_75f`() = assertEquals(0.75f, (100f).percentageOf(0.5f, 200f, -100f))
	@Test fun `percentage  200 of max 200 min -100 with current  0_5f is    1f`() = assertEquals(1f, (200f).percentageOf(0.5f, 200f, -100f))
	@Test fun `percentage  -50 of max 200 min -100 with current  0_5f is 0_25f`() = assertEquals(0.25f, (-50f).percentageOf(0.5f, 200f, -100f))
	@Test fun `percentage -100 of max 200 min -100 with current  0_5f is    0f`() = assertEquals(0f, (-100f).percentageOf(0.5f, 200f, -100f))
	@Test fun `percentage -100 of max 200 min -100 with current  0_75f is   0f`() = assertEquals(0f, (-100f).percentageOf(0.75f, 200f, -100f))
	@Test fun `percentage  -20 of max 200 min -100 with current  0_75f is 0_6f`() = assertEquals(0.6f, (-20f).percentageOf(0.75f, 200f, -100f))
	@Test fun `percentage   40 of max 200 min -100 with current  0_75f is 0_8f`() = assertEquals(0.8f, (40f).percentageOf(0.75f, 200f, -100f))


	@Test fun `1dot3334f round to -3 decimals is 1`() = assertEquals(1f, 1.3334f.roundDecimals(-3))
	@Test fun `1dot3334f round to -2 decimals is 1`() = assertEquals(1f, 1.3334f.roundDecimals(-2))
	@Test fun `1dot3334f round to -1 decimals is 1`() = assertEquals(1f, 1.3334f.roundDecimals(-1))
	@Test fun `1dot3334f round to 0 decimals is 1`() = assertEquals(1f, 1.3334f.roundDecimals(0))
	@Test fun `1dot3334f round to 1 decimals is 1dot3`() = assertEquals(1.3f, 1.3334f.roundDecimals(1))
	@Test fun `1dot3334f round to 2 decimals is 1dot33`() = assertEquals(1.33f, 1.3334f.roundDecimals(2))
	@Test fun `1dot3334f round to 3 decimals is 1dot333`() = assertEquals(1.333f, 1.3334f.roundDecimals(3))
	@Test fun `1dot3334f round to 4 decimals is 1dot3334`() = assertEquals(1.3334f, 1.3334f.roundDecimals(4))
	@Test fun `1dot3334f round to 5 decimals is 1dot3334`() = assertEquals(1.3334f, 1.3334f.roundDecimals(5))

	@Test fun `1dot3334 round to -3 decimals is 1`() = assertEquals(1.0, 1.3334.roundDecimals(-3), 0.0)
	@Test fun `1dot3334 round to -2 decimals is 1`() = assertEquals(1.0, 1.3334.roundDecimals(-2), 0.0)
	@Test fun `1dot3334 round to -1 decimals is 1`() = assertEquals(1.0, 1.3334.roundDecimals(-1), 0.0)
	@Test fun `1dot3334 round to 0 decimals is 1`() = assertEquals(1.0, 1.3334.roundDecimals(0), 0.0)
	@Test fun `1dot3334 round to 1 decimals is 1dot3`() = assertEquals(1.3, 1.3334.roundDecimals(1), 0.0)
	@Test fun `1dot3334 round to 2 decimals is 1dot33`() = assertEquals(1.33, 1.3334.roundDecimals(2), 0.0)
	@Test fun `1dot3334 round to 3 decimals is 1dot333`() = assertEquals(1.333, 1.3334.roundDecimals(3), 0.0)
	@Test fun `1dot3334 round to 4 decimals is 1dot3334`() = assertEquals(1.3334, 1.3334.roundDecimals(4), 0.0)
	@Test fun `1dot3334 round to 5 decimals is 1dot3334`() = assertEquals(1.3334, 1.3334.roundDecimals(5), 0.0)
	@Test fun `1dot3335 round to 3 decimals (HALF_UP) is 1dot334`() = assertEquals(1.334, 1.3335.roundDecimals(3, RoundingMode.HALF_UP), 0.0)
	@Test fun `1dot3335 round to 3 decimals (HALF_EVEN) is 1dot334`() = assertEquals(1.334, 1.3335.roundDecimals(3, RoundingMode.HALF_EVEN), 0.0)
	@Test fun `1dot3335 round to 3 decimals (HALF_DOWN) is 1dot333`() = assertEquals(1.333, 1.3335.roundDecimals(3, RoundingMode.HALF_DOWN), 0.0)

	@Test fun `-1dot3337 round to 3 decimals (FLOOR) is 1dot334`() = assertEquals(-1.334, (-1.3337).roundDecimals(3, RoundingMode.FLOOR), 0.0)
	@Test fun `-1dot3337 round to 3 decimals (DOWN) is 1dot333`() = assertEquals(-1.333, (-1.3337).roundDecimals(3, RoundingMode.DOWN), 0.0)
}
