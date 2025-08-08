package com.inlacou.inkkotlinextensions

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.Normalizer
import java.util.*
import kotlin.collections.ArrayList

val String.digitsNum: Int
	get() = this.filter { it.toString().isNumeric() }.length

fun String.removeLast(c: Char): String = removeRange(lastIndexOf(c), lastIndexOf(c)+1)
fun String.removeLast(s: String): String = removeRange(lastIndexOf(s), lastIndexOf(s)+s.length)

fun String.deAccent(): String = Normalizer.normalize(this, Normalizer.Form.NFD).replace("\\p{Mn}+".toRegex(), "")

/**
 * Finds pair of given char. Only works with "|'|`|(|{|[
 * @param index of the pair start
 */
fun String.findPair(index: Int): Int {
	val allowedChars = listOf('"', '\'', '`', '´', '(', '{', '[')
	val givenChar = this[index]
	if (!allowedChars.contains(givenChar)) {
		throw IllegalArgumentException("Char $givenChar at given index $index is not in $allowedChars")
	}
	val pairChar = when(givenChar) {
		'('     ->  ')'
		'{'     ->  '}'
		'['     ->  ']'
		else -> givenChar
	}
	
	val aux = this.substring(index+1)
	return (aux.indexesOf(pairChar.toString()).map {
		Triple(it+index+1, aux[it], aux[it-1])
	}.first { it.third!='\\' }.first)
}

/**
 * Removes everything between given char and its pair. Only works with "|'|`|(|{|[
 * @param index of the pair start
 */
fun String.removePair(index: Int): String {
	return removeRange(index, findPair(index)+1)
}

/**
 * Gives indexes of given
 * @param string
 * starting from given
 * @param startIndex
 * @param ignoreCase whether to ignore case on given @param string
 */
fun CharSequence.indexesOf(string: String, startIndex: Int = 0, ignoreCase: Boolean = false): List<Int> {
	val indexes = mutableListOf<Int>()
	var lastIndex = indexOf(string, startIndex, ignoreCase)
	while (lastIndex!=-1) {
		indexes.add(lastIndex)
		lastIndex = indexOf(string, lastIndex+1, ignoreCase)
	}
	return indexes
}

fun String?.isAlpha(allowSpaces: Boolean = false): Boolean {
	return if(allowSpaces) this?.matches("^[ñÑa-zA-Z ]*$".toRegex()) ?: false
	else this?.matches("^[ña-zA-Z]*$".toRegex()) ?: false
}

fun String?.isAlphaNumeric(allowSpaces: Boolean = false): Boolean {
	return if(allowSpaces) this?.matches("^[ñÑa-zA-Z0-9 ]*$".toRegex()) ?: false
	else this?.matches("^[ña-zA-Z0-9]*$".toRegex()) ?: false
}

fun String?.isNumeric(): Boolean {
	return this?.matches("^[0-9]*$".toRegex()) ?: false
}

fun String.toSortableNumber(): String {
	var initial = this
	var response = ""
	while (initial.isNotEmpty()) {
		response += initial.first().toSortableNumber()
		initial = initial.removeRange(0, 1)
	}
	return if(response.isEmpty()) "0" else response
}

fun Char.toSortableNumber(): String {
	return when(this){
		'º' -> "100"
		'ª' -> "101"
		'!' -> "102"
		'|' -> "103"
		'\"' -> "104"
		'@' -> "105"
		'·' -> "106"
		'#' -> "107"
		'$' -> "108"
		'~' -> "109"
		'%' -> "110"
		'½' -> "111"
		'&' -> "112"
		'¬' -> "113"
		'/' -> "114"
		'{' -> "115"
		'}' -> "116"
		'<' -> "117"
		'>' -> "118"
		'(' -> "119"
		'[' -> "120"
		')' -> "121"
		']' -> "122"
		'.' -> "123"
		':' -> "124"
		',' -> "126"
		';' -> "127"
		'─' -> "128"
		'-' -> "129"
		'_' -> "130"
		'`' -> "131"
		'^' -> "132"
		'*' -> "133"
		'+' -> "134"
		'ç' -> "135"
		'ḉ' -> "136"
		'Ç' -> "137"
		'Ḉ' -> "138"
		'¨' -> "139"
		'´' -> "140"
		'\'' -> "141"
		'1' -> "142"
		'2' -> "143"
		'3' -> "144"
		'4' -> "145"
		'5' -> "146"
		'6' -> "147"
		'7' -> "148"
		'8' -> "149"
		'9' -> "150"
		'A', 'Á', 'À', 'a', 'á', 'à' -> "151"
		'B', 'b' -> "152"
		'C', 'c' -> "153"
		'D', 'd' -> "154"
		'E', 'É', 'È', 'e', 'é', 'è' -> "155"
		'F', 'f' -> "156"
		'G', 'g' -> "157"
		'H', 'h' -> "158"
		'I', 'Í', 'Ì', 'i', 'í', 'ì' -> "159"
		'J', 'j' -> "160"
		'K', 'k' -> "161"
		'L', 'l' -> "162"
		'M', 'm' -> "163"
		'N', 'n' -> "164"
		'Ñ', 'ñ' -> "165"
		'O', 'Ó', 'Ò', 'o', 'ó', 'ò' -> "166"
		'P', 'p' -> "167"
		'Q', 'q' -> "168"
		'R', 'r' -> "169"
		'S', 's' -> "170"
		'T', 't' -> "171"
		'U', 'Ú', 'Ù', 'u', 'ú', 'ù' -> "172"
		'V', 'v' -> "173"
		'W', 'w' -> "174"
		'X', 'x' -> "175"
		'Y', 'Ý', 'Ỳ', 'y', 'ý', 'ỳ' -> "176"
		'Z', 'z' -> "177"
		else -> "000"
	}
}

fun Char.isValidLetter(): Boolean {
	return validLetters.contains(this)
}

/**
 * Checks if given param is previous to the receiver. Works only with strings of the version format, like 1.2, 1.2.1, etc.
 * @param other
 * @return true if other version is higher than actual
 */
fun String.previousVersion(other: String): Boolean {
	val actual = this
	try {
		val st = StringTokenizer(actual.trim { it <= ' ' }, ".,")
		val st2 = StringTokenizer(other.trim { it <= ' ' }, ".,")

		//Pass to array
		val first = ArrayList<Int>()
		while (st.hasMoreTokens()) {
			first.add(Integer.parseInt(st.nextToken()))
		}

		//Pass to array
		val second = ArrayList<Int>()
		while (st2.hasMoreTokens()) {
			second.add(Integer.parseInt(st2.nextToken()))
		}

		//Longest array
		val max = if (first.size > second.size) first.size else second.size

		for (i in 0 until max) {
			val fromFirst = if (first.size > i) first[i] else 0
			val fromSecond = if (second.size > i) second[i] else 0
			if (fromFirst > fromSecond) {
				return false
			} else if (fromFirst < fromSecond) {
				return true
			}
		}
	} catch (e: Exception) {
		System.err.println(e.message)
	}

	return false
}

val validLetters = listOf(
	'A', 'Á', 'À', 'a', 'á', 'à',
	'B', 'b',
	'C', 'c',
	'D', 'd',
	'E', 'É', 'È', 'e', 'é', 'è',
	'F', 'f',
	'G', 'g',
	'H', 'h',
	'I', 'Í', 'Ì', 'i', 'í', 'ì',
	'J', 'j',
	'K', 'k',
	'L', 'l',
	'M', 'm',
	'N', 'n',
	'Ñ', 'ñ',
	'O', 'Ó', 'Ò', 'o', 'ó', 'ò',
	'P', 'p',
	'Q', 'q',
	'R', 'r',
	'S', 's',
	'T', 't',
	'U', 'Ú', 'Ù', 'u', 'ú', 'ù',
	'V', 'v',
	'W', 'w',
	'X', 'x',
	'Y', 'Ý', 'Ỳ', 'y', 'ý', 'ỳ',
	'Z', 'z')

//https://es.wikipedia.org/wiki/Separador_de_millares
/**
 * @param forceDecimals means if it should add 0s if not enough decimal digits
 */
fun String.formatDecimal(
	maxDecimals: Int,
	forceDecimals: Boolean = false,
	markThousands: Boolean = true,
	decimalSeparator: String = ",",
	thousandsSeparator: String = ".",
	roundingMode: RoundingMode = RoundingMode.FLOOR,
	onDecimalsLimited: (() -> Unit)? = null
): String {
	val secret = "%&%&lñjkndfkljWEkljkjsdgrkbnldflkjnjzdgKJFELBEUEW"
	if (trim().isEmpty() or trim().equals("nan", ignoreCase = true) or trim().equals("null", ignoreCase = true)) {
		return this
	}

	var curated = this.replaceFirst(decimalSeparator, secret).replace(decimalSeparator, "")

	val sign = if(curated.substring(0,1)=="-"){
		curated = curated.replace("-", "")
		"-"
	}else{
		""
	}

	curated = curated.replace(" ", "")
	curated = curated.replace(".", "")
	curated = curated.replace(",", "")
	curated = curated.replace(secret, ".")

	var bd = BigDecimal(curated)
	bd = bd.setScale(maxDecimals, roundingMode)
	val value = bd.toString().replace(".", decimalSeparator)

	val separatorPosition = value.lastIndexOf(decimalSeparator)

	try {
		//Integer part
		var integerPart = (value.substring(0, if (separatorPosition == -1) value.length else separatorPosition))
		//Decimal part
		var decimalPart = when {
			separatorPosition == -1 -> null
			separatorPosition + 1 < curated.length -> curated.substring(
				separatorPosition + 1,
				if (separatorPosition + 1 + maxDecimals < curated.length) {
					onDecimalsLimited?.invoke()
					separatorPosition + 1 + maxDecimals
				}
				else curated.length)
			else -> ""
		}

		if(forceDecimals){
			if(decimalPart==null){
				decimalPart = ""
			}
			while (decimalPart.length<maxDecimals){
				decimalPart += "0"
			}
		}

		val newThousandsSeparator = if(thousandsSeparator!=decimalSeparator){
			thousandsSeparator
		}else{
			when(decimalSeparator){
				"," -> "."
				"." -> ","
				else -> ","
			}
		}

		if(markThousands) {
			var lastDot = integerPart.length
			while (lastDot - 3 > 0) {
				val first = integerPart.substring(0, lastDot - 3)
				val second = integerPart.substring(lastDot - 3, integerPart.length)
				integerPart = "$first$newThousandsSeparator$second"
				lastDot -= 3
			}
		}

		return when {
			forceDecimals && decimalPart!="" -> sign + integerPart + decimalSeparator + decimalPart
			forceDecimals && maxDecimals==0 -> sign + integerPart
			!contains(decimalSeparator) || maxDecimals == 0 -> sign + integerPart
			decimalPart == "" -> sign + integerPart + decimalSeparator
			else -> sign + integerPart + decimalSeparator + decimalPart
		}
		/*
		return if (forceDecimals) value
		else if (!contains(decimalSeparator) || maxDecimals == 0) integerPart.toString()
		else if (decimalPart == "") integerPart.toString() + decimalSeparator
		else integerPart.toString() + decimalSeparator + decimalPart
		*/
	}catch (nfe: NumberFormatException){
		return ""
	}
}

internal fun <T> Array<T>.toStringWithIndentation(tabLevel: Int = 0): String = toList().toStringWithIndentation(tabLevel)

/**
 * Used to convert a [List] to [String] giving more indentation to each item.
 * If the item is a multiline [String], it will do even more indentation for each line of it.
 *
 * @receiver [List]<[T]> to convert to [String]
 *
 * @param [tabLevel] [Int] to start with
 *
 * @return [String]
 */
internal fun <T> Collection<T>.toStringWithIndentation(tabLevel: Int = 0): String {
	if (this.isEmpty()) return "[]"
	var result = if (this.first() !is String) "["
	else ""
	forEach {
		result +=
			if (it !is String && it.toString().lines().size > 1) it.toString().lines().toStringWithIndentation(tabLevel + 1)
			else "\n${"\t" * (tabLevel + 1)}$it,"
	}
	result = result.dropLast(1) // Remove the trailing comma
	return if (this.isNotEmpty() && this.first() !is String) "$result\n${"\t" * tabLevel}]"
	else result
}

/**
 * Used to multiply a [String] [n] times.
 *
 * @return [String]
 */
internal operator fun String.times(n: Int): String {
	var result = this
	repeat(n) { result += result }
	return result
}

fun String.fromTo(index1: Int, index2: Int): String {
	return this.substring(index1, index2)
}

fun String.removeFromTo(index1: Int, index2: Int): String {
	return this.substring(0, index1)+this.substring(index2)
}
