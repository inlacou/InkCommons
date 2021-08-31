package com.inlacou.inkandroidextensions.general

import android.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.inlacou.inkandroidextensions.hexToColor
import com.inlacou.inkkotlinextensions.colorToHex

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class GeneralExtensionsInstTest {

	@Test
	fun hex_to_color_and_back() {
		assertEquals("#FF0000", "#FF0000".hexToColor().colorToHex())
		assertEquals("#00FF00", "#00FF00".hexToColor().colorToHex())
		assertEquals("#0000FF", "#0000FF".hexToColor().colorToHex())
		assertEquals("#FFFFFF", "#FFFFFF".hexToColor().colorToHex())
		assertEquals("#000000", "#000000".hexToColor().colorToHex())
		
		//It does not work with minified versions
		assertNotEquals("#F00", "#F00".hexToColor().colorToHex())
		assertNotEquals("#0F0", "#0F0".hexToColor().colorToHex())
		assertNotEquals("#00F", "#00F".hexToColor().colorToHex())
		assertNotEquals("#FFF", "#FFF".hexToColor().colorToHex())
		assertNotEquals("#000", "#000".hexToColor().colorToHex())
		
		//Just different colors
		assertNotEquals("#FFFFFF", "#000000".hexToColor().colorToHex())
		assertNotEquals("#FFF", "#000".hexToColor().colorToHex())
	}
	
	@Test
	@Throws(Exception::class)
	fun color_to_hex() {
		assertEquals("#FF0000", Color.RED.colorToHex())
		assertEquals("#00FF00", Color.GREEN.colorToHex())
		assertEquals("#0000FF", Color.BLUE.colorToHex())
		assertEquals("#FFFFFF", Color.WHITE.colorToHex())
		assertEquals("#000000", Color.BLACK.colorToHex())
		
		//It does not work with minified versions
		assertNotEquals("#F00", Color.RED.colorToHex())
		assertNotEquals("#0F0", Color.GREEN.colorToHex())
		assertNotEquals("#00F", Color.BLUE.colorToHex())
		assertNotEquals("#FFF", Color.WHITE.colorToHex())
		assertNotEquals("#000", Color.BLACK.colorToHex())
		
		//Just different colors
		assertNotEquals("#FFFFFF", Color.BLACK.colorToHex())
		assertNotEquals("#FFF", Color.BLACK.colorToHex())
	}
	
	@Test
	@Throws(Exception::class)
	fun hex_to_color() {
		assertEquals("#FF0000".hexToColor(), Color.RED)
		assertEquals("#00FF00".hexToColor(), Color.GREEN)
		assertEquals("#0000FF".hexToColor(), Color.BLUE)
		assertEquals("#FFFFFF".hexToColor(), Color.WHITE)
		assertEquals("#000000".hexToColor(), Color.BLACK)
		
		//It can work with minified versions
		assertEquals("#F00".hexToColor(), Color.RED)
		assertEquals("#0F0".hexToColor(), Color.GREEN)
		assertEquals("#00F".hexToColor(), Color.BLUE)
		assertEquals("#FFF".hexToColor(), Color.WHITE)
		assertEquals("#000".hexToColor(), Color.BLACK)
		
		//Just different colors
		assertNotEquals("#FFFFFF".hexToColor(), Color.BLACK)
		assertNotEquals("#FFF".hexToColor(), Color.BLACK)
	}
}
