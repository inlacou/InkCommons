package com.inlacou.inkbetterandroidviews.dialogs.input.integer

import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogCtrl

open class IntInputDialogCtrl(override val view: IntInputDialog, override val model: IntInputDialogMdl): BasicDialogCtrl(view, model) {

	fun onAcceptClick(input: Int?) {
		if(view.eiInt?.isValid()==true && input!=null) {
			view.dismiss(onEnd = { model.onAccepted.invoke(input) })
		}
	}

}