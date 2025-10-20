package com.inlacou.lib.inkcomposeextensions.background.color

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inlacou.lib.inkcomposeextensions.Config
import com.inlacou.lib.inkcomposeextensions.background.BORDER_WIDTH
import com.inlacou.lib.inkcomposeextensions.background.BOX_PADDING
import com.inlacou.lib.inkcomposeextensions.background.BOX_SIZE
import com.inlacou.lib.inkcomposeextensions.background.BoxItem
import com.inlacou.lib.inkcomposeextensions.background.ColorBoxItem
import com.inlacou.lib.inkcomposeextensions.background.INNER_RADIUS
import com.inlacou.lib.inkcomposeextensions.background.OUTER_RADIUS
import com.inlacou.lib.inkcomposeextensions.background.SIZE_DIFF

@Composable
fun ColorBox(
    item: ColorBoxItem,
    borderColor: Color = Color(Config.colorConfig.borderDark),
    onSelect: (BoxItem) -> Unit
) {
    val isSelected = item.isSelected // Use the item's isSelected state
    val size = BOX_SIZE
    val cornerRadius = OUTER_RADIUS
    val borderColor =
        if (isSelected) borderColor
        else Color.Transparent
    val innerSize =
        if (isSelected) size - SIZE_DIFF else size // Reduce size for selected state if necessary
    val padding = if (isSelected) BOX_PADDING else 0
    val radius = if (isSelected) INNER_RADIUS else cornerRadius

    Box(
        modifier =
            Modifier.size(size.dp)
                .clip(RoundedCornerShape(cornerRadius.dp))
                .border(
                    width = BORDER_WIDTH.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(cornerRadius.dp),
                )
                .clickable { onSelect(item) }
                .padding(padding.dp) // padding between outer box and inner box
    ) {
        Box(
            modifier =
                Modifier.size(innerSize.dp)
                    .clip(RoundedCornerShape(radius.dp))
                    .background(item.color)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ColorBoxPreview() {
    ColorBox(ColorBoxItem(color = Color.Red, isSelected = true), onSelect = {})
}
