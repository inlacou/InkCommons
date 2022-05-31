package com.inlacou.commons.ui.fragments.gradienttint

import android.graphics.*
import com.inlacou.inkandroidextensions.tintGradient

sealed class Gradienter {
	abstract operator fun invoke(bitmap: Bitmap, vararg colors: Int): Bitmap
	object LinearTopDown : Gradienter() {
		override fun invoke(bitmap: Bitmap, vararg colors: Int): Bitmap
			= bitmap.tintGradient(LinearGradient(0f, 0f, 0f, bitmap.height.toFloat(), colors, null, Shader.TileMode.CLAMP))
	}
	object LinearTopLeftDownRight: Gradienter() {
		override fun invoke(bitmap: Bitmap, vararg colors: Int): Bitmap
		  = bitmap.tintGradient(LinearGradient(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat(), colors, null, Shader.TileMode.CLAMP))
	}
	object LinearTopRightDownLeft: Gradienter() {
		override fun invoke(bitmap: Bitmap, vararg colors: Int): Bitmap
		  = bitmap.tintGradient(LinearGradient(bitmap.width.toFloat(), 0f, 0f, bitmap.height.toFloat(), colors, null, Shader.TileMode.CLAMP))
	}
	object LinearDownTop: Gradienter() {
		override fun invoke(bitmap: Bitmap, vararg colors: Int): Bitmap
		  = bitmap.tintGradient(LinearGradient(0f, bitmap.height.toFloat(), 0f, 0f, colors, null, Shader.TileMode.CLAMP))
	}
	object LinearDownLeftTopRight: Gradienter() {
		override fun invoke(bitmap: Bitmap, vararg colors: Int): Bitmap
			= bitmap.tintGradient(LinearGradient(0f, bitmap.height.toFloat(), bitmap.width.toFloat(), 0f, colors, null, Shader.TileMode.CLAMP))
	}
	object LinearDownRightTopLeft: Gradienter() {
		override fun invoke(bitmap: Bitmap, vararg colors: Int): Bitmap
			= bitmap.tintGradient(LinearGradient(bitmap.width.toFloat(), bitmap.height.toFloat(), 0f, 0f, colors, null, Shader.TileMode.CLAMP))
	}
	object LinearLeftRight: Gradienter() {
		override fun invoke(bitmap: Bitmap, vararg colors: Int): Bitmap
			= bitmap.tintGradient(LinearGradient(0f, 0f, bitmap.width.toFloat(), 0f, colors, null, Shader.TileMode.CLAMP))
	}
	object LinearRightLeft: Gradienter() {
		override fun invoke(bitmap: Bitmap, vararg colors: Int): Bitmap
			= bitmap.tintGradient(LinearGradient(bitmap.width.toFloat(), 0f, 0f, 0f, colors, null, Shader.TileMode.CLAMP))
	}
	object Radial10: Gradienter() {
		override fun invoke(bitmap: Bitmap, vararg colors: Int): Bitmap
			= bitmap.tintGradient(RadialGradient(bitmap.width.toFloat()/2, bitmap.height.toFloat()/2, 10f, colors, null, Shader.TileMode.CLAMP))
	}
	object Radial50: Gradienter() {
		override fun invoke(bitmap: Bitmap, vararg colors: Int): Bitmap
			= bitmap.tintGradient(RadialGradient(bitmap.width.toFloat()/2, bitmap.height.toFloat()/2, 50f, colors, null, Shader.TileMode.CLAMP))
	}
	object Radial100: Gradienter() {
		override fun invoke(bitmap: Bitmap, vararg colors: Int): Bitmap
			= bitmap.tintGradient(RadialGradient(bitmap.width.toFloat()/2, bitmap.height.toFloat()/2, 100f, colors, null, Shader.TileMode.CLAMP))
	}
	object Sweep: Gradienter() {
		override fun invoke(bitmap: Bitmap, vararg colors: Int): Bitmap
			= bitmap.tintGradient(SweepGradient(bitmap.width.toFloat()/2, bitmap.height.toFloat()/2, colors, null))
	}
}