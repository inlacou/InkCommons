package com.inlacou.inkbetterandroidviews.dialogs.simple

import android.text.SpannableStringBuilder
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogViewMdl

data class SimpleDialogViewMdl(
	override var title: SpannableStringBuilder?,
	var content: SpannableStringBuilder? = null,
	override var cancelOnOutsideClick: Boolean = true,
	var showAcceptButton: Boolean = true,
	var onAccepted: (() -> Unit)? = null,
	override var onCancelled: (() -> Unit)? = null,
): BasicDialogViewMdl(title, cancelOnOutsideClick, onCancelled)