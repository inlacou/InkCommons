package com.inlacou.inkkotlinextensions.extensions

import com.inlacou.inkkotlinextensions.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TimeExtensionsUnitTests {
    
    @Test fun `300k millis is 300 seconds`() = Assertions.assertEquals(300, 300000L.totalSeconds)
    @Test fun `330k millis is any minutes and 30 seconds`() = Assertions.assertEquals(30, 330000L.seconds)
    @Test fun `300k millis is 5 min`() = Assertions.assertEquals(5, 300000L.totalMinutes)
    @Test fun `1800k millis is 30 min`() = Assertions.assertEquals(30, 1800000L.totalMinutes)
    @Test fun `3600k millis is 60 min`() = Assertions.assertEquals(60, 3600000L.totalMinutes)
    @Test fun `3900k millis is 65 min`() = Assertions.assertEquals(65, 3900000L.totalMinutes)
    @Test fun `3900k millis is any hours and 5 min`() = Assertions.assertEquals(5, 3900000L.minutes)
    @Test fun `3600k millis is 1 hour`() = Assertions.assertEquals(1, 3600000L.totalHours)
    @Test fun `3900k millis is 1 hour and some minutes`() = Assertions.assertEquals(1, 3900000L.totalHours)
    @Test fun `7200k millis is 2 hours`() = Assertions.assertEquals(2, 7200000L.totalHours)
    @Test fun `93600k millis is 26 hours`() = Assertions.assertEquals(26, 93600000L.totalHours)
    @Test fun `93600k millis is any days and 2 hours`() = Assertions.assertEquals(2, 93600000L.hours)
    @Test fun `93600k millis is 1 day and any hours`() = Assertions.assertEquals(1, 93600000L.days)
    @Test fun `842400k millis is 9 days`() = Assertions.assertEquals(9, 842400000L.days)
    @Test fun `842400k millis is 1 week`() = Assertions.assertEquals(1, 842400000L.weeks)
    @Test fun `842400k millis is 18 days`() = Assertions.assertEquals(18, 1556000000L.days)
    @Test fun `842400k millis is 2 weeks`() = Assertions.assertEquals(2,  1556000000L.weeks)
    
}
