package com.inlacou.inkandroidextensions

import android.content.Context
import com.inlacou.inkkotlinextensions.toStringMinDigits
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/* From: http://stackoverflow.com/questions/4216745/java-string-to-date-conversion
	G       Era designator          Text                AD
	y       Year                    Year                1996; 96
	Y       Week year               Year                2009; 09
	M/L     Month in year           Month               July; Jul; 07
	w       Week in year            Number              27
	W       Week in month           Number              2
	D       Day in year             Number              189
	d       Day in month            Number              10
	F       Day of week in month    Number              2
	E       Day in week             Text                Tuesday; Tue
	u       Day number of week      Number              1
	a       Am/pm marker            Text                PM
	H       Hour in day (0-23)      Number              0
	k       Hour in day (1-24)      Number              24
	K       Hour in am/pm (0-11)    Number              0
	h       Hour in am/pm (1-12)    Number              12
	m       Minute in hour          Number              30
	s       Second in minute        Number              55
	S       Millisecond             Number              978
	z       Time zone               General time zone   Pacific Standard Time; PST; GMT-08:00
	Z       Time zone               RFC 822 time zone   -0800
	X       Time zone               ISO 8601 time zone  -08; -0800; -08:00
 */

/* My List
	 %1$tD = date (day/month/year(2digits) format)
	 %1$td = day
	 %1$tm = month
	 %1$tM = minutes
	 %1$ty = year (2 digits)
	 %1$tY = year (4 digits)
	 %1$tI = hour 12h format
	 %1$tk = hour 24h format
	 %1$tH = hour 24h format
	 %1$Tp = PM or AM
	 %1$tp = pm or am
	 %1$tA = day as text (miércoles)
	 %1$ta = day as text (mié.)
	 %1$TA = day as text (MIÉRCOLES)
	 %1$Ta = day as text (MIË.)
	 %1$tB = month as text (julio)
	 %1$tb = month as text (jul.)
	 %1$th = month as text (jul.)
	 %1$TB = month as text (JULIO)
	 %1$Tb = month as text (JUL.)
  */
var Calendar.year: Int
	set(value) = set(Calendar.YEAR, value)
	get() = get(Calendar.YEAR)
/**
 * Starts at 0
 */
var Calendar.month: Int
	set(value) = set(Calendar.MONTH, value)
	get() = get(Calendar.MONTH)
var Calendar.dayOfYear: Int
	set(value) = set(Calendar.DAY_OF_YEAR, value)
	get() = get(Calendar.DAY_OF_YEAR)
/**
 * Starts at 1
 */
var Calendar.dayOfMonth: Int
	set(value) = set(Calendar.DAY_OF_MONTH, value)
	get() = get(Calendar.DAY_OF_MONTH)
var Calendar.dayOfWeek: Int
	set(value) = set(Calendar.DAY_OF_WEEK, value)
	get() = get(Calendar.DAY_OF_WEEK)
var Calendar.hour: Int
	set(value) = set(Calendar.HOUR_OF_DAY, value)
	get() = get(Calendar.HOUR_OF_DAY)
var Calendar.minute: Int
	set(value) = set(Calendar.MINUTE, value)
	get() = get(Calendar.MINUTE)
var Calendar.second: Int
	set(value) = set(Calendar.SECOND, value)
	get() = get(Calendar.SECOND)
var Calendar.millisecond: Int
	set(value) = set(Calendar.MILLISECOND, value)
	get() = get(Calendar.MILLISECOND)

fun Calendar.setYear(value: Int) : Calendar {
	set(Calendar.YEAR, value)
	return this
}

fun Calendar.setMonth(value: Int) : Calendar {
	set(Calendar.MONTH, value)
	return this
}

fun Calendar.setDayOfYear(value: Int) : Calendar {
	set(Calendar.DAY_OF_YEAR, value)
	return this
}

fun Calendar.setDayOfMonth(value: Int) : Calendar {
	set(Calendar.DAY_OF_MONTH, value)
	return this
}

fun Calendar.setDayOfWeek(value: Int) : Calendar {
	set(Calendar.DAY_OF_WEEK, value)
	return this
}

fun Calendar.setHours(value: Int) : Calendar {
	set(Calendar.HOUR_OF_DAY, value)
	return this
}

fun Calendar.setMinutes(value: Int) : Calendar {
	set(Calendar.MINUTE, value)
	return this
}

fun Calendar.setSeconds(value: Int) : Calendar {
	set(Calendar.SECOND, value)
	return this
}

fun Calendar.setMilliseconds(value: Int) : Calendar {
	set(Calendar.MILLISECOND, value)
	return this
}

fun Calendar.setMilliseconds(millis: Long): Calendar {
	this.timeInMillis = millis
	return this }

fun Long.toCalendar(): Calendar = Calendar.getInstance().setMilliseconds(this)

fun Calendar.isLeapYear() = ((year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0)))

fun Calendar.subtractDays(number: Int): Calendar {
	this.add(Calendar.DAY_OF_YEAR, -number)
	return this }

fun Calendar.addDays(number: Int): Calendar {
	this.add(Calendar.DAY_OF_YEAR, number)
	return this }

fun Calendar.addDaysUntilMonthEnd(): Calendar {
	val currentMonth = month
	while(month==currentMonth) {
		this.add(Calendar.DAY_OF_YEAR, 1)
	}
	this.add(Calendar.DAY_OF_YEAR, -1)
	return this
}

fun Calendar.addMonths(number: Int): Calendar {
	this.add(Calendar.MONTH, number)
	return this }

fun Calendar.subtractMonths(number: Int): Calendar {
	this.add(Calendar.MONTH, -number)
	return this }

fun Calendar.addYears(number: Int): Calendar {
	this.add(Calendar.YEAR, number)
	return this }

/**
 * This method sets the time in the calendar object to 00:00:00.000
 *
 * @receiver Calendar object which time should be set to 00:00:00.000
 * @return Calendar object with time changed to 00:00:00.000
 */
fun Calendar.toMidnight(): Calendar {
	set(Calendar.HOUR_OF_DAY, 0)
	set(Calendar.MINUTE, 0)
	set(Calendar.SECOND, 0)
	set(Calendar.MILLISECOND, 0)
	return this
}

/**
 * This checks if this is isImmediatePreviousMonth to passed calendar.
 *
 * @receiver calendar that must be previous
 * @param postCalendar that must be posterior
 */
fun Calendar.isImmediatePreviousMonth(postCalendar: Calendar): Boolean {
	if(postCalendar.before(this)) return false
	val prevMonth = month
	var postMonth = postCalendar.month
	if(postMonth==0) postMonth += 12
	return postMonth-prevMonth==1
}

fun Calendar.sameMonth(other: Calendar): Boolean = year==other.year && month==other.month

fun Calendar.sameDay(other: Calendar): Boolean = year==other.year && month==other.month && dayOfYear==other.dayOfYear

fun Calendar.isDifferentDay(other: Calendar): Boolean = !sameDay(other)

/**
 * This method compares calendars using month and year
 *
 * @receiver First calendar object to compare
 * @param calendar Second calendar object to compare
 * @return Boolean value if first calendar is before the second one
 */
fun Calendar?.isMonthBefore(calendar: Calendar): Boolean {
	if (this == null) {
		return false
	}
	
	val firstDay = this.clone() as Calendar
	firstDay.toMidnight()
	firstDay.dayOfMonth = 1
	val secondDay = calendar.clone() as Calendar
	secondDay.toMidnight()
	secondDay.dayOfMonth = 1
	
	return firstDay.before(secondDay)
}

/**
 * This method compares calendars using month and year
 *
 * @receiver First calendar object to compare
 * @param calendar Second calendar object to compare
 * @return Boolean value if first calendar is after the second one
 */
fun Calendar?.isMonthAfter(calendar: Calendar): Boolean {
	if (this == null) {
		return false
	}
	
	val firstDay = this.clone() as Calendar
	firstDay.toMidnight()
	firstDay.dayOfMonth = 1
	val secondDay = calendar.clone() as Calendar
	secondDay.toMidnight()
	secondDay.dayOfMonth = 1
	
	return firstDay.after(secondDay)
}

// String utils

fun Long?.toTime(showSeconds: Boolean): String {
	Timber.d(".millisToTime (msg) $this | showSeconds $showSeconds")
	if (this == null) {
		return ""
	}
	if (showSeconds) {
		SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
	} else {
		SimpleDateFormat("HH:mm", Locale.ENGLISH)
	}.let {
		it.timeZone = TimeZone.getTimeZone("Europe/Madrid")
		return it.format(Date(this))
	}
}

@JvmOverloads fun Long?.toDate(context: Context, monthAsText: Boolean = true): String {
	Timber.d(".millisToTime (msg) $this | monthAsText $monthAsText")
	if (this == null) {
		return ""
	}
	val calendar = Calendar.getInstance()
	calendar.timeInMillis = this
	return calendar.toDate(context, monthAsText)
}

fun Calendar?.toDateDebug(): String {
	if (this == null) {
		return ""
	}
	return "$year/${month+1}/$dayOfMonth"
}

fun Calendar?.toDateTimeDebug(): String {
	if (this == null) {
		return ""
	}
	return "$year/${month+1}/$dayOfMonth $hour:$minute"
}

@JvmOverloads fun Calendar?.toDate(context: Context, monthAsText: Boolean = true): String {
	if (this == null) {
		return ""
	}
	return if (monthAsText) {
		String.format(context.resources.getString(R.string.date), this)
	} else {
		String.format(context.resources.getString(R.string.date_month_as_number), this)
	}
}

fun Long?.toDateTime(context: Context, separator: String = ", ", dayOfWeek: Boolean = false, monthAsNumber: Boolean = true): String {
	if (this == null) {
		return ""
	}
	val calendar = Calendar.getInstance()
	calendar.timeInMillis = this
	return calendar.toDateTime(context, separator, dayOfWeek, monthAsNumber)
}

fun Calendar?.toTime(showSeconds: Boolean): String {
	if (this == null) {
		return ""
	}
	return timeInMillis.toTime(showSeconds)
}

/**
 * The long is not a timestamp here. That's crucial.
 */
fun Long.toDuration(showHours: Boolean = true, showMinutes: Boolean = true, showSeconds: Boolean = true): String {
	val seconds = this/1000
	val minutes = seconds/60
	val hours = minutes/60
	val hasHours = hours!=0L
	
	return if((showHours || hasHours) && showMinutes && showSeconds){
		hours.toStringMinDigits(2) +
			":${(minutes-hours*60).toStringMinDigits(2)}" +
			":${(seconds-minutes*60).toStringMinDigits(2)}"
	}else if((showHours || hasHours) && showMinutes) {
		hours.toStringMinDigits(2) +
			":${(minutes-hours*60).toStringMinDigits(2)}"
	}else if(showMinutes && showSeconds) {
		(minutes-hours*60).toStringMinDigits(2) +
			":${(seconds-minutes*60).toStringMinDigits(2)}"
	}else if(showHours){
		hours.toStringMinDigits(2)
	}else if(showMinutes) {
		(minutes-hours*60).toStringMinDigits(2)
	}else if(showSeconds) {
		(seconds-minutes*60).toStringMinDigits(2)
	}else {
		""
	}
}
fun Calendar?.toDateTime(context: Context, separator: String = ", ", dayOfWeek: Boolean = false, monthAsNumber: Boolean = true): String {
	if (this == null) {
		return ""
	}
	return if(dayOfWeek) {
		if(monthAsNumber){
			String.format(context.resources.getString(R.string.datetime_day_of_week_month_as_number), this, separator)
		}else{
			String.format(context.resources.getString(R.string.datetime_day_of_week), this, separator)
		}
	}else if(monthAsNumber) {
		String.format(context.resources.getString(R.string.datetime_month_as_number), this, separator)
	}else{
		String.format(context.resources.getString(R.string.datetime), this, separator)
	}
}

/**
 * Gives you the number of mondays, tuesdays, wednesdays... and so on, for a given month.
 *
 * WARNING! Use Calendar.MONDAY, Calendar.FRIDAY, etc.
 * If you do not want to, MONDAY is 2 and SUNDAY is 1. The week starts on sunday
 */
fun Calendar.numOfDaysOfWeeks(positionOnWeek: Int): Int {
	Calendar.MONDAY
	this.dayOfMonth = 1
	var acc = 0
	val initialMonth = month
	do {
		if(dayOfWeek == positionOnWeek) {
			Timber.d("$positionOnWeek ding: ${toDateDebug()}")
			acc += 1
		}else {
			Timber.d("$positionOnWeek current: ${toDateDebug()}")
		}
		dayOfMonth += 1
	}while (month==initialMonth)
	Timber.d("$positionOnWeek finally: ${toDateDebug()}")
	return acc
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

// String utils
fun Long.toDurationFixed(showHours: Boolean = true, showMinutes: Boolean = true, showSeconds: Boolean = true): String {
	val seconds = this/1000
	val minutes = seconds/60
	val hours = minutes/60
	val hasHours = hours!=0L
	
	return if((showHours || hasHours) && showMinutes && showSeconds){
		hours.toStringMinDigits(2) +
			":${(minutes-hours*60).toStringMinDigits(2)}" +
			":${(seconds-minutes*60).toStringMinDigits(2)}"
	}else if((showHours || hasHours) && showMinutes) {
		hours.toStringMinDigits(2) +
			":${(minutes-hours*60).toStringMinDigits(2)}"
	}else if(showMinutes && showSeconds) {
		(minutes-hours*60).toStringMinDigits(2) +
			":${(seconds-minutes*60).toStringMinDigits(2)}"
	}else if(showHours){
		hours.toStringMinDigits(2)
	}else if(showMinutes) {
		(minutes-hours*60).toStringMinDigits(2)
	}else if(showSeconds) {
		(seconds-minutes*60).toStringMinDigits(2)
	}else {
		""
	}
}

fun Long.toDurationDynamic(showHoursAlways: Boolean = true, showMinutesAlways: Boolean = true, showSecondsAlways: Boolean = true, divider: String = ":"): String {
	val seconds = this/1000
	val minutes = seconds/60
	val hours = minutes/60
	val hasHours = hours!=0L
	val hasMinutes = minutes-hours*60!=0L
	val hasSeconds = seconds-minutes*60!=0L
	
	var result = ""
	
	if((showHoursAlways || hasHours)) result += hours.toStringMinDigits(2)
	if((showMinutesAlways || hasMinutes || hasHours)) {
		if(result.isNotEmpty()) result += divider
		result += (minutes-hours*60).toStringMinDigits(2)
	}
	if((showSecondsAlways || hasSeconds || hasMinutes)) {
		if(result.isNotEmpty()) result += divider
		result += (seconds-minutes*60).toStringMinDigits(2)
	}
	
	return result
}