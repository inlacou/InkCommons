package com.inlacou.commons.ui.views.sidebar.row

import com.inlacou.commons.ui.views.sidebar.SidebarViewMdl
import com.inlacou.inkbetterandroidviews.BaseViewCtrl

class SidebarRowViewCtrl(val view: SidebarRowView, var model: SidebarViewMdl): BaseViewCtrl(view, model) {

	fun onClick() {
		model.onClick?.invoke(model)
	}

}