package com.inlacou.inkkotlincommons

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DateTests {

    @Test fun diff() {
        val first = YearMonth(year = 2026, month = 9)
        val second = YearMonth(year = 2026, month = 8)

        println("$first | ${first.totalMonths}")
        println("$second | ${second.totalMonths}")

        Assertions.assertEquals(1, first.diffInMonths(second))
    }

    @Test fun addMonths_minus2() {
        val first = YearMonth(year = 2026, month = 9)
        val second = first.addMonths(-2)
        val expected = YearMonth(year = 2026, month = 7)

        Assertions.assertEquals(expected, second)
    }

    @Test fun addMonths_plus10() {
        val first = YearMonth(year = 2026, month = 9)
        val second = first.addMonths(10)
        val expected = YearMonth(year = 2027, month = 7)

        Assertions.assertEquals(expected, second)
    }

    @Test fun addMonths_minus10() {
        val first = YearMonth(year = 2026, month = 2)
        val second = first.addMonths(-10)
        val expected = YearMonth(year = 2025, month = 4)

        Assertions.assertEquals(expected, second)
    }

}