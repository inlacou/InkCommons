package com.inlacou.lib.inkcomposeextensions

object Config {
    var colorConfig = DefaultColors
}

open class Colors(
    val textDefault: Int,
    val textSelected: Int,
    val iconTint: Int,
    val backgroundDark: Int,
    val borderDark: Int,
) {
    companion object {
        val BLACK: Int = -16777216
        val DKGRAY: Int = -12303292
        val BLUE: Int = -16776961
        val CYAN: Int = -16711681
        val GRAY: Int = -7829368
        val GREEN: Int = -16711936
        val LTGRAY: Int = -3355444
        val MAGENTA: Int = -65281
        val RED: Int = -65536
        val TRANSPARENT: Int = 0
        val WHITE: Int = -1
        val YELLOW = -256
    }
}

object DefaultColors: Colors(
    textDefault = DKGRAY,
    textSelected = MAGENTA,
    iconTint = DKGRAY,
    backgroundDark = BLACK,
    borderDark = RED,
)