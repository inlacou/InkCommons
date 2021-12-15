package com.inlacou.inkbetterandroidviews.dialogs.basic

import android.app.Activity
import com.inlacou.inkbetterandroidviews.BaseViewCtrl

abstract class BasicDialogViewCtrl(open val view: BasicDialogView, open val model: BasicDialogViewMdl): BaseViewCtrl(view, model) {

	open fun onOutsideClick() = if(model.cancelOnOutsideClick) {
		model.onCancelled?.invoke()
		view.dismiss()
	} else Unit

	open fun onCancelClick() = view.dismiss(view.context as Activity)

}