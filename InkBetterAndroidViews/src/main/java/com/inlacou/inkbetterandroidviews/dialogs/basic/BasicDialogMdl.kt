package com.inlacou.inkbetterandroidviews.dialogs.basic

import android.text.SpannableStringBuilder
import com.inlacou.inkbasicmodels.TextViewMdl
import com.inlacou.inkandroidextensions.ResId

open class BasicDialogMdl(
	open var title: TextViewMdl? = null,
	open var buttonCancelText: SpannableStringBuilder? = null,
	open var buttonAcceptText: SpannableStringBuilder? = null,
	open var cancelOnOutsideClick: Boolean = true,
	open var onCancelled: (() -> Unit)? = null,
	open var backgroundColorResId: ResId? = null,
)