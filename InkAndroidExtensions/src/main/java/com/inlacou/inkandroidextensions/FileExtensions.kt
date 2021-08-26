package com.inlacou.inkandroidextensions

import android.media.MediaMetadataRetriever
import androidx.annotation.RequiresPermission
import timber.log.Timber
import java.io.*

fun File.toByteArray(): ByteArray? {
	val file = File(path)
	val size = file.length().toInt()
	val bytes = ByteArray(size)
	return try {
		val buf = BufferedInputStream(FileInputStream(file))
		buf.read(bytes, 0, bytes.size)
		buf.close()
		bytes
	} catch (e: FileNotFoundException) {
		null
	} catch (e: IOException) {
		null
	}
}

fun File.sizeLabel(): String {
	val b = size()
	val kb = b/1000
	val mb = kb/1000
	val gb = mb/1000
	return "${gb}gb ${mb-gb*1000}mb ${kb-mb*1000}kb"
}

fun File.size(): Long {
	var size: Long = 0
	if (isDirectory) {
		for (child in listFiles()) {
			size += child.size()
		}
	} else {
		size = length()
	}
	return size
}

@RequiresPermission("android.permission.READ_EXTERNAL_STORAGE")
fun File.readFolder(): List<File>? {
	Timber.d("readFolder | Path: $path")
	if(isFile){
		Timber.d("readFolder | path not a folder, but a file")
		return emptyList()
	}
	val files = listFiles()
	Timber.d("readFolder | files on folder: ${files.size}")
	return files.toList()
}

@RequiresPermission("android.permission.READ_EXTERNAL_STORAGE")
fun File.readTree(prefix: String = ""): String {
	return if(!exists()) ""
	else if(isFile) "\n$prefix   $name"
	else{ //isFolder
		var text = "\n$prefix-> / $name"
		(listFiles()?.toList() ?: listOf()).forEach { text += "${it.readTree("$prefix   |")}" }
		val aux = "\n$prefix"
		text += "$aux   \\"
		text
	}
}

@RequiresPermission("android.permission.READ_EXTERNAL_STORAGE")
fun File.toMetadata(): MediaMetadataRetriever {
	//val path = absolutePath.replace("[", "%5B").replace("]", "%5D") //FileNotFoundException
	//val path = absolutePath.replace("[", "\\[").replace("]", "\\]") //FileNotFoundException
	//val path = absolutePath.replace("[", "").replace("]", "") //FileNotFoundException
	//val path = absolutePath.replace("[", "`[").replace("]", "`]") //FileNotFoundException
	val path = absolutePath //setDataSource failed: status = 0x80000000
	Timber.d("toMetadata | path:            $path")
	//Timber.d("readFolder | toURI().rawPath: ${toURI().rawPath}")
	val inputStream = FileInputStream(path)
	val mmr = MediaMetadataRetriever()
	mmr.setDataSource(inputStream.fd)
	return mmr
}
