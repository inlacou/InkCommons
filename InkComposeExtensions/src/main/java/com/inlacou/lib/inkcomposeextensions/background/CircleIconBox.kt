package com.inlacou.lib.inkcomposeextensions.background

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
fun CircleIconBox(
    item: CircleIconBoxItem,
    backgroundColor: Color = Color(Config.colorConfig.backgroundDark),
    iconTintColor: Color = Color(Config.colorConfig.iconTint),
    onSelect: (BoxItem) -> Unit
) {
    Box(
        modifier =
            Modifier.Companion.size(BOX_SIZE.dp)
                .clip(CircleShape)
                .background(backgroundColor)
                .clickable { onSelect(item) }
                .padding(0.dp),
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
fun CircleIconBoxPreview() {
    CircleIconBox(
        item = CircleIconBoxItem(Utils.icon1),
        onSelect = {}
    )
}
