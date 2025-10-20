package com.inlacou.lib.inkcomposeextensions.background

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inlacou.lib.inkcomposeextensions.Config
import com.inlacou.lib.inkcomposeextensions.Utils

@Composable
fun RoundedIconBox(
    item: RoundedIconBoxItem,
    backgroundColor: Color = Color(Config.colorConfig.backgroundDark),
    iconTintColor: Color = Color(Config.colorConfig.iconTint),
    borderColor: Color = Color(Config.colorConfig.borderDark),
    onSelect: (BoxItem) -> Unit
) {
    val isSelected = item.isSelected // Use the item's isSelected state
    val cornerRadius = OUTER_RADIUS
    val borderColor =
        if (isSelected) borderColor
        else Color.Transparent
    val padding = if (isSelected) BOX_PADDING else 0
    Box(
        modifier =
            Modifier.Companion.size(BOX_SIZE.dp)
                .clip(RoundedCornerShape(OUTER_RADIUS.dp))
                .background(backgroundColor)
                .clickable { onSelect(item) }
                .border(
                    width = BORDER_WIDTH.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(cornerRadius.dp),
                )
                .padding(padding.dp), // padding between outer box and inner box,
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.Companion.size(ICON_SIZE.dp),
            painter = painterResource(item.icon),
            contentDescription = null,
            tint = iconTintColor,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RoundedIconBoxPreview() {
    RoundedIconBox(
        item = RoundedIconBoxItem(Utils.icon1, isSelected = true),
        onSelect = {}
    )
}
