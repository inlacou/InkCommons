package com.inlacou.inkkotlinextensions

val Long.totalSeconds get(): Long = this/1000
val Long.seconds get(): Long = (this/1000)%60
val Long.totalMinutes get(): Long = totalSeconds/60
val Long.minutes get(): Long = (totalSeconds/60)%60
val Long.totalHours get(): Long = totalMinutes/60
val Long.hours get(): Long = (totalMinutes/60)%24
val Long.days get(): Long = totalHours/24
val Long.weeks get(): Long = days/7

/**
 * The [Long] is not a timestamp here. That's *crucial*.
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