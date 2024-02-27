package com.inlacou.inkbetterandroidviews.dialogs.input.double

import android.text.SpannableStringBuilder
import com.inlacou.inkbetterandroidviews.R
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogMdl

open class DoubleInputDialogMdl(
	override var title: SpannableStringBuilder?,
	override var backgroundColorResId: Int? = null,
	var content: SpannableStringBuilder? = null,
	var hint: String = "",
	var input: Double? = null,
	var suffix: String = "",
	var prefix: String = "",
	var titleColorResId: Int = R.color.basic_black,
	var contentColorResId: Int = R.color.basic_black,
	var textColorResId: Int = R.color.basic_black,
	var hintColorResId: Int = R.color.basic_black,
	var inputColorResId: Int = R.color.basic_black,
	var suffixColorResId: Int = R.color.basic_black,
	var prefixColorResId: Int = R.color.basic_black,
	var maxDecimals: Int? = null,
	var maxDigits: Int? = null,
	var required: Boolean = true,
	override var cancelOnOutsideClick: Boolean = true,
	var onAccepted: ((Double) -> Unit),
	override var onCancelled: (() -> Unit)? = null,
): BasicDialogMdl(title, cancelOnOutsideClick, onCancelled, backgroundColorResId) {
	constructor(
		title: String?,
		backgroundColorResId: Int? = null,
		content: String? = null,
		hint: String = "",
		input: Double? = null,
		suffix: String = "",
		prefix: String = "",
		titleColorResId: Int = R.color.basic_black,
		contentColorResId: Int = R.color.basic_black,
		textColorResId: Int = R.color.basic_black,
		hintColorResId: Int = R.color.basic_black,
		inputColorResId: Int = R.color.basic_black,
		suffixColorResId: Int = R.color.basic_black,
		prefixColorResId: Int = R.color.basic_black,
		maxDecimals: Int? = null,
		maxDigits: Int? = null,
		required: Boolean = true,
		cancelOnOutsideClick: Boolean = true,
		onAccepted: ((Double) -> Unit),
		onCancelled: (() -> Unit)? = null
	): this(SpannableStringBuilder(title), backgroundColorResId, SpannableStringBuilder(content),
		hint, input, suffix, prefix,
		titleColorResId, contentColorResId, textColorResId, hintColorResId,
		inputColorResId, suffixColorResId, prefixColorResId, maxDecimals,
		maxDigits, required, cancelOnOutsideClick, onAccepted, onCancelled
	)
}