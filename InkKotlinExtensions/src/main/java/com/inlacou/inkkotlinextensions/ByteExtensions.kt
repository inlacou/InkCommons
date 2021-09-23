package com.inlacou.inkkotlinextensions

fun Byte.toPositiveInt() = toInt() and 0xFF

private val HEX_CHARS = "0123456789ABCDEF".toCharArray()

fun String.hexToByte(): Byte {
	val value = if(this.length%2==0) this else "0$this"
	val firstIndex = HEX_CHARS.indexOf(value[0])
	val secondIndex = HEX_CHARS.indexOf(value[1])
	
	return firstIndex.shl(4).or(secondIndex).toByte()
}

fun Byte.toHex(): String {
	val octet = toInt()
	return "${HEX_CHARS[(octet and 0xF0).ushr(4)]}${HEX_CHARS[octet and 0x0F]}"
}

fun Int.decimalToHex(): String {
	var result = toString(16)
	if(result.length%2!=0 && result[0] !='-') result = "0$result" //Keep it in pairs, 00 0F FF and so on
	return result.uppercase()
}

fun String.binaryToHex(): String {
	var result = String.format("%21X", java.lang.Long.parseLong(this, 2)).trim()
	val desiredResultDigits = this.length/4
	return if(result.length==desiredResultDigits) {
		result
	} else {
		while (result.length<desiredResultDigits){
			result = "0$result"
		}
		result
	}
}

fun Int.decimalToByte(): Byte {
	return decimalToHex().hexToByte()
}

fun String.binaryToByte(): Byte {
	return binaryToHex().hexToByte()
}

/**
 * For when the int value goes from -128 to 127 with the following progression: Goes from 0 to 127 and then continues from -128 to -1. Examples:
 *    00 ->	 0
 *    01 ->	 1
 *    02 ->	 2
 *    79 ->	 127
 *    80 ->	-128
 *    FE ->	-2
 *    FF ->	-1
 */
fun Int.decimalNegativableToByte(): Byte {
	return when {
		this>127    -> throw IllegalArgumentException()
		this<-128   -> throw IllegalArgumentException()
		this<0      -> (this+256).decimalToByte()
		else        -> this.decimalToByte()
	}
}

/**
 * Inverse of @see Int.decimalNegativableToByte()
 */
fun Byte.byteToDecimalNegativable(): Int {
	val aux = this.toHex().hexToDecimal()
	return when {
		aux<128 -> aux
		else    -> aux-256
	}
}
