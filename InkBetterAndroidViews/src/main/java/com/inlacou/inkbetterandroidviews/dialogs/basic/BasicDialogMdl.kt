package com.inlacou.inkbetterandroidviews.dialogs.basic

import android.text.SpannableStringBuilder

abstract class BasicDialogMdl(
	open var title: SpannableStringBuilder? = null,
	open var buttonCancelText: SpannableStringBuilder? = null,
	open var buttonAcceptText: SpannableStringBuilder? = null,
	open var cancelOnOutsideClick: Boolean = true,
	open var onCancelled: (() -> Unit)? = null,
	open var backgroundColorResId: Int? = null,
)