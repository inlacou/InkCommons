package com.inlacou.inkbetterandroidviews.dialogs.simple

import android.text.SpannableStringBuilder
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogMdl

data class SimpleDialogMdl(
	override var title: SpannableStringBuilder?,
	override var backgroundColorResId: Int? = null,
	var content: SpannableStringBuilder? = null,
	override var cancelOnOutsideClick: Boolean = true,
	var showAcceptButton: Boolean = true,
	var onAccepted: (() -> Unit)? = null,
	override var onCancelled: (() -> Unit)? = null,
): BasicDialogMdl(title, cancelOnOutsideClick, onCancelled, backgroundColorResId)