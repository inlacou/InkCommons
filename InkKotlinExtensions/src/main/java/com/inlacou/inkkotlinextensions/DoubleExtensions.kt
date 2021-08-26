package com.inlacou.inkkotlinextensions

import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.floor

/**
 * How many decimals the number has.
 */
fun Double.decimalsNumber(): Int {
	val s = toString()
	return s.substring(s.indexOf(".")+1, s.length).length
}

/**
 * Decimal value as Int.
 */
fun Double.decimals(): Int {
	val s = toString()
	return s.substring(s.indexOf(".")+1, s.length).toInt()
}

/**
 * Scraps superfluous decimals. Default 2 significant decimals.
 */
fun Double.scrapDecimals(decimals: Int = 2): String {
	var pattern = "#."
	repeat(decimals) { pattern += "#" }
	val df = DecimalFormat(pattern)
	df.roundingMode = RoundingMode.FLOOR
	return df.format(this)
}

/**
 * Compares two double values to see if they are equal. On the decimal part it only checks the defined number of digits.
 * this first number to compare.
 * @param value2 second number to compare.
 * @param maxDecimals max number of digits to compare on decimal part.
 * @return true if equal, false if not
 */
fun Double.compareDoubleLimitDecimals(value2: Double, maxDecimals: Int): Boolean {
	val intValue1 = floor(this)
	val intValue2 = floor(value2)
	
	var decimalValue1 = this.decimals()
	var decimalValue2 = value2.decimals()
	
	while (decimalValue1.digitsNum>maxDecimals) decimalValue1 /= 10
	while (decimalValue2.digitsNum>maxDecimals) decimalValue2 /= 10
	
	return intValue1==intValue2 && decimalValue1==decimalValue2
}

/**
 * Examples:
 * a 0.5 value on a 0 to 1 range stays as 0.5
 * a 0.3 value on a 0 to 1 range changes to 0.7
 * a -0.2 value on a 0 to 1 range changes to 1.2
 */
fun Float.invert(from: Int, to: Int): Float {
	return ((this-from)*-1)+to
}

/**
 * Examples:
 * a 0.5 value on a 0 to 1 range stays as 0.5
 * a 0.3 value on a 0 to 1 range changes to 0.7
 * a -0.2 value on a 0 to 1 range changes to 1.2
 */
fun Double.invert(from: Int, to: Int): Double {
	return ((this-from)*-1)+to
}


/**
 * Percentage, where 1.0f is 100%, 0.0f is 0%, and -1 is 100% on the opposite direction. Caps at 1 and -1. Has different calculation for positive and negative, with different "lengths".
 */
fun Float.percentageOf(positiveMax: Float, negativeMax: Float): Float {
	when {
		this>0f -> {
			val aux = this/positiveMax
			return when {
				aux>1f -> 1f
				else -> aux
			}
		}
		this<0f -> {
			val aux = this/negativeMax * -1
			return when {
				aux<-1f -> -1f
				else -> aux
			}
		}
		else -> {
			return 0f
		}
	}
}

/**
 *
 */
fun Float.percentageOf(currentPercentage: Float, positiveMax: Float, negativeMax: Float): Float {
	return when {
		this>0f -> {
			//If we are on the positive realm
			//We calculate the percentage of the remaining that we advance
			val remaining = 1f-currentPercentage
			val percentage = percentageOf(positiveMax, negativeMax)
			val aux = currentPercentage+(remaining*percentage)
			if(aux>1f) 1f else aux
		}
		this==0f -> currentPercentage
		else -> {
			//If we are on the negative realm
			//We calculate the percentage of the current that we advance backwards. Pretty weird to explain, but on DoubleExtensionsUnitTest you can see a few examples.
			val percentage = percentageOf(positiveMax, negativeMax)
			val aux = currentPercentage+(currentPercentage*percentage)
			if(aux<-1f) -1f else aux
		}
	}
}

/**
 * Percentage, where 1.0f is 100%, 0.0f is 0%, and -1 is 100% on the opposite direction. Caps at 1 and -1.
 */
fun Float.percentageOf(max: Float): Float {
	val aux = this/max
	return when {
		aux>1f -> 1f
		aux<-1f -> -1f
		else -> aux
	}
}