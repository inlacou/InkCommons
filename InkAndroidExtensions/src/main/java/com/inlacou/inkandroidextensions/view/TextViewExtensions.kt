package com.inlacou.inkandroidextensions.view

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.inlacou.inkandroidextensions.getColorCompat
import com.inlacou.inkandroidextensions.getDrawableCompat
import com.inlacou.inkandroidextensions.fromHtml

fun TextView.setTextColorResId(colorResId: Int){
	this.setTextColor(ColorStateList.valueOf(this.context.getColorCompat(colorResId)))
}

fun TextView.setDrawableTint(colorResId: Int) = compoundDrawables.forEach { it?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, colorResId), PorterDuff.Mode.SRC_IN) }

fun TextView.stripUnderlines() {
	class URLSpanNoUnderline(url: String) : URLSpan(url) {
		override fun updateDrawState(ds: TextPaint) {
			super.updateDrawState(ds)
			ds.isUnderlineText = false
		}
	}

	val s = SpannableString(text)
	val spans = s.getSpans(0, s.length, URLSpan::class.java)
	for (span in spans) {
		val start = s.getSpanStart(span)
		val end = s.getSpanEnd(span)
		s.removeSpan(span)
		s.setSpan(URLSpanNoUnderline(span.url), start, end, 0)
	}
	text = s
}

fun TextView.allowHTML(){
	movementMethod = LinkMovementMethod.getInstance()
}

fun TextView.setHTML(text: String){
	this.text = text.fromHtml()
}

fun EditText.setSelectionEnd(){
	setSelection(text.length)
}

/**
 * If not resId given, mantain the previous
 */
fun TextView.setDrawable(padding: Int, leftResId: Int? = null, topResId: Int? = null, rightResId: Int? = null, botResId: Int? = null){
	setCompoundDrawablesWithIntrinsicBounds(when{
		leftResId==null -> compoundDrawables[0]
		leftResId>=0 -> context.getDrawableCompat(leftResId)
		else -> null
	}, when{
		topResId==null -> compoundDrawables[1]
		topResId>=0 -> context.getDrawableCompat(topResId)
		else -> null
	}, when{
		rightResId==null -> compoundDrawables[2]
		rightResId>=0 -> context.getDrawableCompat(rightResId)
		else -> null
	}, when{
		botResId==null -> compoundDrawables[3]
		botResId>=0 -> context.getDrawableCompat(botResId)
		else -> null
	})
	compoundDrawablePadding = padding
}

fun TextView.setDrawableLeft(drawable: Drawable?, padding: Int? = null){
	setCompoundDrawablesWithIntrinsicBounds(drawable, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3])
	padding?.let { compoundDrawablePadding = padding }
}

fun TextView.setDrawableRight(drawable: Drawable?, padding: Int? = null){
	setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[0], compoundDrawables[1], drawable, compoundDrawables[3])
	padding?.let { compoundDrawablePadding = padding }
}
