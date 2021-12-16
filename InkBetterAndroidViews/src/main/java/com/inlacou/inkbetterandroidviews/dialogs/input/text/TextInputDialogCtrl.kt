package com.inlacou.inkbetterandroidviews.dialogs.input.text

import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogCtrl

class TextInputDialogCtrl(override val view: TextInputDialog, override val model: TextInputDialogMdl): BasicDialogCtrl(view, model) {

	fun onAcceptClick(text: String) {
		if(view.eiText?.isValid()==true) {
			view.dismiss(onEnd = { model.onAccepted.invoke(text) })
		}
	}

}