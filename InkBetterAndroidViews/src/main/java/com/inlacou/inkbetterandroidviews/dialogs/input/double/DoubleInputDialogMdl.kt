package com.inlacou.inkbetterandroidviews.dialogs.input.double

import android.text.SpannableStringBuilder
import com.inlacou.inkbetterandroidviews.R
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogMdl

data class DoubleInputDialogMdl(
	override var title: SpannableStringBuilder?,
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
): BasicDialogMdl(title, cancelOnOutsideClick, onCancelled)