package com.inlacou.inkkotlinextensions

import java.lang.NumberFormatException
import java.math.RoundingMode
import java.math.RoundingMode.*
import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.pow

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
	df.roundingMode = FLOOR
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

fun Float.changeValueFromOneRangeToAnother(oldMin: Float, oldMax: Float, newMin: Float, newMax: Float): Float {
	val oldValue = this
	val newRange = (newMax - newMin)
	val oldRange = (oldMax - oldMin)
	return  if (oldRange == 0f) newMin
	else (((oldValue - oldMin) * newRange) / oldRange) + newMin
}

/**
 * @param roundingMode:
 * CEILING: Rounding mode to round towards positive infinity. For positive values this rounding mode behaves as UP, for negative values as DOWN. Rule: x.round() >= x
 * FLOOR: Rounding mode to round towards negative infinity. For positive values this rounding mode behaves as DOWN, for negative values as UP. Rule: x.round() <= x
 * UP: Rounding mode where positive values are rounded towards positive infinity and negative values towards negative infinity. Rule: x.round().abs() >= x.abs()
 * DOWN: Rounding mode where the values are rounded towards zero. Rule: x.round().abs() <= x.abs()
 * HALF_DOWN: Rounding mode where values are rounded towards the nearest neighbor. Ties are broken by rounding down.
 * HALF_EVEN: Rounding mode where values are rounded towards the nearest neighbor. Ties are broken by rounding to the even neighbor.
 * HALF_UP: Rounding mode where values are rounded towards the nearest neighbor. Ties are broken by rounding up.
 * UNNECESSARY: Rounding mode where the rounding operations throws an ArithmeticException for the case that rounding is necessary, i.e. for the case that the value cannot be represented exactly.
 *
 * @param desiredDecimals if less than 0 will be treated as 0.
 */
fun Float.roundDecimals(desiredDecimals: Int, roundingMode: RoundingMode = DOWN): Float {
	val realDesiredDecimals = if(desiredDecimals<0) 0 else desiredDecimals

	val realRoundingMode = when(roundingMode) {
		CEILING -> if(this>0) UP else DOWN
		FLOOR -> if(this>0) DOWN else UP
		else -> roundingMode
	}

	val integerPart = this.toLong()
	var s = this.toString()
	s = s.substring(s.indexOf(".")+1, s.length)
	val foundDecimals = s.length
	var decimalPart = s.toLong()

	/* Check if necessary */
	if(realDesiredDecimals>=foundDecimals) {
		/* Not necessary */
		return this
	} else {
		decimalPart = decimalPart.take(realDesiredDecimals)
		val roundChar = s.take(realDesiredDecimals+1).last().digitToInt()
		when(realRoundingMode) {
			UP -> decimalPart += 1
			DOWN -> { /*default, do nothing */ }
			CEILING -> decimalPart += 1
			FLOOR -> { /*default, do nothing */ }
			HALF_UP -> if(roundChar==5) decimalPart += 1
			HALF_DOWN -> { /*default, do nothing */ }
			HALF_EVEN -> if(roundChar==5 && decimalPart.last()%2!=0) decimalPart += 1
			UNNECESSARY -> throw ArithmeticException("Rounding mode UNNECESSARY specified but rounding found necessary for current number $this with $desiredDecimals target decimals")
		}
		val sign = if(this<0) -1 else 1
		return if(realDesiredDecimals!=0) integerPart+(decimalPart.toFloat()/10f.pow(realDesiredDecimals))*sign
		else integerPart.toFloat()
	}
}

/**
 * @param roundingMode:
 * CEILING: Rounding mode to round towards positive infinity. For positive values this rounding mode behaves as UP, for negative values as DOWN. Rule: x.round() >= x
 * FLOOR: Rounding mode to round towards negative infinity. For positive values this rounding mode behaves as DOWN, for negative values as UP. Rule: x.round() <= x
 * UP: Rounding mode where positive values are rounded towards positive infinity and negative values towards negative infinity. Rule: x.round().abs() >= x.abs()
 * DOWN: Rounding mode where the values are rounded towards zero. Rule: x.round().abs() <= x.abs()
 * HALF_DOWN: Rounding mode where values are rounded towards the nearest neighbor. Ties are broken by rounding down.
 * HALF_EVEN: Rounding mode where values are rounded towards the nearest neighbor. Ties are broken by rounding to the even neighbor.
 * HALF_UP: Rounding mode where values are rounded towards the nearest neighbor. Ties are broken by rounding up.
 * UNNECESSARY: Rounding mode where the rounding operations throws an ArithmeticException for the case that rounding is necessary, i.e. for the case that the value cannot be represented exactly.
 *
 * @param desiredDecimals if less than 0 will be treated as 0.
 */
fun Double.roundDecimals(desiredDecimals: Int, roundingMode: RoundingMode = DOWN): Double {
	val realDesiredDecimals = if(desiredDecimals<0) 0 else desiredDecimals

	val realRoundingMode = when(roundingMode) {
		CEILING -> if(this>0) UP else DOWN
		FLOOR -> if(this>0) DOWN else UP
		else -> roundingMode
	}

	val integerPart = this.toLong()
	var s = this.toString()
	s = s.substring(s.indexOf(".")+1, s.length)
	val foundDecimals = s.length
	var decimalPart = s.toLong()
	println("$this | $integerPart | $decimalPart")

	/* Check if necessary */
	if(realDesiredDecimals>=foundDecimals) {
		/* Not necessary */
		return this
	} else {
		decimalPart = decimalPart.take(realDesiredDecimals)
		val roundChar = s.take(realDesiredDecimals+1).last().digitToInt()
		when(realRoundingMode) {
			UP -> decimalPart += 1
			DOWN -> { /*default, do nothing */ }
			CEILING -> decimalPart += 1
			FLOOR -> { /*default, do nothing */ }
			HALF_UP -> if(roundChar==5) decimalPart += 1
			HALF_DOWN -> { /*default, do nothing */ }
			HALF_EVEN -> if(roundChar==5 && decimalPart.last()%2!=0) decimalPart += 1
			UNNECESSARY -> throw ArithmeticException("Rounding mode UNNECESSARY specified but rounding found necessary for current number $this with $desiredDecimals target decimals")
		}
		val sign = if(this<0) -1 else 1
 		return if(realDesiredDecimals!=0) integerPart+(decimalPart.toDouble()/10f.pow(realDesiredDecimals))*sign
		else integerPart.toDouble()
	}
}

fun Long.take(itemsNumber: Int) = try { this.toString().take(itemsNumber).toLong() } catch (nfe: NumberFormatException) { 0 }
fun Long.last() = try { this.toString().last().digitToInt() } catch (nfe: NumberFormatException) { 0 }