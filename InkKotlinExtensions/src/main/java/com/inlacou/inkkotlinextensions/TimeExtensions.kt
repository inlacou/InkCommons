package com.inlacou.inkkotlinextensions

val Long.totalSeconds get(): Long = this/1000
val Long.seconds get(): Long = (this/1000)%60
val Long.totalMinutes get(): Long = totalSeconds/60
val Long.minutes get(): Long = (totalSeconds/60)%60
val Long.totalHours get(): Long = totalMinutes/60
val Long.hours get(): Long = (totalMinutes/60)%24
val Long.days get(): Long = totalHours/24
val Long.weeks get(): Long = days/7
