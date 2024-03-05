package com.inlacou.inkbetterandroidviews.dialogs.basic

import com.inlacou.inkbetterandroidviews.BaseViewCtrl

abstract class BasicDialogCtrl(open val view: BasicDialog, open val model: BasicDialogMdl): BaseViewCtrl(view, model) {

	open fun onOutsideClick() {
		if (model.cancelOnOutsideClick) onCancelClick()
	}

	open fun onCancelClick() = view.dismiss(onEnd = model.onCancelled)

}