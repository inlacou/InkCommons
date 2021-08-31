package com.inlacou.inkandroidextensions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import java.net.URLEncoder

fun String?.isValidPhone(): Boolean {
	return this!=null && android.util.Patterns.PHONE.matcher(this).matches()
}

fun String?.isValidWebUrl(): Boolean {
	return this!=null && android.util.Patterns.WEB_URL.matcher(this).matches()
}

fun String?.isValidEmail(): Boolean {
	return this!=null && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun Resources.getStringOrNull(id: Int?): String? {
	if(id==null) return null
	return try {
		getString(id)
	} catch(e: Exception) {
		null
	}
}

/**
 * Sugar
 */
fun String.fromHtml(): Spanned? {
	return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
		Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
	} else {
		Html.fromHtml(this)
	}
}

/**
 * Sugar
 */
fun String.urlEncode(encoder: String = "UTF-8"): String = URLEncoder.encode(this, encoder)

fun String.copyToClipboard(context: Context, label: String = "label") {
	val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
	val clip = ClipData.newPlainText(label, this)
	clipboard.setPrimaryClip(clip) //Do not simplify
}

fun Context.getClipboardText(): CharSequence {
	val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
	return clipboard.primaryClip?.getItemAt(0)?.text ?: ""
}

