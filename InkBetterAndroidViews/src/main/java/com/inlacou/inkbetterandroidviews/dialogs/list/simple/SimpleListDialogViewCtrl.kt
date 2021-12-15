package com.inlacou.inkbetterandroidviews.dialogs.list.simple

import com.inlacou.inkbetterandroidviews.adapters.SimpleRvAdapter
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogViewCtrl

class SimpleListDialogViewCtrl(override val view: SimpleListDialogView, override val model: SimpleListDialogViewMdl): BasicDialogViewCtrl(view, model){

	fun onItemSelected(item: SimpleRvAdapter.Row) {
		model.onItemSelected.invoke(item)
		view.dismiss()
	}

}