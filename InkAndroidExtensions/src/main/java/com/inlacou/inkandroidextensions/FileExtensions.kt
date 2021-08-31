package com.inlacou.inkandroidextensions

import android.media.MediaMetadataRetriever
import androidx.annotation.RequiresPermission
import com.inlacou.inkkotlinextensions.removeLast
import timber.log.Timber
import java.io.*
import java.net.URLConnection

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
fun File.readFolder(): List<File> {
	Timber.d("readFolder | Path: $path")
	if(isFile){
		Timber.d("readFolder | path not a ic_folder, but a file")
		return emptyList()
	}
	val files = listFiles()
	Timber.d("readFolder | files on ic_folder: ${files?.size}")
	return files?.toList() ?: listOf()
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
fun File.getFolders(): List<File> {
	return readFolder().filter { !it.isFile }
}

val audioFileExtensions = listOf(".3gp", ".aa", ".aac", ".aax", ".act", ".aiff", ".alac", ".amr", ".ape", ".au", ".awb", ".dct", ".dss", ".dvf",
	".flac", ".gsm", ".iklax", ".ivs", ".m4a", ".m4b", ".m4p", ".mmf", ".mp3", ".mpc", ".msv", ".nmf", ".nsf", ".ogg", ".oga", ".mogg", ".opus", "ra", ".rm",
	".rf64", ".sln", ".tta", ".voc", ".vox", ".wav", ".wma", ".wv", ".webm", "8svx", ".cda") //source https://en.wikipedia.org/wiki/Audio_file_format
val androidSupportedAudioFileExtensions = listOf(".3gp", ".acc", ".flac", ".ogg", ".m4a", ".mid", ".mp3", ".xmf", ".wav") //source https://blog.online-convert.com/files-supported-by-android/
val androidSupportedVideoFileExtensions = listOf(".3gp", ".mkv", ".mp4", ".ts", ".webm") //source https://blog.online-convert.com/files-supported-by-android/

val musicRegex = Regex(".*(${androidSupportedAudioFileExtensions.map{"($it)|"}.toString().replace(" ", "").replace(",", "").replace("[", "").replace("]", "")})$".removeLast("|"))
val videoRegex = Regex(".*(${androidSupportedVideoFileExtensions.map{"($it)|"}.toString().replace(" ", "").replace(",", "").replace("[", "").replace("]", "")})$".removeLast("|"))

@RequiresPermission("android.permission.READ_EXTERNAL_STORAGE")
fun File.isMusic(): Boolean {
	Timber.d("regex to ${this.absolutePath} | ${musicRegex.pattern} " +
		"| ${musicRegex.matches(absolutePath)} " +
		"| hasVideo: ${hasVideoLight()}")
	return !isDirectory && musicRegex.matches(absolutePath) && !hasVideoLight()
}

@RequiresPermission("android.permission.READ_EXTERNAL_STORAGE")
fun File.isVideo(): Boolean {
	return videoRegex.matches(absolutePath) && hasVideoLight()
}

@RequiresPermission("android.permission.READ_EXTERNAL_STORAGE")
fun File.getFiles(): List<File> {
	return readFolder().filter { it.isFile }
}

/**
 * @param blackRegex example to check file extension "(jpg|jpeg|png|mp4|mp3)$"
 */
@RequiresPermission("android.permission.READ_EXTERNAL_STORAGE")
fun File.readTree(prefix: String = "", whiteRegex: Regex? = null, blackRegex: Regex? = null): String? {
	return if(!exists()) ""
	else if(isFile) {
		if(blackRegex?.matches(path)!=true || whiteRegex?.matches(path)==true) "\n$prefix   $name"
		else null
	} else{ //isFolder
		var text = "\n$prefix-> / $name"
		(listFiles()?.toList() ?: listOf()).mapNotNull { it.readTree("$prefix   |", whiteRegex, blackRegex) }.forEach { text += it }
		val aux = "\n$prefix"
		text += "$aux   \\"
		text
	}
}

/**
 * Time consuming method.
 */
fun File.hasVideo(): Boolean {
	if(isDirectory) return false
	return try{
		val inputStream = FileInputStream(absolutePath)
		val mmr = MediaMetadataRetriever()
		mmr.setDataSource(inputStream.fd)
		mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO)!=null
	}catch (e: Exception) {
		Timber.e(e)
		false
	}
}

fun File.hasVideoLight(): Boolean {
	if(isDirectory) return false
	return mimeType.let {
		it!=null && it.startsWith("video")
	}
}

val File.mimeType
	get() = URLConnection.guessContentTypeFromName(path)

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
