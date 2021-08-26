package com.inlacou.inkandroidextensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

fun String.base64ToBitmap(): Bitmap? {
	return try {
		val decodedString = Base64.decode(this, Base64.DEFAULT)
		BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
	}catch (e: Exception){
		null
	}
}

fun ByteArray.toBitmap(): Bitmap {
	val options = BitmapFactory.Options()
	return BitmapFactory.decodeByteArray(this, 0, this.size, options) //Convert bytearray to bitmap
}

//IMPROVEMENT add a way to limit image size: https://stackoverflow.com/questions/39361550/android-resize-image-to-upload-to-server
//IMPROVEMENT set which is the limit on size for images

fun String.filePathToBase64(): String? {
	val bm = BitmapFactory.decodeFile(this)
	val baos = ByteArrayOutputStream()
	bm.compress(Bitmap.CompressFormat.JPEG, 75, baos) //bm is the bitmap object
	val byteArrayImage = baos.toByteArray()
	return Base64.encodeToString(byteArrayImage, Base64.DEFAULT)
}

fun String.byteArrayToBitmap(): Bitmap? {
	val decodedString = Base64.decode(this, Base64.DEFAULT)
	return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
}