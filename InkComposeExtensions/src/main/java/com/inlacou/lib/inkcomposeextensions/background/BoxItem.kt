package com.inlacou.lib.inkcomposeextensions.background

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

sealed class BoxItem(open val isSelected: Boolean = false)

data class ColorBoxItem(val color: Color, override val isSelected: Boolean = false) :
    BoxItem(isSelected)

data class GradientColorBoxItem(
    val colors: List<Color>,
    val angle: Int = 90,
    override val isSelected: Boolean = false,
) : BoxItem(isSelected)

data class TransparentBoxItem(
    val color: Color = Color.Transparent,
    override val isSelected: Boolean = false,
) : BoxItem(isSelected)

data class RoundedIconBoxItem(
    @DrawableRes val icon: Int,
    override val isSelected: Boolean = false,
) : BoxItem(isSelected)

data class CircleIconBoxItem(
    @DrawableRes val icon: Int,
    override val isSelected: Boolean = false,
) : BoxItem(isSelected)

data class CircleImageBoxItem(
    @DrawableRes val icon: Int,
    override val isSelected: Boolean = false,
) : BoxItem(isSelected)
