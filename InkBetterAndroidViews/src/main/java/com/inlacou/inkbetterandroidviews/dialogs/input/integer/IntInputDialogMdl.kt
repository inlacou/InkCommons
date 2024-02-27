package com.inlacou.inkbetterandroidviews.dialogs.input.integer

import android.text.SpannableStringBuilder
import com.inlacou.inkbetterandroidviews.R
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogMdl

open class IntInputDialogMdl(
	override var title: SpannableStringBuilder?,
	override var backgroundColorResId: Int? = null,
	var content: SpannableStringBuilder? = null,
	var hint: String = "",
	var input: Int? = null,
	var suffix: String = "",
	var prefix: String = "",
	var titleColorResId: Int = R.color.basic_black,
	var contentColorResId: Int = R.color.basic_black,
	var textColorResId: Int = R.color.basic_black,
	var hintColorResId: Int = R.color.basic_black,
	var inputColorResId: Int = R.color.basic_black,
	var suffixColorResId: Int = R.color.basic_black,
	var prefixColorResId: Int = R.color.basic_black,
	var maxDigits: Int? = null,
	var required: Boolean = true,
	override var cancelOnOutsideClick: Boolean = true,
	var onAccepted: ((Int) -> Unit),
	override var onCancelled: (() -> Unit)? = null,
): BasicDialogMdl(title, cancelOnOutsideClick, onCancelled, backgroundColorResId) {
	constructor(
		title: String?,
		backgroundColorResId: Int? = null,
		content: String? = null,
		hint: String = "",
		input: Int? = null,
		suffix: String = "",
		prefix: String = "",
		titleColorResId: Int = R.color.basic_black,
		contentColorResId: Int = R.color.basic_black,
		textColorResId: Int = R.color.basic_black,
		hintColorResId: Int = R.color.basic_black,
		inputColorResId: Int = R.color.basic_black,
		suffixColorResId: Int = R.color.basic_black,
		prefixColorResId: Int = R.color.basic_black,
		maxDigits: Int? = null,
		required: Boolean = true,
		cancelOnOutsideClick: Boolean = true,
		onAccepted: ((Int) -> Unit),
		onCancelled: (() -> Unit)? = null
	): this(SpannableStringBuilder(title), backgroundColorResId, SpannableStringBuilder(content),
		hint, input, suffix, prefix,
		titleColorResId, contentColorResId, textColorResId, hintColorResId,
		inputColorResId, suffixColorResId, prefixColorResId, maxDigits,
		required, cancelOnOutsideClick, onAccepted, onCancelled
	)
}