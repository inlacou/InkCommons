package com.inlacou.inkkotlinextensions

import java.util.*

/**
 * Transforms each byte to:
 * radix 10 -> it's decimal value
 * radix 8 -> it's octal value
 * radix 16 -> it's hex value
 */
fun ByteArray.toStringsRadix(radix: Int = 10): String {
	return map { it.toString(radix) }.toString().replace(", ", "").replace("[", "").replace("]", "").toUpperCase(Locale.ROOT)
}

/**
 * Trasforms every byte to it's corresponding Char value
 */
fun ByteArray.toChars(): String {
	return map { it.toChar() }.toString().replace(", ", "").replace("[", "").replace("]", "")
}

/**
 * Trasforms every byte to it's corresponding Char value
 */
fun Array<Byte>.toChars(): String {
	return map { it.toChar() }.toString().replace(", ", "").replace("[", "").replace("]", "")
}

fun ByteArray.toStringIn(): String {
	return String(this)
}

fun ByteArray.toPositiveInts(): String {
	return map { it.toPositiveInt() }.toString().replace(", ", "").replace("[", "").replace("]", "")
}

fun ByteArray.toMultipleOf(multiple: Int): ByteArray {
	var aux = this
	
	while(aux.size%multiple!=0){  //Maybe optimize with diff instead of while?
		aux = byteArrayOf('0'.toByte()).plus(aux)
	}
	
	return aux
}

fun ByteArray.toJvmMultipleOf(multiple: Int): Array<Byte> {
	var aux = this
	
	while(aux.size%multiple!=0){  //Maybe optimize with diff instead of while?
		aux = byteArrayOf('0'.toByte()).plus(aux)
	}
	
	return aux.toTypedArray()
}

fun String.hexToByteArray() : ByteArray {
	val result = ByteArray(length / 2)
	for (i in 0 until length step 2) {
		result[i.shr(1)] = (this[i].toString()+this[i+1]).hexToByte()
	}
	return result
}

fun ByteArray.toHex(separator: String = "") : String{
	val result = StringBuffer()
	
	forEach { result.append(it.toHex()+separator) }
	
	return result.toString()
}