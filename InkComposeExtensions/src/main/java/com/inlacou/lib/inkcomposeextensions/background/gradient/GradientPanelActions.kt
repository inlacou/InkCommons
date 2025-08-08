package com.inlacou.lib.inkcomposeextensions.background.gradient

import com.inlacou.lib.inkcomposeextensions.background.GradientColorBoxItem

data class GradientPanelActions(
    val items: List<GradientColorBoxItem>,
    val gradientAngle: Int,
    val gradientStartColor: Int,
    val gradientEndColor: Int,
    val onGradientAngleChanged: (Int) -> Unit,
    val onStopTrackingTouch: () -> Unit,
    val gradientColorSelectListener: (startColor: Int, endColor: Int, position: Int) -> Unit,
    val startColorClick: (color: Int) -> Unit,
    val endColorClick: (color: Int) -> Unit,
)
