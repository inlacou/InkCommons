package com.inlacou.inkkotlinextensions.extensions

import com.inlacou.inkkotlinextensions.*
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test

class ByteArrayExtensionsTests {
	@Test fun `char A to byte 41`() = assertEquals(0x41.toByte(), 'A'.toByte())
	@Test fun `char B to byte 42`() = assertEquals(0x42.toByte(), 'B'.toByte())
	@Test fun `char C to byte 43`() = assertEquals(0x43.toByte(), 'C'.toByte())
	@Test fun `bytes 41, 42 and 43 to chars A B C`() = assertEquals("ABC", arrayOf(0x41.toByte(), 0x42.toByte(), 0x43.toByte()).toChars())
	@Test fun `chars A B C to bytes to chars A B C`() = assertEquals("ABC", arrayOf('A'.toByte(), 'B'.toByte(), 'C'.toByte()).toChars())
	@Test fun `bytes 41, 42 and 43 to chars A B C to bytes to chars`() = assertEquals(arrayOf('A'.toByte(), 'B'.toByte(), 'C'.toByte()).toChars(), arrayOf(0x41.toByte(), 0x42.toByte(), 0x43.toByte()).toChars())
	@Test fun `byteArray 41, 42 and 43 to chars A B C`() = assertEquals("ABC", arrayOf(0x41.toByte(), 0x42.toByte(), 0x43.toByte()).toByteArray().toChars())
	@Test fun `chars A B C to byteArray to chars A B C`() = assertEquals("ABC", arrayOf('A'.toByte(), 'B'.toByte(), 'C'.toByte()).toByteArray().toChars())
	@Test fun `byteArray 41, 42 and 43 to chars A B C to bytes to chars`() = assertEquals(arrayOf('A'.toByte(), 'B'.toByte(), 'C'.toByte()).toChars(), arrayOf(0x41.toByte(), 0x42.toByte(), 0x43.toByte()).toByteArray().toChars())
	@Test fun `byteArray 41, 42 and 43 to chars A B C to byteArray to chars`() = assertEquals(arrayOf('A'.toByte(), 'B'.toByte(), 'C'.toByte()).toByteArray().toChars(), arrayOf(0x41.toByte(), 0x42.toByte(), 0x43.toByte()).toByteArray().toChars())
	
	@Test fun `chars A B Z to byte to byteArray to string ABZ`() = assertEquals("ABZ", arrayOf('A'.toByte(), 'B'.toByte(), 'Z'.toByte()).toByteArray().toStringIn())
	@Test fun `chars A B Z to byte to byteArray to decimal 65 66 90 (mode 1)`() = assertEquals("656690", arrayOf('A'.toByte(), 'B'.toByte(), 'Z'.toByte()).toByteArray().toPositiveInts())
	@Test fun `chars A B Z to byte to byteArray to decimal 65 66 90 (mode 2)`() = assertEquals("656690", arrayOf('A'.toByte(), 'B'.toByte(), 'Z'.toByte()).toByteArray().toStringsRadix())
	@Test fun `chars A B Z to byte to byteArray to octal 101 102 132`() = assertEquals("101102132", arrayOf('A'.toByte(), 'B'.toByte(), 'Z'.toByte()).toByteArray().toStringsRadix(8))
	@Test fun `chars A B Z to byte to byteArray to hex 41 42 5a (mode 1)`() = assertEquals("41425A", arrayOf('A'.toByte(), 'B'.toByte(), 'Z'.toByte()).toByteArray().toStringsRadix(16))
	@Test fun `chars A B Z to byte to byteArray to hex 41 42 5a (mode 2)`() = assertEquals("41425A", arrayOf('A'.toByte(), 'B'.toByte(), 'Z'.toByte()).toByteArray().toHex())
	@Test fun `String 41 42 5a to byteArray equals A B Z as byte to byteArray `() = assertEquals("41425A".hexToByteArray().toHex(), arrayOf('A'.toByte(), 'B'.toByte(), 'Z'.toByte()).toByteArray().toHex())
	
	@Test fun `apply multiplier 1 to ABZ (for example 2, so it was even number of digits) then to char) so 3 digits`() = assertEquals(arrayOf('A'.toByte(), 'B'.toByte(), 'Z'.toByte()).toByteArray().toMultipleOf(1).toChars(), "ABZ")
	@Test fun `apply multiplier 2 to ABZ (for example 2, so it was even number of digits) then to char) so 4 digits`() = assertEquals(arrayOf('A'.toByte(), 'B'.toByte(), 'Z'.toByte()).toByteArray().toMultipleOf(2).toChars(), "0ABZ")
	@Test fun `apply multiplier 3 to ABZ (for example 2, so it was even number of digits) then to char) so 3 digits`() = assertEquals(arrayOf('A'.toByte(), 'B'.toByte(), 'Z'.toByte()).toByteArray().toMultipleOf(3).toChars(), "ABZ")
	@Test fun `apply multiplier 4 to ABZ (for example 2, so it was even number of digits) then to char) so 4 digits`() = assertEquals(arrayOf('A'.toByte(), 'B'.toByte(), 'Z'.toByte()).toByteArray().toMultipleOf(4).toChars(), "0ABZ")
	@Test fun `apply multiplier 5 to ABZ (for example 2, so it was even number of digits) then to char) so 5 digits`() = assertEquals(arrayOf('A'.toByte(), 'B'.toByte(), 'Z'.toByte()).toByteArray().toMultipleOf(5).toChars(), "00ABZ")
	
	@Test fun `apply multiplier 1 and transformation to array of Byte to ABZ (for example 2, so it has even number of digits) then to char) so 3 digits`() = assertEquals(arrayOf('A'.toByte(), 'B'.toByte(), 'Z'.toByte()).toByteArray().toJvmMultipleOf(1).toChars(), "ABZ")
	@Test fun `apply multiplier 2 and transformation to array of Byte to ABZ (for example 2, so it has even number of digits) then to char) so 4 digits`() = assertEquals(arrayOf('A'.toByte(), 'B'.toByte(), 'Z'.toByte()).toByteArray().toJvmMultipleOf(2).toChars(), "0ABZ")
	@Test fun `apply multiplier 3 and transformation to array of Byte to ABZ (for example 2, so it has even number of digits) then to char) so 3 digits`() = assertEquals(arrayOf('A'.toByte(), 'B'.toByte(), 'Z'.toByte()).toByteArray().toJvmMultipleOf(3).toChars(), "ABZ")
	@Test fun `apply multiplier 4 and transformation to array of Byte to ABZ (for example 2, so it has even number of digits) then to char) so 4 digits`() = assertEquals(arrayOf('A'.toByte(), 'B'.toByte(), 'Z'.toByte()).toByteArray().toJvmMultipleOf(4).toChars(), "0ABZ")
	@Test fun `apply multiplier 5 and transformation to array of Byte to ABZ (for example 2, so it has even number of digits) then to char) so 5 digits`() = assertEquals(arrayOf('A'.toByte(), 'B'.toByte(), 'Z'.toByte()).toByteArray().toJvmMultipleOf(5).toChars(), "00ABZ")

	@Test fun `Byte toLong equals Byte toStringRadix(10) toLong with char A`() = assertEquals('A'.toByte().toLong(), arrayOf('A'.toByte()).toByteArray().toStringsRadix(10).toLong())
	@Test fun `Byte toLong equals Byte toStringRadix(10) toLong with char B`() = assertEquals('B'.toByte().toLong(), arrayOf('B'.toByte()).toByteArray().toStringsRadix(10).toLong())
	@Test fun `Byte toLong equals Byte toStringRadix(10) toLong with char C`() = assertEquals('C'.toByte().toLong(), arrayOf('C'.toByte()).toByteArray().toStringsRadix(10).toLong())
	@Test fun `Byte toLong equals Byte toStringRadix(10) toLong with char D`() = assertEquals('D'.toByte().toLong(), arrayOf('D'.toByte()).toByteArray().toStringsRadix(10).toLong())
	@Test fun `Byte toLong equals Byte toStringRadix(10) toLong with char Z`() = assertEquals('Z'.toByte().toLong(), arrayOf('Z'.toByte()).toByteArray().toStringsRadix(10).toLong())
	@Test fun `Byte toLong equals Byte toStringRadix(10) toLong with char z`() = assertEquals('z'.toByte().toLong(), arrayOf('z'.toByte()).toByteArray().toStringsRadix(10).toLong())
}
