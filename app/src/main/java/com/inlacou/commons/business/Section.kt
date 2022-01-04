package com.inlacou.commons.business

enum class Section(val mdl: SectionMdl) {
	GENERAL(SectionMdl(text = "General", hideable = false)),
	TEXTVIEW_BITMAP(SectionMdl(text = "TextView Bitmap", hideable = false)),
	BETTER_SPINNER(SectionMdl(text = "Better Spinner", hideable = false)),
	DIALOGS(SectionMdl(text = "Dialogs", hideable = false)),
}