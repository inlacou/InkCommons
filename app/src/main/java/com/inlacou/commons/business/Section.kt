package com.inlacou.commons.business

enum class Section(val mdl: SectionMdl) {
	GENERAL(SectionMdl(text = "General", hideable = false)),
	EVENTBUS_VS_RX(SectionMdl(text = "Eventbus Vs Rx", hideable = false)),
	TEXTVIEW_BITMAP(SectionMdl(text = "TextView Bitmap", hideable = false)),
	BETTER_SPINNER(SectionMdl(text = "Better Spinner", hideable = false)),
	DIALOGS(SectionMdl(text = "Dialogs", hideable = false)),
	COROUTINES(SectionMdl(text = "Coroutines", hideable = false)),
}