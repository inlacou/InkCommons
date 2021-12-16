package com.inlacou.inkbetterandroidviews.dialogs.list.simple

import com.inlacou.inkbetterandroidviews.adapters.SimpleRvAdapter
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogCtrl

class SimpleListDialogCtrl(override val view: SimpleListDialog, override val model: SimpleListDialogMdl): BasicDialogCtrl(view, model){

	fun onItemSelected(item: SimpleRvAdapter.Row) {
		model.onItemSelected.invoke(item)
		view.dismiss()
	}

}