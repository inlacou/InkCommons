package com.inlacou.inkandroidextensions.general

import android.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.inlacou.inkandroidextensions.hexToColor
import com.inlacou.inkandroidextensions.isValidEmail
import com.inlacou.inkandroidextensions.isValidPhone
import com.inlacou.inkandroidextensions.isValidWebUrl
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class StringExtensionsInstTest {

	@Test
	fun test_isValidPhone() {
		assertTrue("112".isValidPhone())
		assertFalse("asffefsw".isValidPhone())
		assertFalse("34607545596sefef".isValidPhone())
		assertTrue("607545596".isValidPhone())
		assertTrue("+34607545596".isValidPhone())
		assertTrue("454 645 564".isValidPhone())
		assertTrue("45 4 645 564".isValidPhone())
		assertTrue("4554 645 564".isValidPhone())
		assertTrue("45554 645 564".isValidPhone())
		assertTrue("456564 645 564".isValidPhone())
		assertTrue("4576454 645 564".isValidPhone())
	}

	@Test
	fun test_isValidWebUrl() {
		assertTrue("https://stackoverflow.com/questions/25770563/0-test-class-found-in-package-default-package".isValidWebUrl())
		assertFalse("https:/tweetdeck.twitter.com/".isValidWebUrl())
		assertFalse("htstps://mail.google.com/mail/u/0/#inbox".isValidWebUrl())
		assertTrue("https://byvapps.slack.com/messages/G2SC40G8M/".isValidWebUrl())
		assertTrue("https://jsoneditoronline.org/".isValidWebUrl())
		assertFalse("https://www.face book.com/".isValidWebUrl())
	}

	@Test fun inlacou_sharklasers_com_is_valid_email() = assertTrue("inlacou@sharklasers.com".isValidEmail())
	@Test fun inlacou_sharklasers_is_not_valid_email() = assertFalse("inlacou@sharklasers".isValidEmail())
	@Test fun inla_cou_sharklasers_com_is_not_valid_email() = assertFalse("inla cou@sharklasers.com".isValidEmail())
	@Test fun inlacou__sharklasers_com_is_not_valid_email() = assertFalse("inlacou@@sharklasers.com".isValidEmail())

	/* Broke by Android versions going forward
	@Test
	fun test_clipboard() {
		activityScenarioRule.scenario.onActivity {
			it.runOnUiThread {
				val appContext = InstrumentationRegistry.getInstrumentation().context
				"hola que tal".copyToClipboard(context = appContext)
				assertEquals("'hola que tal' vs '${appContext.getClipboardText()}'", "hola que tal", appContext.getClipboardText())
				"me llamo juan".copyToClipboard(context = appContext)
				"mi gato se llama guantes".copyToClipboard(context = appContext)
				assertEquals("'mi gato se llama guantes' vs '${appContext.getClipboardText()}'", "mi gato se llama guantes", appContext.getClipboardText())
			}
		}
	}*/

	@Test fun hexToColor_F00_is_color_red() = assert("#F00".hexToColor()==Color.RED)
	@Test fun hexToColor_0F0_is_color_green() = assert("#0F0".hexToColor()==Color.GREEN)
	@Test fun hexToColor_00F_is_color_blue() = assert("#00F".hexToColor()==Color.BLUE)
	@Test fun hexToColor_FFF_is_color_white() = assert("#FFF".hexToColor()==Color.WHITE)
	@Test fun hexToColor_000_is_color_black() = assert("#000".hexToColor()==Color.BLACK)

}
