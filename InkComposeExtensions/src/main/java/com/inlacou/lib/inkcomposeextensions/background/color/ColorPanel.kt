package com.inlacou.lib.inkcomposeextensions.background.color

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.inlacou.lib.inkcomposeextensions.background.BoxItem
import com.inlacou.lib.inkcomposeextensions.background.CircleIconBoxItem
import com.inlacou.lib.inkcomposeextensions.background.CircleImageBoxItem
import com.inlacou.lib.inkcomposeextensions.background.ColorBoxItem
import com.inlacou.lib.inkcomposeextensions.background.ITEM_PADDING
import com.inlacou.lib.inkcomposeextensions.background.MultiRowHorizontalScroll
import com.inlacou.lib.inkcomposeextensions.background.TransparentBoxItem

@Composable
fun ColorPanel(
    modifier: Modifier = Modifier,
    items: List<BoxItem>,
    selectedPosition: Int,
    colorSelectedListener: OnColorSelectedListener,
) {
    Row(
        modifier = modifier.padding(vertical = ITEM_PADDING.dp, horizontal = (2 * ITEM_PADDING).dp)
    ) {
        MultiRowHorizontalScroll(
            items = items,
            selectedItemIndex = selectedPosition,
            onItemSelected = { item ->
                when (item) {
                    is ColorBoxItem ->
                        colorSelectedListener.onColorSelected(
                            item.color.toArgb(),
                            items.indexOf(item),
                            ColorData.DataType.COLOR,
                            true,
                        )
                    is TransparentBoxItem ->
                        colorSelectedListener.onColorSelected(
                            item.color.toArgb(),
                            items.indexOf(item),
                            ColorData.DataType.COLOR,
                            true,
                        )
                    is CircleImageBoxItem -> colorSelectedListener.onPickerSelected()
                    is CircleIconBoxItem -> colorSelectedListener.onDropperSelected()
                    else -> Unit
                }
            },
        )
    }
}
