package com.inlacou.inkkotlinextensions.extensions

import com.inlacou.inkkotlinextensions.*
import org.junit.Assert
import org.junit.jupiter.api.Test

class ByteExtensionsTests {
	@Test fun `hex 00 to byte 00`() = Assert.assertEquals(0x00.toByte(), "00".hexToByte())
	@Test fun `hex 0A to byte 0A`() = Assert.assertEquals(0x0A.toByte(), "0A".hexToByte())
	@Test fun `hex 0B to byte 0B`() = Assert.assertEquals(0x0B.toByte(), "0B".hexToByte())
	@Test fun `hex 0C to byte 0C`() = Assert.assertEquals(0x0C.toByte(), "0C".hexToByte())
	@Test fun `hex 0D to byte 0D`() = Assert.assertEquals(0x0D.toByte(), "0D".hexToByte())
	@Test fun `hex 0E to byte 0E`() = Assert.assertEquals(0x0E.toByte(), "0E".hexToByte())
	@Test fun `hex 0F to byte 0F`() = Assert.assertEquals(0x0F.toByte(), "0F".hexToByte())
	@Test fun `hex F0 to byte F0`() = Assert.assertEquals(0xF0.toByte(), "F0".hexToByte())
	@Test fun `hex FA to byte FA`() = Assert.assertEquals(0xFA.toByte(), "FA".hexToByte())
	@Test fun `hex FB to byte FB`() = Assert.assertEquals(0xFB.toByte(), "FB".hexToByte())
	@Test fun `hex FC to byte FC`() = Assert.assertEquals(0xFC.toByte(), "FC".hexToByte())
	@Test fun `hex FD to byte FD`() = Assert.assertEquals(0xFD.toByte(), "FD".hexToByte())
	@Test fun `hex FE to byte FE`() = Assert.assertEquals(0xFE.toByte(), "FE".hexToByte())
	@Test fun `hex FF to byte FF`() = Assert.assertEquals(0xFF.toByte(), "FF".hexToByte())
	
	@Test fun `byte 00 to hex 00`() = Assert.assertEquals("00", 0x00.toByte().toHex())
	@Test fun `byte 0A to hex 0A`() = Assert.assertEquals("0A", 0x0A.toByte().toHex())
	@Test fun `byte 0B to hex 0B`() = Assert.assertEquals("0B", 0x0B.toByte().toHex())
	@Test fun `byte 0C to hex 0C`() = Assert.assertEquals("0C", 0x0C.toByte().toHex())
	@Test fun `byte 0D to hex 0D`() = Assert.assertEquals("0D", 0x0D.toByte().toHex())
	@Test fun `byte 0E to hex 0E`() = Assert.assertEquals("0E", 0x0E.toByte().toHex())
	@Test fun `byte 0F to hex 0F`() = Assert.assertEquals("0F", 0x0F.toByte().toHex())
	@Test fun `byte F0 to hex F0`() = Assert.assertEquals("F0", 0xF0.toByte().toHex())
	@Test fun `byte FA to hex FA`() = Assert.assertEquals("FA", 0xFA.toByte().toHex())
	@Test fun `byte FB to hex FB`() = Assert.assertEquals("FB", 0xFB.toByte().toHex())
	@Test fun `byte FC to hex FC`() = Assert.assertEquals("FC", 0xFC.toByte().toHex())
	@Test fun `byte FD to hex FD`() = Assert.assertEquals("FD", 0xFD.toByte().toHex())
	@Test fun `byte FE to hex FE`() = Assert.assertEquals("FE", 0xFE.toByte().toHex())
	@Test fun `byte FF to hex FF`() = Assert.assertEquals("FF", 0xFF.toByte().toHex())
	
	@Test fun `binary 0000 0000 to byte 00`() = Assert.assertEquals(0x00.toByte(), "00000000".binaryToByte())
	@Test fun `binary 0000 1111 to byte 0F`() = Assert.assertEquals(0x0F.toByte(), "00001111".binaryToByte())
	@Test fun `binary 1111 1111 to byte FF`() = Assert.assertEquals(0xFF.toByte(), "11111111".binaryToByte())
	
	@Test fun `binary 0000 0000 to hex 00`() = Assert.assertEquals("00", "00000000".binaryToHex())
	@Test fun `binary 0000 1111 to hex 0F`() = Assert.assertEquals("0F", "00001111".binaryToHex())
	@Test fun `binary 1111 1111 to hex FF`() = Assert.assertEquals("FF", "11111111".binaryToHex())
	
	@Test fun `binary 0000 0000 to decimal 0`() = Assert.assertEquals(0, 0x00.toByte().toPositiveInt())
	@Test fun `binary 0000 1111 to decimal 15`() = Assert.assertEquals(15, 0x0F.toByte().toPositiveInt())
	@Test fun `binary 1111 1111 to decimal 255`() = Assert.assertEquals(255, 0xFF.toByte().toPositiveInt())
	
	@Test fun `decimal 0 to hex 00`() = Assert.assertEquals("00", 0.decimalToHex())
	@Test fun `decimal 10 to hex 0A`() = Assert.assertEquals("0A", 10.decimalToHex())
	@Test fun `decimal 15 to hex 0F`() = Assert.assertEquals("0F", 15.decimalToHex())
	@Test fun `decimal 26 to hex 1A`() = Assert.assertEquals("1A", 26.decimalToHex())
	@Test fun `decimal 186 to hex B0`() = Assert.assertEquals("B0", 176.decimalToHex())
	@Test fun `decimal 192 to hex C0`() = Assert.assertEquals("C0", 192.decimalToHex())
	@Test fun `decimal 208 to hex D0`() = Assert.assertEquals("D0", 208.decimalToHex())
	@Test fun `decimal 224 to hex E0`() = Assert.assertEquals("E0", 224.decimalToHex())
	@Test fun `decimal 240 to hex F0`() = Assert.assertEquals("F0", 240.decimalToHex())
	@Test fun `decimal 255 to hex FF`() = Assert.assertEquals("FF", 255.decimalToHex())
	
	@Test fun `decimal 0 to byte 00`() = Assert.assertEquals(0x00.toByte(), 0.decimalToByte())
	@Test fun `decimal 10 to byte 0A`() = Assert.assertEquals(0x0A.toByte(), 10.decimalToByte())
	@Test fun `decimal 15 to byte 0F`() = Assert.assertEquals(0x0F.toByte(), 15.decimalToByte())
	@Test fun `decimal 26 to byte 1A`() = Assert.assertEquals(0x1A.toByte(), 26.decimalToByte())
	@Test fun `decimal 186 to byte B0`() = Assert.assertEquals(0xB0.toByte(), 176.decimalToByte())
	@Test fun `decimal 192 to byte C0`() = Assert.assertEquals(0xC0.toByte(), 192.decimalToByte())
	@Test fun `decimal 208 to byte D0`() = Assert.assertEquals(0xD0.toByte(), 208.decimalToByte())
	@Test fun `decimal 224 to byte E0`() = Assert.assertEquals(0xE0.toByte(), 224.decimalToByte())
	@Test fun `decimal 240 to byte F0`() = Assert.assertEquals(0xF0.toByte(), 240.decimalToByte())
	@Test fun `decimal 255 to byte FF`() = Assert.assertEquals(0xFF.toByte(), 255.decimalToByte())
}
