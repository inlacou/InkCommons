package com.inlacou.inkandroidextensions

import android.content.Context
import java.util.*

@JvmOverloads fun Long?.toDate(context: Context, monthAsText: Boolean = true): String {
	if (this == null) return ""
	val calendar = Calendar.getInstance()
	calendar.timeInMillis = this
	return calendar.toDate(context, monthAsText)
}

@JvmOverloads fun Calendar?.toDate(context: Context, monthAsText: Boolean = true): String {
	if (this == null) return ""
	return String.format(
		if (monthAsText) context.resources.getString(R.string.date)
		else context.resources.getString(R.string.date_month_as_number)
	, this)
}

fun Long?.toDateTime(context: Context, separator: String = ", ", dayOfWeek: Boolean = false, monthAsNumber: Boolean = true): String {
	if (this == null) return ""
	val calendar = Calendar.getInstance()
	calendar.timeInMillis = this
	return calendar.toDateTime(context, separator, dayOfWeek, monthAsNumber)
}

fun Calendar?.toDateTime(context: Context, separator: String = ", ", dayOfWeek: Boolean = false, monthAsNumber: Boolean = true): String {
	if (this == null) return ""
	return if(dayOfWeek) {
		if(monthAsNumber) String.format(context.resources.getString(R.string.datetime_day_of_week_month_as_number), this, separator)
		else String.format(context.resources.getString(R.string.datetime_day_of_week), this, separator)
	}else if(monthAsNumber) String.format(context.resources.getString(R.string.datetime_month_as_number), this, separator)
	else String.format(context.resources.getString(R.string.datetime), this, separator)
}

fun Int.toMonthName(context: Context): String {
	return toLong().toMonthName(context)
}

fun Long.toMonthName(context: Context): String {
	return context.getString(when(this) {
		0L -> R.string.January
		1L -> R.string.February
		2L -> R.string.March
		3L -> R.string.April
		4L -> R.string.May
		5L -> R.string.June
		6L -> R.string.July
		7L -> R.string.August
		8L -> R.string.September
		9L -> R.string.October
		10L -> R.string.November
		11L -> R.string.December
		else -> throw IllegalArgumentException("Provided month index must be between 0 and 11. Index provided: $this")
	})
}
