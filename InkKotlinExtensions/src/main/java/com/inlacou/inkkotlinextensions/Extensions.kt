package com.inlacou.inkkotlinextensions

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.math.pow

fun Boolean.inverse(): Boolean {
	return !this
}

val systemDecimalSeparator: String
	get() {
		val nf = NumberFormat.getInstance()
		return if (nf is DecimalFormat) {
			nf.decimalFormatSymbols.decimalSeparator.toString()
		}else {
			"."
		}
	}

val systemThousandSeparator: String
	get() {
		val nf = NumberFormat.getInstance()
		return if (nf is DecimalFormat) {
			when (nf.decimalFormatSymbols.decimalSeparator.toString()){
				"," -> "."
				else -> ","
			}
		}else {
			","
		}
	}

/**
 * 'lowerCamelCase' must (1) start with a lowercase letter and (2) the first letter of every new subsequent word has its first letter capitalized and is compounded with the previous word.
 *
 * An example of lower camel case of the variable camel case var is camelCaseVar.
 */
//fun String.toLowerCamelCase(): String {
	//TODO()
//}

/**
 * 'UpperCamelCase' must (1) start with an uppercase letter and (2) the first letter of every new subsequent word has its first letter capitalized and is compounded with the previous word.
 *
 * An example of upper camel case of the variable camel case var is CamelCaseVar.
 */
//fun String.toUpperLowerCase(): String {
	//TODO()
//}

/**
 * 'snake_case' is as simple as replacing all spaces with a "_" and lowercasing all the words. It's possible to snake_case and mix camelCase and PascalCase but imo, that ultimately defeats the purpose.
 *
 * An example of snake case of the variable snake case var is snake_case_var.
 */
//fun String.toSnakeCase(): String {
	//TODO()
//}

/**
 * 'kebab-case' is as simple as replacing all spaces with a "-" and lowercasing all the words. It's possible to kebab-case and mix camelCase and PascalCase but that ultimately defeats the purpose.
 *
 * An example of kebab case of the variable kebab case var is kebab-case-var.
 */
//fun String.toKebabCase(): String {
	//TODO()
//}

fun Int.toCharr(): String {
	return if (this in 1..26) (this + 64).toChar().toString() else ""
}

fun Long.millisToTimeLabel(): String {
	val seconds = this/1000
	val minutes = seconds/60
	val hours = minutes/60
	
	return "${hours.toStringMinDigits(2)}:${(minutes-hours*60).toStringMinDigits(2)}:${(seconds-minutes*60).toStringMinDigits(2)}"
}

/**
 * Generates a random number.
 * @param digits defines the target and the max digits, but not the actual number of digits.
 */
fun randomNumber(digits: Int): Int = (Math.random()*10.0.pow(digits.toDouble())).toInt()

/**
 * Generates a random string.
 * @param characters how many characters it will have
 */
fun randomText(characters: Int): String {
	val generator = Random()
	val randomStringBuilder = StringBuilder()
	val randomLength = (characters)
	var tempChar: Char
	for (i in 0 until randomLength) {
		tempChar = (generator.nextInt(96) + 32).toChar()
		randomStringBuilder.append(tempChar)
	}
	return randomStringBuilder.toString()
}

fun Int.colorToHex(): String {
	return String.format("#%06X", 0xFFFFFF and this)
}

