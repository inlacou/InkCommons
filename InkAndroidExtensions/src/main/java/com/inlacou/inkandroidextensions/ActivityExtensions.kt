package com.inlacou.inkandroidextensions

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.FileProvider
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun Activity.windowSize(): Point {
	val display = windowManager.defaultDisplay
	val size = Point()
	display.getSize(size)
	return size
}

@RequiresApi(api = Build.VERSION_CODES.R)
fun Context.windowSize(): Point {
	val displayMetrics = DisplayMetrics()
	val display: Display = display!!
	display.getRealMetrics(displayMetrics)
	return Point(displayMetrics.widthPixels, displayMetrics.heightPixels)
}

fun Activity.startActivity(intent: Intent, requestCode: Int? = null){
	if(requestCode!=null){
		startActivityForResult(intent, requestCode)
	}else{
		startActivity(intent)
	}
}

fun Activity.hideKeyboard(){
	val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
	//Find the currently focused view, so we can grab the correct window token from it.
	var view = currentFocus
	//If no view currently has focus, create a new one, just so we can grab a window token from it
	if (view == null) {
		view = View(this)
	}
	imm.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * callback boolean determines if it should continue this path or not
 */
fun Activity.iterateOverAllViews(doSomething: ((View) -> Boolean)? = null, debug: Boolean = false){
	if(debug) Timber.d("family | start: ${getView()}")
	var view = getView()
	try {
		while (view!=null){
			view = view.parent as View?
			if(debug) Timber.d("family | parent: $view")
		}
	}catch (e: java.lang.Exception){
		if(debug) Timber.d("family | final parent: $view")
		view?.let { handleView(it, debug = debug, callback = doSomething) }
	}
}

private fun handleView(view: View, tag: String = " |", debug: Boolean = false, callback: ((View) -> Boolean)? = null){
	if(debug) Timber.d("family$tag $view")
	if(callback?.invoke(view)!=false) {
		when (view) {
			is AppBarLayout -> {
				(0 until view.childCount)
						.map { view.getChildAt(it) }
						.forEach { handleView(it, "$tag appBarChild |", debug = debug, callback = callback) }
			}
			is LinearLayout -> {
				(0 until view.childCount)
						.map { view.getChildAt(it) }
						.forEach { handleView(it, "$tag linearChild |", debug = debug, callback = callback) }
			}
			is DrawerLayout -> {
				(0 until view.childCount)
						.map { view.getChildAt(it) }
						.forEach { handleView(it, "$tag drawerChild |", debug = debug, callback = callback) }
			}
			is ScrollView -> {
				(0 until view.childCount)
						.map { view.getChildAt(it) }
						.forEach { handleView(it, "$tag scrollChild |", debug = debug, callback = callback) }
			}
			is FrameLayout -> {
				(0 until view.childCount)
						.map { view.getChildAt(it) }
						.forEach { handleView(it, "$tag frameChild |", debug = debug, callback = callback) }
			}
			is RelativeLayout -> {
				(0 until view.childCount)
						.map { view.getChildAt(it) }
						.forEach { handleView(it, "$tag relativeChild |", debug = debug, callback = callback) }
			}
			is CoordinatorLayout -> {
				(0 until view.childCount)
						.map { view.getChildAt(it) }
						.forEach { handleView(it, "$tag coordinatorChild |", debug = debug, callback = callback) }
			}
			is ViewGroup -> {
				(0 until view.childCount)
						.map { view.getChildAt(it) }
						.forEach { handleView(it, "$tag coordinatorChild |", debug = debug, callback = callback) }
			}
		}
	}
}

fun Activity.handleThrowable(throwable: Throwable){
	//if(throwable is VolleyError){
	//	handleGenericError(throwable, null)
	//}else{
		Timber.e(throwable.message)
	//}
}

/*fun Activity?.handleGenericError(exception: Exception?, s: String? = null) {
	Timber.d(".handleGenericError | Code: $s | Error: $exception | message: ${exception?.message}")
	Timber.d(".handleGenericError | Code: ${this!=null} && ${exception!=null} && ${exception is VolleyError}")

	if (this!=null && exception!=null && exception is VolleyError) {
		try {
			Timber.d(".handleGenericError2 | ${exception.errorMessage}")
		} catch (e: Exception) {}
		try {
			val jsonObject = JSONObject(exception.errorMessage)
			Timber.d(".handleGenericError | jsonObject: $jsonObject")
			snackbar(when {
				jsonObject.has("localized_description") -> jsonObject.getString("localized_description")
				jsonObject.has("error_description") -> jsonObject.getString("error_description").let {
					when(it){
						"AUTH_INVALID_CREDENTIALS" -> getString(R.string.Auth_invalid_credentials)
						else -> it
					}
				}
				jsonObject.has("msg") -> jsonObject.getString("msg")
				jsonObject.has("message") -> jsonObject.getString("message")
				else -> this.getString(R.string.Unknown_server_error)
			})
		} catch (e: JSONException) {
			e.printStackTrace()
			snackbar(when(exception){
				is NoConnectionError -> this.getString(R.string.Could_not_reach_server_check_internet_connection)
				else -> this.getString(R.string.Unknown_server_error)
			})
		}
	}
}*/

/**
 * Saves the image as PNG to the app's cache directory.
 * @param image Bitmap to save.
 * @return Uri of the saved file or null
 */
fun Activity.saveImage(image: Bitmap, onComplete: (Uri?) -> Unit): Disposable {
	return Observable.just(1).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.computation()).subscribe({
		val imagesFolder = File(cacheDir, "images")
		var uri: Uri? = null
		try {
			imagesFolder.mkdirs()
			val file = File(imagesFolder, "shared_image.png")

			val stream = FileOutputStream(file)
			image.compress(Bitmap.CompressFormat.PNG, 90, stream)
			stream.flush()
			stream.close()
			uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
		} catch (e: IOException) {
			Timber.d("IOException while trying to write file for sharing: " + e.message)
		}
		onComplete.invoke(uri)
	},{})
}

fun Activity.toast(messageResId: Int, duration: Int = Toast.LENGTH_SHORT) = toast(getString(messageResId), duration)
fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
	Timber.d("toasted: $message")
	Toast.makeText(this, message, duration).show()
}

fun Activity.dialog(messageResId: Int, titleResId: Int? = null) = dialog(getString(messageResId), if(titleResId!=null) getString(titleResId) else null)
fun Activity.dialog(message: CharSequence, title: CharSequence? = null) {
	Timber.d("alerted: $message")
	val builder = AlertDialog.Builder(this)
	title?.let { builder.setTitle(it) }
	message.let { builder.setMessage(message) }
	builder.show()
}

fun Activity?.snackbar(message: String, length: Int = Snackbar.LENGTH_LONG) = this?.getView()?.let {
	Timber.d("Snackbaring $message")
	Snackbar.make(it, message, length).apply {
		view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).apply {
			setTextColor(getColorCompat(R.color.snackbar_text_color_over_dark))
		}
	}.show()
}
fun Activity?.snackbar(messageResId: Int, length: Int = Snackbar.LENGTH_LONG) = this?.getView()?.let { snackbar(getString(messageResId), length) }
fun Activity?.getView(): View? = this?.window?.decorView?.findViewById(android.R.id.content)
fun Activity?.disableScreenTimeout() = this?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
fun Activity?.enableScreenTimeout() = this?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)