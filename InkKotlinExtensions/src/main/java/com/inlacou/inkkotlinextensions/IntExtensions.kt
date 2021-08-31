package com.inlacou.inkkotlinextensions

import kotlin.math.floor
import kotlin.math.roundToInt

val Int.digitsNum: Int get() = toString().length

fun Int.between(low: Int, high: Int): Boolean {
	return this in low..high
}

fun Int.toPercentage(max: Int): Float {
	return (this.toFloat()/max.toFloat())
}

fun Int.betweenRange(other: Int, range: Int): Boolean {
	return this in (other-range)..(other+range)
}

fun Float.betweenRange(other: Float, range: Float): Boolean {
	return this in (other-range)..(other+range)
}

fun Long.toStringMinDigits(minDigits: Int): String {
	var result = this.toString()
	while (result.length<minDigits){
		result = "0$result"
	}
	return result
}

fun Int.toStringMinDigits(minDigits: Int): String {
	var result = this.toString()
	while (result.length<minDigits){
		result = "0$result"
	}
	return result
}

/**
 * Result is rounded, up in case of .50000..., so 7.5 as result would be a 8, 7.49 -> 7 and 7.51 -> 8
 */
fun Int.changeValueFromOneRangeToAnother(oldMin: Int, oldMax: Int, newMin: Int, newMax: Int): Int {
	val oldValue = this.toDouble()
	val newRange = (newMax - newMin)
	val oldRange = (oldMax - oldMin)
	val newValue =
			if (oldRange == 0) newMin.toDouble()
			else (((oldValue - oldMin) * newRange) / oldRange) + newMin
	return floor(newValue).roundToInt()
}
