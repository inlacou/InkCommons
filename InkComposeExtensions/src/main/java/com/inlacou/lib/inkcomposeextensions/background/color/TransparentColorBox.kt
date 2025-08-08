package com.inlacou.lib.inkcomposeextensions.background.color

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.scale
import com.inlacou.lib.inkcomposeextensions.Config
import com.inlacou.lib.inkcomposeextensions.R
import com.inlacou.lib.inkcomposeextensions.background.BORDER_WIDTH
import com.inlacou.lib.inkcomposeextensions.background.BOX_PADDING
import com.inlacou.lib.inkcomposeextensions.background.BOX_SIZE
import com.inlacou.lib.inkcomposeextensions.background.BoxItem
import com.inlacou.lib.inkcomposeextensions.background.INNER_RADIUS
import com.inlacou.lib.inkcomposeextensions.background.OUTER_RADIUS
import com.inlacou.lib.inkcomposeextensions.background.SIZE_DIFF
import com.inlacou.lib.inkcomposeextensions.background.TransparentBoxItem
import toPixel

@Composable
fun TransparentColorBox(
    item: TransparentBoxItem,
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
        Box(modifier = Modifier.size(innerSize.dp).clip(RoundedCornerShape(radius.dp))) {
            val tileSize = 8.dp
            val tile =
                resizeImageBitmap(
                    ImageBitmap.imageResource(id = R.drawable.checkers),
                    tileSize.value.toPixel(),
                )
            Canvas(modifier = Modifier.size(innerSize.dp)) {
                val count = innerSize / tileSize.value.toInt()
                for (row in 0..count) {
                    for (col in 0..count) {
                        // Draw the tile
                        drawImage(
                            image = tile,
                            topLeft = Offset(x = col * tileSize.toPx(), y = row * tileSize.toPx()),
                        )
                    }
                }
            }
        }
    }
}

private fun resizeImageBitmap(image: ImageBitmap, size: Int): ImageBitmap {
    // Convert ImageBitmap to Android Bitmap
    val originalBitmap = image.asAndroidBitmap()

    // Resize the Bitmap using Android utilities
    val resizedBitmap = originalBitmap.scale(size, size)

    // Convert back to ImageBitmap
    return resizedBitmap.asImageBitmap()
}

@Preview(showBackground = true)
@Composable
fun TransparentBoxPreview() {
    TransparentColorBox(TransparentBoxItem(isSelected = true), onSelect = {})
}
