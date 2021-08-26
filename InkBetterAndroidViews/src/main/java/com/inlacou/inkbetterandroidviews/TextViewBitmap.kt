package com.inlacou.inkbetterandroidviews

import android.content.Context
import android.graphics.*
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import com.inlacou.inkandroidextensions.dpToPx
import timber.log.Timber
import kotlin.math.max

class TextViewBitmap private constructor(val context: Context) {
	
	private var maxWidth: Int? = null
	private var text: String = ""
	private var color: Int = Color.BLACK
	private var typefaceResName: String? = null
	private var typeface: Typeface? = null
	private var fontSize: Float = 16f
	private var fontSizeInSp: Boolean = true
	private var maxLines: Int? = null
	private var ellipsize: TextUtils.TruncateAt = TextUtils.TruncateAt.END
	private var boldFrom: Int? = null
	private var boldTo: Int? = null
	//TODO make bolding dynamic (not needed now, don't do it if not needed, it's not and easy task. Or even if easy, it would be quite time consuming)
	
	/**
	 * Method that emulates a TextView into a canvas. Does NOT really create a textview and then build a canvas with it.
	 * @param ellipsize only supports END and MARQUEE, and MARQUEE not *exactly* but good enough
	 */
	fun buildTextViewAsBitmap(): Bitmap {
		Log.e("TextViewBitmap", "maxWidth: $maxWidth"
				+ " | text: $text"
				+ " | color: $color"
				+ " | typefaceResName: $typefaceResName"
				+ " | typeface: $typeface"
				+ " | fontSize: $fontSize"
				+ " | fontSizeInSp: $fontSizeInSp"
				+ " | lines: $maxLines"
				+ " | ellipsize: $ellipsize"
		)
		val fontSizePX = if(fontSizeInSp) fontSize.dpToPx() else fontSize
		val normalPaint = Paint().apply {
			this.isAntiAlias = true
			//TODO typefaceResName should have priority
			this.typeface = this@TextViewBitmap.typeface ?: if(typefaceResName!=null) Typeface.createFromAsset(context.assets, "fonts/$typefaceResName.ttf") else Typeface.DEFAULT
			this.color = color
			this.textSize = fontSizePX
			this.textAlign = Paint.Align.LEFT
		}
		val boldPaint = Paint().apply {
			this.isAntiAlias = true
			//TODO typefaceResName should have priority
			this.typeface = Typeface.create(this@TextViewBitmap.typeface, Typeface.BOLD) ?: if(typefaceResName!=null) Typeface.create(Typeface.createFromAsset(context.assets, "fonts/$typefaceResName.ttf"), Typeface.BOLD) else Typeface.DEFAULT_BOLD
			this.color = color
			this.textSize = fontSizePX
			this.textAlign = Paint.Align.LEFT
		}
		
		val height = (fontSizePX / 0.75).toInt()
		val maxLines = this.maxLines ?: 1
		Timber.d("maxLines: $maxLines")
		var actualLines = if(maxWidth!=null) text.chunkByPaintMeasure(normalPaint, maxWidth!!, maxLines) else listOf(text)
		Timber.d("actualLines: ${actualLines.size}")
		
		//transform text to have lines
		actualLines = actualLines.mapIndexed { index, s ->
			when {
				index!=actualLines.size-1 -> "$s\n" //add line
				ellipsize==TextUtils.TruncateAt.END -> {
					"${s.take(s.length-2)}..."
				} //ellipsize
				else -> s //fallback
			}
		}
		var indexAcc = 0
		val linesToDraw = actualLines.mapIndexed { index, it ->
			var currentBoldFrom = (boldFrom?:0) - indexAcc
			var currentBoldTo = (boldTo?:0) - indexAcc
			val max = it.length
			val line = if ((boldFrom==null || boldTo==null)
					|| (currentBoldFrom <= 0 && currentBoldTo <= 0)
					|| (currentBoldFrom>max || currentBoldTo>max)) {
				//not in this line
				Timber.d("this line has no bold: $it")
				Line(listOf(
						DrawText(it, normalPaint, normalPaint.measureText(it))
				))
			} else {
				Timber.d("this line has bold: $it")
				if(currentBoldFrom<0) currentBoldFrom = 0
				if(currentBoldTo>max) currentBoldTo = max
				val previousText = it.substring(0, currentBoldFrom)
				val boldText = it.substring(currentBoldFrom, currentBoldTo)
				
				var posteriorText = it.substring(currentBoldTo, max)
				Timber.d("$previousText*$boldText*$posteriorText")
				Line(listOf(
						DrawText(previousText, normalPaint, normalPaint.measureText(previousText))
						, DrawText(boldText, boldPaint, boldPaint.measureText(boldText))
						, DrawText(posteriorText, normalPaint, normalPaint.measureText(posteriorText))
				))
			}
			indexAcc += it.replace("\n", "").length
			line
		}
		val textWidth = max(linesToDraw.maxByOrNull { it.measure }?.measure ?: 0f, maxWidth?.toFloat() ?: 0f).toInt()
		val bitmap = Bitmap.createBitmap(if(textWidth>0) textWidth else 1, height*maxLines, Bitmap.Config.ARGB_8888)
		val canvas = Canvas(bitmap)
		var currentHeight = 0f
		linesToDraw.forEach {
			var currentWidth = 0f
			it.texts.forEach {
				Timber.d("drawing text \"${it.text}\" at x,y $currentWidth, ${currentHeight+fontSizePX} with paint ${if(it.paint==normalPaint) "normal" else "bold"}")
				canvas.drawText(it.text, currentWidth, currentHeight+fontSizePX, it.paint)
				currentWidth += it.measure
			}
			currentHeight += (fontSizePX*1.2).toInt()
		}
		return bitmap
	}
	
	private data class Line(val texts: List<DrawText>) {
		val measure: Float
			get() = texts.sumByDouble { it.measure.toDouble() }.toFloat()
	}
	private data class DrawText(val text: String, val paint: Paint, val measure: Float)
	
	/**
	 * TODO This should be line limit aware
	 */
	private fun String.chunkByPaintMeasure(paint: Paint, maxWidth: Int, maxLines: Int = Int.MAX_VALUE): List<String> {
		if(maxWidth==0) return listOf(this)
		val chunks = mutableListOf<String>()
		var currentIndex = 0
		var currentOffset = 0
		var currentLine = 1
		while(currentOffset+currentIndex<this.length) {
			var currentText = this.substring(currentOffset, currentOffset+currentIndex)
			while(
				//La medida que no tenemos que pasar en tamaño visual
				paint.measureText(currentText)<maxWidth
				//Tampoco nos podemos pasar en letras que vamos pillando del texto
				&& currentOffset+currentIndex<=this.length) {
				currentText = this.substring(currentOffset, currentOffset+currentIndex)
				currentIndex += 1
			}
			
			Timber.d("tenemos 1: $currentText")
			
			if(currentOffset+currentIndex>=this.length) {
				chunks.add(this.substring(currentOffset, currentOffset+currentIndex-1))
				return chunks
			}else if(currentLine<maxLines) {
				//Vamos quitando hasta que el último elemento no sea un espacio vacío o un salto de línea
				while ((currentText.lastOrNull()!=' ' && currentText.lastOrNull()!='\n')){
					currentIndex -= 1
					currentText = this.substring(currentOffset, currentOffset+currentIndex)
				}
				currentIndex += 1 //volvemos 1, para que el último sí sea un espacio vacío o un salto de línea
			}
			
			Timber.d("tenemos 2: $currentText")
			
			val aux = currentOffset+currentIndex-1
			chunks.add(this.substring(currentOffset, aux))
			currentLine += 1
			if(currentLine > maxLines) return chunks
			currentOffset = aux
			currentIndex = 0
		}
		return chunks
	}
	
	
	class Builder(val context: Context) {
		
		val item = TextViewBitmap(context)
		
		fun setMaxWidth(maxWidth: Int?): Builder {
			item.maxWidth = maxWidth
			return this
		}
		fun setText(text: String): Builder {
			item.text = text
			return this
		}
		fun setColor(color: Int): Builder {
			item.color = color
			return this
		}
		fun setTypefaceResName(typefaceResName: String): Builder {
			item.typefaceResName = typefaceResName
			return this
		}
		fun setTypeface(typeface: Typeface): Builder {
			item.typeface = typeface
			return this
		}
		fun setFontSize(fontSize: Float): Builder {
			item.fontSize = fontSize
			return this
		}
		fun setIsFontSizeInSp(fontSizeInSp: Boolean): Builder {
			item.fontSizeInSp = fontSizeInSp
			return this
		}
		
		/**
		 * @param ellipsize only supports END and MARQUEE, and MARQUEE not *exactly* but good enough
		 */
		fun setEllipsize(ellipsize: TextUtils.TruncateAt): Builder {
			item.ellipsize = ellipsize
			return this
		}
		fun setMaxLines(lines: Int): Builder {
			item.maxLines = lines
			return this
		}
		/**
		 * alias for @method copyFromTextView
		 */
		fun setFromTextView(textView: TextView): Builder {
			return copyFromTextView(textView)
		}
		/**
		 * Does not copy bold/italic status.
		 */
		fun copyFromTextView(textView: TextView): Builder {
			setColor(textView.currentTextColor)
			setFontSize(textView.textSize)
			setTypeface(textView.typeface)
			setIsFontSizeInSp(false)
			setMaxLines(textView.maxLines)
			setText(textView.text.toString())
			setEllipsize(textView.ellipsize)
			Timber.d("setMaxWidth to textView.width? it is ${textView.width}")
			Timber.d("setMaxWidth to textView.maxWidth? it is ${textView.maxWidth}")
			//maybe setMaxWidth(textView.maxWidth)
			return this
		}
		fun setBold(from: Int, to: Int): Builder {
			item.boldFrom = from
			item.boldTo = to
			return this
		}
		fun build(): Bitmap {
			return item.buildTextViewAsBitmap()
		}
		
	}
	
}