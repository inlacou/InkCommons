package com.inlacou.inkandroidextensions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import java.util.*
import java.util.stream.Stream


fun Int.dpToPx() = (this * Resources.getSystem().displayMetrics.density).toInt()
fun Float.dpToPx() = (this * Resources.getSystem().displayMetrics.density)
fun Int.pxToDp() = (this / Resources.getSystem().displayMetrics.density).toInt()

@RequiresApi(Build.VERSION_CODES.N) fun <T> Stream<T>.toList(): List<T> = mutableListOf<T>().apply { this@toList.forEach { this.add(it) } }

fun Context.getColorCompat(resId: Int): Int {
	return resources.getColorCompat(resId)
}

fun MediaPlayer.setRawAudioDataSource(context: Context, resId: Int) {
	setDataSource(context, getRawResourceUri(context, resId))
}

fun getRawResourceUri(context: Context, resId: Int): Uri {
	return Uri.parse("android.resource://${context}/raw/${resId}")
}

fun String.asStringId(context: Context): String? {
	return try{
		context.resources.getString(context.resources.getIdentifier(this, "string", context.packageName))
	}catch (rnfe: Resources.NotFoundException){
		this.replace("_", " ")
	}
}

fun Context.getDrawableCompat(resId: Int): Drawable {
	return resources.getDrawableCompat(resId)
}

@RequiresPermission(value = Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.networkInfo(): NetworkInfo? = (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo

fun Resources.getDrawableCompat(resId: Int): Drawable {
	return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		getDrawable(resId, null)
	}else{
		getDrawable(resId)
	}
}

fun Resources.getColorCompat(resId: Int): Int {
	return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		getColor(resId, null)
	}else{
		getColor(resId)
	}
}

fun Context.hideKeyboard(view: View) {
	val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
	imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.getCurrentLocale(): Locale {
	return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
		resources.configuration.locales.get(0)
	} else {
		resources.configuration.locale
	}
}

fun String.hexToColor(): Int {
	return Color.parseColor(if(this.length==4) { //For instances like #0F0, equal to #00FF00
		var aux = "#"
		aux += this[1]
		aux += this[1]
		aux += this[2]
		aux += this[2]
		aux += this[3]
		aux += this[3]
		aux
	}else{
		this
	})
}


@RequiresPermission(value = Manifest.permission.ACCESS_NETWORK_STATE)
fun Context?.isOnline(online : () -> Unit, offline : (() -> Unit)? = null) {
	this?.apply {
		val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val netInfo = cm.activeNetworkInfo
		if (netInfo != null && netInfo.isConnected) {
			online()
		}else{
			offline?.invoke()
		}
	} ?: offline?.invoke()
}

fun View.hideKeyboard(): Boolean {
	try {
		val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
		return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
	} catch (ignored: RuntimeException) { }
	return false
}

fun View.showKeyboard() {
	val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
	this.requestFocus()
	imm.showSoftInput(this, 0)
}

fun Bundle.toMap(): Map<String, Any?> {
	val result = HashMap<String, Any?>()
	keySet().forEach { result[it] = get(it) }
	return result
}

fun Context.getExternalStorageDirectoryCompat(): String? {
	var res: String? = null
	try{
		res = Environment.getExternalStorageDirectory().absolutePath
	}catch (e: Exception) {}
	if(res==null || res.isEmpty()){
		res = System.getenv("EXTERNAL_STORAGE")
	}
	return res
}

fun Bitmap.tintGradient(shader: Shader): Bitmap {
	val width = this.width
	val height = this.height
	val updatedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
	val canvas = Canvas(updatedBitmap)
	canvas.drawBitmap(this, 0f, 0f, null)
	val paint = Paint()
	paint.shader = shader
	paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN /* https://i.stack.imgur.com/rLIEz.png */)
	canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
	return updatedBitmap
}
