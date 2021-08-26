package com.inlacou.inkkotlinextensions.extensions

import com.inlacou.inkkotlinextensions.*
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class LongExtensionsTests {

	@Test
	fun to_string_min_digits() {
		assertEquals("12345", 12345L.toStringMinDigits(0))
		assertEquals("1", 1L.toStringMinDigits(0))

		assertEquals("0", 0L.toStringMinDigits(1))
		assertEquals("1", 1L.toStringMinDigits(1))
		assertEquals("10", 10L.toStringMinDigits(1))

		assertEquals("00", 0L.toStringMinDigits(2))
		assertEquals("01", 1L.toStringMinDigits(2))
		assertEquals("10", 10L.toStringMinDigits(2))

		assertEquals("000", 0L.toStringMinDigits(3))
		assertEquals("001", 1L.toStringMinDigits(3))
		assertEquals("010", 10L.toStringMinDigits(3))

		assertEquals("0000", 0L.toStringMinDigits(4))
		assertEquals("0001", 1L.toStringMinDigits(4))
		assertEquals("0010", 10L.toStringMinDigits(4))
	}
	
	@Test
	fun to_time_label() {
		assertEquals("00:00:01", 1000L.millisToTimeLabel())
		assertEquals("00:00:01", 1001L.millisToTimeLabel())
		assertEquals("00:00:01", 1010L.millisToTimeLabel())
		assertEquals("00:00:01", 1100L.millisToTimeLabel())
		assertEquals("00:00:10", 10000L.millisToTimeLabel())
		assertEquals("00:01:00", 60000L.millisToTimeLabel())
		assertEquals("00:01:10", 70000L.millisToTimeLabel())
		assertEquals("00:01:10", 70050L.millisToTimeLabel())
		assertEquals("00:06:00", 360000L.millisToTimeLabel())
		assertEquals("01:00:00", 3600000L.millisToTimeLabel())
	}
	
	/* TODO move to android?
	@Test
	fun to_duration() {
		assertEquals("00:00", 0L.toDuration(showHours = false, showSeconds = true))
		assertEquals("00", 0L.toDuration(showHours = false, showSeconds = false))
		assertEquals("00:00", 0L.toDuration(showHours = true, showSeconds = false))
		assertEquals("01:00", 60000L.toDuration(showHours = false, showSeconds = true))
		assertEquals("01", 60000L.toDuration(showHours = false, showSeconds = false))
		assertEquals("00:01", 60000L.toDuration(showHours = true, showSeconds = false))
		assertEquals("02:00", 120000L.toDuration(showHours = false, showSeconds = true))
		assertEquals("02", 120000L.toDuration(showHours = false, showSeconds = false))
		assertEquals("00:02", 120000L.toDuration(showHours = true, showSeconds = false))
	}*/
}
