package com.inlacou.inkkotlinextensions.extensions

import com.inlacou.inkkotlinextensions.*
import org.junit.Assert.*
import org.junit.jupiter.api.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class StringExtensionsTest {

	@Test fun `1dot2dot0 previous version to 1dot2dot1`() = assertTrue("1.2.0".previousVersion("1.2.1"))
	@Test fun `1dot2dot0 previous version to 1dot2dot1dot0`() = assertTrue("1.2.0".previousVersion("1.2.1.0"))
	@Test fun `1dot2dot0 previous version to 1dot2dot0dot1`() = assertTrue("1.2.0".previousVersion("1.2.0.1"))
	@Test fun `1dot2dot0dot0 previous version to 1dot2dot0dot1`() = assertTrue("1.2.0.0".previousVersion("1.2.0.1"))
	@Test fun `1dot2dot0dot0 previous version to 1dot2dot0dot1dot0`() = assertTrue("1.2.0.0".previousVersion("1.2.0.1.0"))
	@Test fun `1dot2dot0 not previous version to 1dot1dot6dot3`() = assertFalse("1.2.0".previousVersion("1.1.6.3"))
	@Test fun `1dot2dot0 not previous version to 1dot1dot9`() = assertFalse("1.2.0".previousVersion("1.1.9"))
	@Test fun `1dot2dot0 not previous version to 1dot1dot9dot0`() = assertFalse("1.2.0".previousVersion("1.1.9.0"))
	@Test fun `1dot2dot0dot1 not previous version to 1dot2dot0`() = assertFalse("1.2.0.1".previousVersion("1.2.0"))
	@Test fun `1dot2dot0dot1dot0 not previous version to 1dot2dot0`() = assertFalse("1.2.0.1.0".previousVersion("1.2.0"))
	@Test fun `1dot2dot0 same version as 1dot2dot0`() = assertFalse("1.2.0".previousVersion("1.2.0"))
	@Test fun `1dot2dot0dot0 same version as 1dot2dot0`() = assertFalse("1.2.0.0".previousVersion("1.2.0"))
	@Test fun `1dot2dot0dot0dot0 same version as 1dot2dot0dot0dot0`() = assertFalse("1.2.0.0.0".previousVersion("1.2.0.0.0"))
	
	data class ExampleItem(val name: String, val surname: String = "")
	
	@Test fun `null equals null through fromJson and toJson`() = assertEquals(null?.fromJson<ExampleItem>()?.toJson(), null)
	@Test fun `"" equals null through fromJson and toJson`() = assertEquals("".fromJson<ExampleItem>()?.toJson(), null)
	@Test fun `ExampleItem toJson not equals null through fromJson and toJson`() = assertNotEquals(
		ExampleItem(name = "John", surname = "Smith").toJson().fromJson<ExampleItem>()?.toJson(), null)
	
	@Test fun `1 has 1 digit`() = assertEquals(1, "1".digitsNum)
	@Test fun `12 has 2 digits`() = assertEquals(2, "12".digitsNum)
	@Test fun `123 has 3 digits`() = assertEquals(3, "123".digitsNum)
	@Test fun `1234 has 4 digits`() = assertEquals(4, "1234".digitsNum)
	@Test fun `12345 has 5 digits`() = assertEquals(5, "12345".digitsNum)
	@Test fun `123,45 has 5 digits`() = assertEquals(5, "123,45".digitsNum)
	@Test fun `12dot345 has 5 digits`() = assertEquals(5, "12.345".digitsNum)
	@Test fun `12 345dot67 has 7 digits`() = assertEquals(7, "12 345.67".digitsNum)
	@Test fun `12,345dot67 has 7 digits`() = assertEquals(7, "12,345.67".digitsNum)
	@Test fun `12dot345dot67 has 7 digits`() = assertEquals(7, "12.345,67".digitsNum)
	
	@Test fun `empty string is alphanumeric`() = assertTrue("".isAlphaNumeric())
	@Test fun `"hola" string is alphanumeric`() = assertTrue("hola".isAlphaNumeric())
	@Test fun `"hola57" string is alphanumeric`() = assertTrue("hola57".isAlphaNumeric())
	@Test fun `"57" string is alphanumeric`() = assertTrue("57".isAlphaNumeric())
	@Test fun `"año" string is alphanumeric`() = assertTrue("año".isAlphaNumeric())
	@Test fun `"año " string is alphanumeric (has space)`() = assertFalse("año ".isAlphaNumeric())
	@Test fun `"Cuarto " string is not alphanumeric (has space)`() = assertFalse("Cuarto ".isAlphaNumeric())
	
	@Test fun `empty isAlpha`() = assertTrue("".isAlpha())
	@Test fun `"hola" isAlpha`() = assertTrue("hola".isAlpha())
	@Test fun `"hola57" is alpha`() = assertFalse("hola57".isAlpha())
	@Test fun `"57" is not alpha`() = assertFalse("57".isAlpha())
	@Test fun `"año" isAlpha`() = assertTrue("año".isAlpha())
	@Test fun `"Cuarto " isAlpha (if we allow spaces`() = assertTrue("Cuarto ".isAlpha(true))
	@Test fun `"Cuarto " is not alpha (it has a space)`() = assertFalse("Cuarto ".isAlpha())
	
	@Test
	fun `indexOf equals firt index of indexesOf`() {
		val text = "hola adios"
		val text2 = "holaaaaaaaaaaaa adiosssssssss"
		assertEquals(text.indexOf("adios"), text.indexesOf("adios").first())
		assertEquals(text2.indexOf("adios"), text2.indexesOf("adios").first())
	}

	@Test fun `indexesOf test in we have test word multiple times on this test function on this test class`() = assertEquals("we have test word multiple times on this test function on this test class".indexesOf("test"), listOf(8, 41, 63))
	
	@Test fun `removelast + of +-+-+-+-`() = assertEquals("+-+-+--", "+-+-+-+-".removeLast("+"))

	@Test
	fun decimal_formatting() {
		assertEquals("5", ("5").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("5.", ("5.").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("5.0", ("5.0").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("5.01", ("5.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("5.01", ("5.013").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("5.01", ("5.01324").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("", ("").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("123.", ("123.").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("5.10", ("5.10").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("5.10", ("5.101").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("5.10", ("5.100").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("5.10", ("5.1011").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("5.10", ("5.1099").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("5.0", ("5.0").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("0.1", ("0.1").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("0.01", ("0.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("100.01", ("100.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("1,000.01", ("1000.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("10,000.01", ("10000.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("100,000.01", ("100000.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("1,000,000.01", ("1000000.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("10,000,000.01", ("10000000.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("100,000,000.01", ("100000000.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("1,000,000,000.01", ("1000000000.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("10,000,000,000.01", ("10000000000.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("100,000,000,000.01", ("100000000000.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("1,000,000,000,000.01", ("1000000000000.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("10,000,000,000,000.01", ("10000000000000.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("1,000.01", ("1,000.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("10,000.01", ("10,000.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("100,000.01", ("100,000.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("1,000,000.01", ("1,000,000.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("10,000,000.01", ("10,000,000.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("100,000,000.01", ("100,000,000.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("1,000,000,000.01", ("1,000,000,000.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("10,000,000,000.01", ("10,000,000,000.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("100,000,000,000.01", ("100,000,000,000.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("1,000,000,000,000.01", ("1,000,000,000,000.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("10,000,000,000,000.01", ("10,000,000,000,000.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("100,000,000,000.01", (",100,000,000,000.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("100 000 000 000.01", ("100 000 000 000.01").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = ".", thousandsSeparator = " "))
		assertEquals("0.10", ("0.1").formatDecimal(maxDecimals = 2, forceDecimals = true, decimalSeparator = "."))
		assertEquals("0", ("0.1").formatDecimal(maxDecimals = 0, forceDecimals = true, decimalSeparator = "."))
		assertEquals("0", ("0.").formatDecimal(maxDecimals = 0, forceDecimals = false, decimalSeparator = "."))
		assertEquals("-0.1", ("-0.1").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("-1.1", ("-1.1").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
		assertEquals("-1.12", ("-1.123").formatDecimal(maxDecimals = 2, forceDecimals = false, decimalSeparator = "."))
	}
	
	@Test fun `"12{34}56" pair of third char is on sixth position`() = assert("12{34}56".findPair(2)==5)
	@Test fun `"12{34}56" removing range 2 to 5 is 1256`() = assert("12{34}56".removeRange(2, 6)=="1256") { "12{34}56".removeRange(2, 6) }
	@Test fun `"12{34}56" without {} pair and everything in between is 1256`() = assert("12{34}56".removeRange(2, "12{34}56".findPair(2)+1)=="1256") { "12{34}56".removeRange(2, "12{34}56".findPair(2)+1) }
	@Test fun `"12"34"56" without "" pair and everything in between is 1256`() = assert("12\"34\"56".removeRange(2, "12\"34\"56".findPair(2)+1)=="1256") { "12\"34\"56".removeRange(2, "12\"34\"56".findPair(2)+1) }
	@Test fun `"12"3"4"56" without "" pair and everything in between is 1256`() = assert(("12\"3\\\"4\"56".removeRange(2, "12\"3\\\"4\"56".findPair(2)+1))=="1256") { "result: "+("12\"3\"4\"56".removeRange(2, "12\"3\\\"4\"56".findPair(2)+1))+" vs 1256" }
	@Test fun `"12{34}56" without pair starting at index 2 (}) and everything in between is 1256`() = assert("12{34}56".removePair(2)=="1256") { "12{34}56".removePair(2) }
	@Test fun `"12"34"56" without pair starting at index 2 (") and everything in between is 1256`() = assert("12\"34\"56".removePair(2)=="1256") { "12\"34\"56".removePair(2) }
	@Test fun `"12"3"4"56" without pair starting at index 2 (") and everything in between is 1256`() = assert("12\"3\\\"4\"56".removePair(2)=="1256") { "12\"3\"4\"56".removePair(2) }

	@Test fun indexesOf_10_on_012345678910111213_is_10() = assertEquals(listOf(10), "012345678910111213".indexesOf("10"))
	@Test fun indexesOf_1_on_012345678910111213_is_1_10_12_13_14_16() = assertEquals(listOf(1,10,12,13,14,16), "012345678910111213".indexesOf("1"))
	@Test fun indexesOf_a_on_abcABC_is_0() = assertEquals(listOf(0), "abcABC".indexesOf("a"))
	@Test fun indexesOf_A_on_abcABC_is_3() = assertEquals(listOf(3), "abcABC".indexesOf("A"))
	@Test fun indexesOf_a_on_abcABC_is_0_3_ignoring_case() = assertEquals(listOf(0,3), "abcABC".indexesOf("a", ignoreCase = true))
	@Test fun indexesOf_a_on_abcABC_is_3_ignoring_case_starting_on_1() = assertEquals(listOf(3), "abcABC".indexesOf("a", ignoreCase = true, startIndex = 1))

	@Test fun remove_last_char_a_abcabcabc_is_abcabcbc() = assertEquals("should be abcabcabc but is ${"abcabcabc".removeLast('a')}", "abcabcbc", "abcabcabc".removeLast('a'))
	@Test fun remove_last_string_a_abcabcabc_is_abcabcbc() = assertEquals("should be abcabcabc but is ${"abcabcabc".removeLast("a")}", "abcabcbc", "abcabcabc".removeLast("a"))
	@Test fun remove_last_abc_abcabcabc_is_abcabc() = assertEquals("should be abcabc but is ${"abcabcabc".removeLast("abc")}", "abcabc", "abcabcabc".removeLast("abc"))

	@Test fun `abc is alpha`() = assertTrue("abc".isAlpha())
	@Test fun `abc abc is alpha (allowing spaces)`() = assertTrue("abc abc".isAlpha(true))
	@Test fun `abc abc is not alpha (not allowing spaces)`() = assertFalse("abc abc".isAlpha(false))
	@Test fun `abc123 is not alpha (is alphanumeric)`() = assertFalse("abc123".isAlpha())
	@Test fun `abc123 is alphanumeric`() = assertTrue("abc123".isAlphaNumeric())
	@Test fun `abc 123 is alphanumeric (allowing spaces)`() = assertTrue("abc 123".isAlphaNumeric(true))
	@Test fun `abc 123 is not alphanumeric (not allowing spaces)`() = assertFalse("abc 123".isAlphaNumeric(false))
	@Test fun `abc123-+ is not alpha`() = assertFalse("abc123-+".isAlpha())
	@Test fun `abc123-+ is not alphanumeric`() = assertFalse("abc123-+".isAlphaNumeric())
}