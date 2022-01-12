package com.inlacou.commons.ui.views.sidebar

import com.inlacou.commons.business.Section
import com.inlacou.inkbetterandroidviews.adapters.interfaces.Selectable

data class SidebarViewMdl(
		val item: Section,
		val onClick: ((item: SidebarViewMdl) -> Unit?)? = null,
		override var selected: Boolean = false
): Selectable {
	override val selectable: Boolean get() = !item.mdl.isTitle
}