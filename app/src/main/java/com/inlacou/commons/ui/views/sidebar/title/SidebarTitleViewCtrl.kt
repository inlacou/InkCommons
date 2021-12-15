package com.inlacou.commons.ui.views.sidebar.title

import com.inlacou.commons.ui.views.sidebar.SidebarViewMdl
import com.inlacou.inkbetterandroidviews.BaseViewCtrl

class SidebarTitleViewCtrl(val view: SidebarTitleView, var model: SidebarViewMdl): BaseViewCtrl(view, model) {

	fun onClick() = model.onClick?.invoke(model)


}