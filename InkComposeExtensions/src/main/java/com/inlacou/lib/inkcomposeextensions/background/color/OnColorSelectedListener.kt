package com.inlacou.lib.inkcomposeextensions.background.color

interface OnColorSelectedListener {
    fun onColorSelected(
        color: Int,
        position: Int,
        colorType: ColorData.DataType = ColorData.DataType.COLOR,
        fromUser: Boolean = true
    )

    fun onPickerSelected() = Unit

    fun onDropperSelected() = Unit

    fun onDismiss() = Unit
}
