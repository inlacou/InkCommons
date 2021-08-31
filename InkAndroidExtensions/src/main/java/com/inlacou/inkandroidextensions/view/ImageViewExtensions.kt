package com.inlacou.inkandroidextensions.view

import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import com.inlacou.inkandroidextensions.getColorCompat
import com.inlacou.inkandroidextensions.getDrawableCompat
import timber.log.Timber

//ImageView

fun ImageView.tint(colorResId: Int) {
	ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(this.context.getColorCompat(colorResId)))
}

fun ImageView.tint(colorHex: String) {
	ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(Color.parseColor(colorHex)))
}

fun ImageView.clearTint() {
	ImageViewCompat.setImageTintList(this, null)
}

fun ImageView.recycleBitmap() {
	(drawable as BitmapDrawable?)?.bitmap?.let {
		it.recycle()
		Timber.d("SongView | bitmap $it from $this recycled")
	}
}

fun Drawable.tint(colorHex: String): Drawable {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
		setTint(Color.parseColor(colorHex))
		setTintList(ColorStateList.valueOf(Color.parseColor(colorHex)))
	}else{
		setColorFilter(Color.parseColor(colorHex), PorterDuff.Mode.SRC_ATOP)
	}
	return this
}

fun ImageView.setDrawableRes(resId: Int) {
	this.setImageDrawable(resources.getDrawableCompat(resId))
}

fun ImageView.load(resId: Int){
	val options = BitmapFactory.Options()
	options.inJustDecodeBounds = true
	options.inSampleSize = calculateInSampleSize(options, width, height)
	options.inJustDecodeBounds = false
	BitmapFactory.decodeResource(resources, resId, options)
}

internal fun ImageView.tintByResId(colorResId: Int){
	ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(this.context.getColorCompat(colorResId)))
}

internal fun ImageView.tintByColor(color: Int){
	ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color))
}

internal fun ImageView.getTint() = ImageViewCompat.getImageTintList(this)

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
	// Raw height and width of image
	val height = options.outHeight
	val width = options.outWidth
	var inSampleSize = 1

	if (height > reqHeight || width > reqWidth) {

		val halfHeight = height / 2
		val halfWidth = width / 2

		// Calculate the largest inSampleSize value that is a power of 2 and keeps both
		// height and width larger than the requested height and width.
		while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
			inSampleSize *= 2
		}
	}

	return inSampleSize
}

///ImageView