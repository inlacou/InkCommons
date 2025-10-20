package com.inlacou.lib.inkcomposeextensions.background.gradient

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.inlacou.lib.inkcomposeextensions.background.ColorBoxItem
import com.inlacou.lib.inkcomposeextensions.background.GRADIENT_ANGLE_RANGE_END
import com.inlacou.lib.inkcomposeextensions.background.GRADIENT_ANGLE_RANGE_START
import com.inlacou.lib.inkcomposeextensions.background.GradientColorBoxItem
import com.inlacou.lib.inkcomposeextensions.background.ITEM_PADDING
import com.inlacou.lib.inkcomposeextensions.background.OneRowHorizontalScroll
import com.inlacou.lib.inkcomposeextensions.background.color.ColorBox
import com.inlacou.lib.inkcomposeextensions.sliders.CustomTwoDirectionalSlider

@Composable
fun GradientPanel(
    modifier: Modifier = Modifier,
    items: List<GradientColorBoxItem>,
    gradientAngle: Int,
    gradientStartColor: Int,
    gradientEndColor: Int,
    onGradientAngleChanged: (Int) -> Unit,
    onStopTrackingTouch: () -> Unit,
    gradientColorSelectListener: (startColor: Int, endColor: Int, angle: Int) -> Unit,
    startColorClick: (color: Int) -> Unit,
    endColorClick: (color: Int) -> Unit,
) {

    val range = remember { GRADIENT_ANGLE_RANGE_START..GRADIENT_ANGLE_RANGE_END }
    Column(
        modifier = modifier.padding(vertical = ITEM_PADDING.dp, horizontal = (2 * ITEM_PADDING).dp)
    ) {
        Row {
            OneRowHorizontalScroll(
                items,
                selectedItemIndex =
                    items
                        .map { it.colors }
                        .indexOfFirst {
                            it.first().toArgb() == gradientStartColor &&
                                it.last().toArgb() == gradientEndColor
                        },
                onItemSelected = { boxItem ->
                    val colors = (boxItem as GradientColorBoxItem).colors
                    gradientColorSelectListener(
                        colors.first().toArgb(),
                        colors.last().toArgb(),
                        items.indexOf(boxItem),
                    )
                    onGradientAngleChanged(boxItem.angle)
                },
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = ITEM_PADDING.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ColorBox(ColorBoxItem(Color(gradientStartColor))) {
                startColorClick(gradientStartColor)
            }

            CustomTwoDirectionalSlider(
                modifier = Modifier.weight(1f).padding(horizontal = (ITEM_PADDING * 2).dp),
                value = gradientAngle,
                onValueChange = onGradientAngleChanged,
                onStopTrackingTouch = onStopTrackingTouch,
                valueRange = range,
            )

            ColorBox(ColorBoxItem(Color(gradientEndColor))) { endColorClick(gradientEndColor) }
        }
    }
}
