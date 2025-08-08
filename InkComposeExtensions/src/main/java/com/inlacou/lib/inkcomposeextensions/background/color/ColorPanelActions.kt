package com.inlacou.lib.inkcomposeextensions.background.color

import com.inlacou.lib.inkcomposeextensions.background.BoxItem

data class ColorPanelActions(
    val items: List<BoxItem>,
    val selectedPosition: Int,
    val colorSelectedListener: OnColorSelectedListener,
)
