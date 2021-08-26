package com.inlacou.inkkotlinextensions

import java.util.*

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
	val firstIndex = (octet and 0xF0).ushr(4)
	val secondIndex = octet and 0x0F
	return "${HEX_CHARS[firstIndex]}${HEX_CHARS[secondIndex]}"
}

fun Int.decimalToHex(): String {
	var result = toString(16)
	if(result.length%2!=0) result = "0$result"
	return result.toUpperCase(Locale.ROOT)
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
