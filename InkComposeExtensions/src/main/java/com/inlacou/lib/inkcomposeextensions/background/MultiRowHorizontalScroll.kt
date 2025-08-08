package com.inlacou.lib.inkcomposeextensions.background

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inlacou.lib.inkcomposeextensions.Colors
import com.inlacou.lib.inkcomposeextensions.Utils

/**
 * A horizontally scrollable grid component that displays items in multiple rows.
 *
 * This component organizes items into columns that can be scrolled horizontally, with each column
 * containing a configurable number of rows. It's commonly used for displaying collections of
 * selectable items like colors, textures, or icons in a compact, scrollable format.
 *
 * @param items List of BoxItem objects to display
 * @param selectedItemIndex Index of the currently selected item
 * @param onItemSelected Callback triggered when an item is selected
 * @param itemsPerRow Number of items to display per column (default: 2, must be > 1)
 *
 * Example usage:
 * ```
 * MultiRowHorizontalScroll(
 *     items = colorItems,
 *     selectedItemIndex = 0,
 *     onItemSelected = { item -> handleSelection(item) },
 *     itemsPerRow = 2
 * )
 * ```
 */
@Composable
fun MultiRowHorizontalScroll(
    items: List<BoxItem>,
    selectedItemIndex: Int,
    onItemSelected: (BoxItem) -> Unit = {},
    itemsPerRow: Int = ITEMS_PER_ROW,
) {
    assert(itemsPerRow > 1)
    LazyRow(
        modifier = Modifier.fillMaxWidth().background(Color(Colors.TRANSPARENT)),
        horizontalArrangement = Arrangement.spacedBy(ITEM_PADDING.dp),
    ) {
        // This lambda computes the total number of columns required for the layout based on the
        // number of items and items per row
        itemsIndexed(items.chunked(itemsPerRow)) { itemsIndex, rowItems ->
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(ITEM_PADDING.dp),
            ) {
                rowItems.forEachIndexed { index, item ->
                    val actualIndex = items.indexOf(item)
                    renderBoxItem(
                        item = item,
                        actualIndex == selectedItemIndex,
                        onSelect = onItemSelected,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TwoRowHorizontalScrollPreview() {
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
    MultiRowHorizontalScroll(
        items =
            listOf(
                TransparentBoxItem(),
                CircleIconBoxItem(Utils.icon1),
                RoundedIconBoxItem(Utils.icon2),
            ) + colors.map { ColorBoxItem(it) },
        selectedItemIndex = 2,
    )
}
