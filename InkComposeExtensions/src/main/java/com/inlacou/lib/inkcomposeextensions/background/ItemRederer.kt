package com.inlacou.lib.inkcomposeextensions.background

import androidx.compose.runtime.Composable
import com.inlacou.lib.inkcomposeextensions.background.color.ColorBox
import com.inlacou.lib.inkcomposeextensions.background.color.TransparentColorBox
import com.inlacou.lib.inkcomposeextensions.background.gradient.GradientColorBox

@Composable
fun renderBoxItem(item: BoxItem, isSelected: Boolean, onSelect: (BoxItem) -> Unit) {
    when (item) {
        is ColorBoxItem -> ColorBox(item = item.copy(isSelected = isSelected), onSelect = onSelect)
        is GradientColorBoxItem ->
            GradientColorBox(item.copy(isSelected = isSelected), onSelect = onSelect)
        is TransparentBoxItem ->
            TransparentColorBox(item = item.copy(isSelected = isSelected), onSelect = onSelect)
        is RoundedIconBoxItem -> RoundedIconBox(item = item.copy(isSelected = isSelected), onSelect = onSelect)
        is CircleIconBoxItem -> CircleIconBox(item = item.copy(isSelected = isSelected), onSelect = onSelect)
        is CircleImageBoxItem -> CircleImageBox(item = item.copy(isSelected = isSelected), onSelect = onSelect)
    }
}
