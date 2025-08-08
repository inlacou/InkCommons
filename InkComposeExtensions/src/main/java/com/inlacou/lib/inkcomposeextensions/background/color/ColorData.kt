package com.inlacou.lib.inkcomposeextensions.background.color

import androidx.annotation.DrawableRes

class ColorData
constructor(
    @DrawableRes val background: Int = NO_RESOURCE,
    val color: Int = NO_COLOR,
    val type: DataType = DataType.COLOR
) {

    companion object {
        private const val NO_RESOURCE = -1
        private const val NO_COLOR = -1
    }

    enum class DataType(val typeTitle: String) {
        DROPPER("picker"),
        PICKER("color_chooser"),
        COLOR("palette")
    }
}
