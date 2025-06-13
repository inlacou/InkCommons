package com.inlacou.inkbetterandroidviews.dialogs.list.complex

import android.text.SpannableStringBuilder
import android.view.View
import com.inlacou.inkandroidextensions.ResId
import com.inlacou.inkbasicmodels.TextViewMdl
import com.inlacou.inkbetterandroidviews.adapters.SimpleRvAdapter
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogMdl

open class ComplexListDialogMdl<CustomView: View, CustomModel>(
	override var title: TextViewMdl? = null,
	override var buttonCancelText: SpannableStringBuilder? = null,
	override var buttonAcceptText: SpannableStringBuilder? = null,
	override var backgroundColorResId: ResId? = null,
	var itemLayoutResId: Int,
	var items: List<CustomModel>,
	override var cancelOnOutsideClick: Boolean = true,
	var onViewPopulate: ((ComplexListDialog<CustomView, CustomModel>, CustomView, CustomModel) -> Unit),
	override var onCancelled: (() -> Unit)? = null,
): BasicDialogMdl(
	title = title,
	buttonCancelText = buttonCancelText,
	buttonAcceptText = buttonAcceptText,
	cancelOnOutsideClick = cancelOnOutsideClick,
	onCancelled = onCancelled,
	backgroundColorResId = backgroundColorResId,
)