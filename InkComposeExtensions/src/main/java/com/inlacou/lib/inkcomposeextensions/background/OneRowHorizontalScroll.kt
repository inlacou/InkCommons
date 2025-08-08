package com.inlacou.lib.inkcomposeextensions.background

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OneRowHorizontalScroll(
    items: List<BoxItem>,
    selectedItemIndex: Int,
    onItemSelected: (BoxItem) -> Unit = {},
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ITEM_PADDING.dp),
    ) {
        // This lambda computes the total number of columns required for the layout based on the
        // number of colors and items per row
        items(items) { item ->
            val actualIndex = items.indexOf(item)
            renderBoxItem(item = item, actualIndex == selectedItemIndex, onSelect = onItemSelected)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OneRowHorizontalScrollPreview() {
    val colors =
        listOf(
            Color.Red,
            Color.Green,
            Color.Blue,
            Color.Yellow,
            Color.Cyan,
            Color.Magenta,
            Color.Gray,
            Color.Black,
        )
    val items = colors.chunked(2).map { GradientColorBoxItem(it) }
    OneRowHorizontalScroll(items = items, 0)
}
