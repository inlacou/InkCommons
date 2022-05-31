package com.inlacou.inkkotlinextensions.extensions

import com.inlacou.inkkotlinextensions.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class HexExtensionsTests {
	@Test fun `hex F is 16 dec`() = assertEquals(15, "F".hexToDecimal())
	@Test fun `hex A is 10 dec`() = assertEquals(10, "A".hexToDecimal())
	@Test fun `hex 5 is 5 dec`() = assertEquals(5, "5".hexToDecimal())
	@Test fun `hex 11 is 16 dec`() = assertEquals(17, "11".hexToDecimal())
	@Test fun `hex 1F is 33 dec`() = assertEquals(31, "1F".hexToDecimal())

	@Test fun `hex F is 16 dec and back`() = assertEquals("0F", "F".hexToDecimal().decimalToHex())
	@Test fun `hex A is 10 dec and back`() = assertEquals("0A", "A".hexToDecimal().decimalToHex())
	@Test fun `hex 5 is 5 dec and back`() = assertEquals("05", "5".hexToDecimal().decimalToHex())
	@Test fun `hex 11 is 16 dec and back`() = assertEquals("11", "11".hexToDecimal().decimalToHex())
	@Test fun `hex 1F is 33 dec and back`() = assertEquals("1F", "1F".hexToDecimal().decimalToHex())

	@Test fun `hex FFFFFF to grayscale default`() = assertEquals("FFFFFF", "FFFFFF".hexColorToGrayScale())
	@Test fun `hex FFFFFF to grayscale average`() = assertEquals("FFFFFF", "FFFFFF".hexColorToGrayScale(GrayScaleAlgorithm.AVERAGE))
	@Test fun `hex FFFFFF to grayscale lightness`() = assertTrue("FFFFFF".hexColorToGrayScale(GrayScaleAlgorithm.LIGHTNESS).hexToDecimal()<="FFFFFF".hexToDecimal())
	@Test fun `hex FFFFFF to grayscale luminosity`() = assertTrue("FFFFFF".hexColorToGrayScale(GrayScaleAlgorithm.LUMINOSITY).hexToDecimal()<"FFFFFF".hexToDecimal())
	@Test fun `hex 999999 to grayscale default`() = assertEquals("999999", "999999".hexColorToGrayScale())
	@Test fun `hex 999999 to grayscale average`() = assertEquals("999999", "999999".hexColorToGrayScale(GrayScaleAlgorithm.AVERAGE))
	@Test fun `hex 999999 to grayscale lightness`() = assertTrue("999999".hexColorToGrayScale(GrayScaleAlgorithm.LIGHTNESS).hexToDecimal()<="999999".hexToDecimal())
	@Test fun `hex 999999 to grayscale luminosity`() = assertTrue("999999".hexColorToGrayScale(GrayScaleAlgorithm.LUMINOSITY).hexToDecimal()<"999999".hexToDecimal())
	@Test fun `hex 00FF00 to grayscale default`() = assertTrue("00FF00".hexColorToGrayScale().substring(2,4).hexToDecimal()<"FF".hexToDecimal())
	@Test fun `hex 00FF00 to grayscale average`() = assertTrue("00FF00".hexColorToGrayScale(GrayScaleAlgorithm.AVERAGE).substring(2,4).hexToDecimal()<"FF".hexToDecimal())
	@Test fun `hex 00FF00 to grayscale lightness`() = assertTrue("00FF00".hexColorToGrayScale(GrayScaleAlgorithm.LIGHTNESS).substring(2,4).hexToDecimal()<"FF".hexToDecimal())
	@Test fun `hex 00FF00 to grayscale luminosity`() = assertTrue("00FF00".hexColorToGrayScale(GrayScaleAlgorithm.LUMINOSITY).substring(2,4).hexToDecimal()<"FF".hexToDecimal())
	@Test fun `hex 009900 to grayscale default`() = assertTrue("009900".hexColorToGrayScale().substring(2,4).hexToDecimal()<"99".hexToDecimal())
	@Test fun `hex 009900 to grayscale average`() = assertTrue("009900".hexColorToGrayScale(GrayScaleAlgorithm.AVERAGE).substring(2,4).hexToDecimal()<"99".hexToDecimal())
	@Test fun `hex 009900 to grayscale lightness`() = assertTrue("009900".hexColorToGrayScale(GrayScaleAlgorithm.LIGHTNESS).substring(2,4).hexToDecimal()<"99".hexToDecimal())
	@Test fun `hex 009900 to grayscale luminosity`() = assertTrue("009900".hexColorToGrayScale(GrayScaleAlgorithm.LUMINOSITY).substring(2,4).hexToDecimal()<"99".hexToDecimal())
	
	@Test fun `9 to hex color is 000009`() = assertEquals("#000009", 9.colorToHex())
	@Test fun `255 to hex color is 00000FF`() = assertEquals("#0000FF", 255.colorToHex())
}
