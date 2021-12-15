package com.inlacou.inkbetterandroidviews.dialogs.input

import android.text.SpannableStringBuilder
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogViewMdl

data class TextInputDialogViewMdl(
	override var title: SpannableStringBuilder?,
	var content: SpannableStringBuilder? = null,
	var hint: String = "",
	var input: String = "",
	var minLength: Int = 0,
	var required: Boolean = true,
	override var cancelOnOutsideClick: Boolean = true,
	var onAccepted: ((String) -> Unit),
	override var onCancelled: (() -> Unit)? = null,
): BasicDialogViewMdl(title, cancelOnOutsideClick, onCancelled)