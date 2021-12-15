package com.inlacou.inkbetterandroidviews.dialogs.simple

import android.text.SpannableStringBuilder

data class SimpleDialogViewMdl(
	var title: SpannableStringBuilder?,
	var content: SpannableStringBuilder? = null,
	var cancel: Boolean = true,
	var onClick: ((item: Any) -> Unit)? = null,
	var onDelete: ((item: Any) -> Unit)? = null,
)