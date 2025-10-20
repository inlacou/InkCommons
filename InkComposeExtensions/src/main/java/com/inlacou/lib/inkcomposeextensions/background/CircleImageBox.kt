package com.inlacou.lib.inkcomposeextensions.background

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inlacou.lib.inkcomposeextensions.Colors
import com.inlacou.lib.inkcomposeextensions.Utils

@Composable
fun CircleImageBox(item: CircleImageBoxItem, onSelect: (BoxItem) -> Unit) {
    Box(
        modifier =
            Modifier.Companion.size(BOX_SIZE.dp)
                .clip(CircleShape)
                .background(Color(Colors.TRANSPARENT))
                .clickable { onSelect(item) },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier.Companion.size(BOX_SIZE.dp),
            painter = painterResource(item.icon),
            contentDescription = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CircleImageBoxPreview() {
    CircleImageBox(
        item = CircleImageBoxItem(Utils.icon1),
        onSelect = {}
    )
}
