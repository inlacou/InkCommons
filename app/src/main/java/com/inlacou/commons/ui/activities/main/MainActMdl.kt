package com.inlacou.commons.ui.activities.main

import com.inlacou.commons.business.Section

/**
 * Created by inlacou on 02/02/18.
 */
data class MainActMdl(
		var section: Section = Section.GENERAL,
		val drawerOpenedOnStart: Boolean = false
)