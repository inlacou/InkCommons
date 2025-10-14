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

fun Int.changeValueFromOneRangeToAnother(oldMin: Int, oldMax: Int, newMin: Float, newMax: Float): Float {
	val oldValue = this.toFloat()
	val newRange = (newMax - newMin)
	val oldRange = (oldMax - oldMin)
	return  if  (oldRange == 0) newMin
			else (((oldValue - oldMin) * newRange) / oldRange) + newMin
}

/**
 * Example:
 *   10000f.calculateCompoundInterestAndLogIt(0.02f, 10, "año", "€")
 * gives:
 *   12189€ (+2189.0) en 10 años con una mejora por año de un 2.0% y partiendo de un inicio de 10000.0€
 */
fun Float.calculateCompoundInterestAndLogIt(interes: Float, timeUnits: Int, timeUnitName: String, initialValueUnit: String): Float {
	val increment = 1f+interes
	val result = this.calculateCompoundInterest(interes, timeUnits)
	println("${result.toInt()}$initialValueUnit (+${result.toInt()-this}) en $timeUnits ${timeUnitName}s con una mejora por $timeUnitName de un ${(increment*100-100f)}% y partiendo de un inicio de $this$initialValueUnit")
	return result
}

fun Float.calculateCompoundInterest(interestPerInterval: Float, numberOfIntervals: Int): Float {
	val increment = 1f + interestPerInterval
	var result = this
	repeat(numberOfIntervals) { result *= increment }
	return result
}