package com.inlacou.inkbetterandroidviews.dialogs.basic

import android.text.SpannableStringBuilder

abstract class BasicDialogViewMdl(
	open var title: SpannableStringBuilder?,
	open var cancelOnOutsideClick: Boolean = true,
	open var onCancelled: (() -> Unit)? = null,
)