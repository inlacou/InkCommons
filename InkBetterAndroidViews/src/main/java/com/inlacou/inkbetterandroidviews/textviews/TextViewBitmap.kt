package com.inlacou.inkbetterandroidviews.textviews

import android.content.Context
import android.graphics.*
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import com.inlacou.inkandroidextensions.dpToPx
import kotlin.math.max
import kotlin.math.min

/**
 * This class is aimed at widgets, where formatting options are limited.
 * It was created to allow bold on only part of a String on a Widget, but supports a lot of other characteristics.
 * @author inlacou on 10/11/2021
 */
class TextViewBitmap private constructor(val context: Context) {

	private var maxWidth: Int? = null
	private var text: String = ""
	private var color: Int = Color.BLACK
	private var backgroundColor: Int? = null
	private var typefaceResName: String? = null
	private var typeface: Typeface? = null
	private var fontSize: Float = 16f
	private var fontSizeInSp: Boolean = true
	private var maxLines: Int? = null
	private var ellipsize: TextUtils.TruncateAt = TextUtils.TruncateAt.END
	private var boldFrom: Int? = null
	private var boldTo: Int? = null
	private var textAlignment: Int? = null
	//TODO make bolding dynamic (not needed now, don't do it if not needed, it's not and easy task. Or even if easy, it would be quite time consuming)

	private val paintAlignment: Paint.Align
		get() = when(textAlignment) {
			View.TEXT_ALIGNMENT_TEXT_START, View.TEXT_ALIGNMENT_VIEW_START -> Paint.Align.LEFT
			View.TEXT_ALIGNMENT_TEXT_END, View.TEXT_ALIGNMENT_VIEW_END -> Paint.Align.RIGHT
			else -> Paint.Align.LEFT
		}


	/**
	 * Method that emulates a TextView into a canvas. Does NOT really create a textview and then build a canvas with it.
	 * @param ellipsize only supports END and MARQUEE, and MARQUEE not *exactly* but good enough
	 */
	fun buildTextViewAsBitmap(): Bitmap {
		Log.i("TextViewBitmap", "maxWidth: $maxWidth"
		  + " | text: $text"
		  + " | color: $color"
		  + " | backgroundColor: $backgroundColor"
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
			this.color = this@TextViewBitmap.color
			this.textSize = fontSizePX
			this.textAlign = paintAlignment
		}
		val boldPaint = Paint().apply {
			this.isAntiAlias = true
			//TODO typefaceResName should have priority
			this.typeface = Typeface.create(this@TextViewBitmap.typeface, Typeface.BOLD) ?: if(typefaceResName!=null) Typeface.create(Typeface.createFromAsset(context.assets, "fonts/$typefaceResName.ttf"), Typeface.BOLD) else Typeface.DEFAULT_BOLD
			this.color = this@TextViewBitmap.color
			this.textSize = fontSizePX
			this.textAlign = paintAlignment
		}

		val height = (fontSizePX / 0.75).toInt()
		val maxLines = this.maxLines ?: 1
		val chunks = if(maxWidth!=null) text.chunkByPaintMeasure(normalPaint, maxWidth!!, maxLines) else Pair(listOf(text), 0)
		var actualLines = chunks.first
		val shouldApplyEllipsize = chunks.second>maxLines

		//transform text to have lines
		actualLines = actualLines.mapIndexed { index, s ->
			when {
				index!=actualLines.size-1 -> "$s\n" //add line
				ellipsize==TextUtils.TruncateAt.END && shouldApplyEllipsize-> {
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
				Line(listOf(
					DrawText(it, normalPaint, normalPaint.measureText(it))
				))
			} else {
				if(currentBoldFrom<0) currentBoldFrom = 0
				if(currentBoldTo>max) currentBoldTo = max
				val previousText = it.substring(0, currentBoldFrom)
				val boldText = it.substring(currentBoldFrom, currentBoldTo)

				var posteriorText = it.substring(currentBoldTo, max)
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
		val bitmap = Bitmap.createBitmap(if(textWidth>0) textWidth else 1, height*min(linesToDraw.size, maxLines), Bitmap.Config.ARGB_8888)
		val canvas = Canvas(bitmap)
		var currentHeight = 0f
		backgroundColor?.let { canvas.drawColor(it) }
		linesToDraw.forEach {
			var currentWidth = 0f
			it.texts.forEach {
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

	private fun String.chunkByPaintMeasure(paint: Paint, maxWidth: Int, maxLines: Int = Int.MAX_VALUE): Pair<List<String>, Int> {
		if(maxWidth<=0) return Pair(listOf(this), 0)
		val chunks = mutableListOf<String>()
		var currentIndex = 0
		var currentOffset = 0
		var currentLine = 0
		while(currentOffset+currentIndex<this.length) {
			currentLine += 1
			var currentText = this.substring(currentOffset, currentOffset+currentIndex)
			while(
			//La medida que no tenemos que pasar en tamaño visual
				paint.measureText(currentText)<maxWidth
				//Tampoco nos podemos pasar en letras que vamos pillando del texto
				&& currentOffset+currentIndex<=this.length) {
				currentText = this.substring(currentOffset, currentOffset+currentIndex)
				currentIndex += 1
			}

			if(currentOffset+currentIndex>=this.length) {
				chunks.add(this.substring(currentOffset, this.length))
				return Pair(chunks.take(maxLines), currentLine)
			}else if(currentLine<maxLines) {
				//Vamos quitando hasta que el último elemento no sea un espacio vacío o un salto de línea
				while ((currentText.lastOrNull()!=' ' && currentText.lastOrNull()!='\n')) {
					currentText = this.substring(currentOffset, currentOffset+currentIndex)
					currentIndex -= 1
				}
				currentIndex += 2 //volvemos 2, para que el último sí sea un espacio vacío o un salto de línea. Volvemos 2, 1 porque hemos buscado hasta 1 de más, otro por el -1 final que en este ultimo paso nos sobraba.
			}

			val aux = currentOffset+currentIndex-1
			chunks.add(this.substring(currentOffset, aux))
			currentOffset = aux
			currentIndex = 0
		}
		return Pair(chunks.take(maxLines), currentLine)
	}

	class Builder(val context: Context) {

		val item = TextViewBitmap(context)

		/**
		 * Be careful, if this is more than real max width, image scaleType will enter in play.
		 */
		fun setMaxWidth(maxWidth: Int?): Builder {
			item.maxWidth = maxWidth
			return this
		}
		fun setText(text: String): Builder {
			item.text = text
			return this
		}
		/**
		 * Built color resource, not raw
		 */
		fun setColor(color: Int): Builder {
			item.color = color
			return this
		}
		/**
		 * Built color resource, not raw
		 */
		fun setBackgroundColor(backgroundColor: Int): Builder {
			item.backgroundColor = backgroundColor
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
			//maybe setMaxWidth(textView.maxWidth)
			return this
		}
		fun setBold(from: Int, to: Int): Builder {
			item.boldFrom = from
			item.boldTo = to
			return this
		}
		/**
		 * Take from View.TEXT_ALIGNMENT_something
		 */
		fun setAlignment(textAlignment: Int?): Builder {
			item.textAlignment = textAlignment
			return this
		}
		fun build(): Bitmap {
			return item.buildTextViewAsBitmap()
		}

	}

}