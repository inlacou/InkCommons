package com.inlacou.inkkotlincommons

import kotlinx.serialization.Serializable
import java.util.Calendar

@Serializable
data class YearMonth(
    val year: Int,
    val month: Int,
): Comparable<YearMonth> {
    constructor(calendar: Calendar): this(month = calendar.get(Calendar.MONTH), year = calendar.get(Calendar.YEAR))

    companion object {
        fun getInstance() = YearMonth(Calendar.getInstance())
    }

    val totalMonths by lazy { year * 12 + month }
    val calendar: Calendar by lazy { Calendar.Builder().setDate(year, month, 0).build() }

    fun addMonths(months: Int): YearMonth {
        val newMonths = (month+months)%12
        return YearMonth(year = year+((month+months)/12)+(if(newMonths < 0) -1 else 0), month = if(newMonths < 0) 12+newMonths else newMonths)
    }
    fun diffInMonths(other: YearMonth): Int = totalMonths-other.totalMonths

    operator fun inc(): YearMonth = if(month == 11) YearMonth(month = 0, year = this.year+1) else YearMonth(month = this.month+1, year = this.year)
    operator fun dec(): YearMonth = if(month ==  0) YearMonth(month = 11, year = this.year-1) else YearMonth(month = this.month-1, year = this.year)
    override operator fun compareTo(other: YearMonth): Int = totalMonths.compareTo(other.totalMonths)
    fun before(other: YearMonth): Boolean = this < other
    fun after(other: YearMonth): Boolean = this > other

    override fun toString(): String {
        return "$year/${(month+1).toString().padStart(2, '0')}"
    }
}