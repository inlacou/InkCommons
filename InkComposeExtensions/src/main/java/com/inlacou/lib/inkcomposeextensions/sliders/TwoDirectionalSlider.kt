package com.inlacou.lib.inkcomposeextensions.sliders

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.inlacou.lib.inkcomposeextensions.Config
import isLandScape
import sliderGesture

@Composable
fun TwoDirectionalSlider(
    modifier: Modifier = Modifier,
    value: Int,
    onValueChange: (Int) -> Unit,
    onStopTrackingTouch: () -> Unit = {},
    name: String? = null,
    valueRange: IntRange = -50..50,
    inactiveColor: Color = Color(0xFF3b3b3b),
    activeColor: Color = Color(0xFFFFFFFF),
    valueColor: Color = Color(0xFFCCCCCC),
    nameColor: Color = Color(Config.colorConfig.textDefault),
    nameTextStyle: TextStyle? = null,
    valueTextStyle: TextStyle? = null,
) {
    val localTextStyle = LocalTextStyle.current

    val rememberedNameTextStyle =
        nameTextStyle
            ?: remember(localTextStyle) {
                localTextStyle
                    //.applyCascadeTypographyTheme(TypographyApiModel(Typography.T4, FontWeights.SEMI_BOLD))
            }

    val rememberedValueTextStyle =
        valueTextStyle
            ?: remember(localTextStyle) {
                localTextStyle
                    //.applyCascadeTypographyTheme(TypographyApiModel(Typography.T4, FontWeights.MEDIUM))
            }
    val landscape = isLandScape()
    val slider: @Composable (Modifier) -> Unit = { sliderModifier ->
        CustomTwoDirectionalSlider(
            modifier = sliderModifier,
            value = value,
            onValueChange = onValueChange,
            onStopTrackingTouch = onStopTrackingTouch,
            isVertical = landscape,
            valueRange = valueRange,
            inactiveColor = inactiveColor,
            activeColor = activeColor,
        )
    }
    if (landscape) {
        Column(
            modifier = modifier.defaultMinSize(minWidth = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(text = value.toString(), color = valueColor, style = rememberedValueTextStyle)
            slider(Modifier)
        }
    } else {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            name?.let {
                Text(
                    modifier = Modifier.width(75.dp),
                    text = name,
                    color = nameColor,
                    style = rememberedNameTextStyle,
                )
            }
            slider(Modifier.weight(1f))
            Text(
                modifier = Modifier.width(55.dp),
                text = value.toString(),
                textAlign = TextAlign.Center,
                color = valueColor,
                style = rememberedValueTextStyle,
            )
        }
    }
}

@Composable
fun CustomTwoDirectionalSlider(
    modifier: Modifier = Modifier,
    value: Int,
    onValueChange: (Int) -> Unit,
    onStopTrackingTouch: () -> Unit = {},
    isVertical: Boolean = false,
    valueRange: ClosedRange<Int> = -50..50,
    inactiveColor: Color = Color(0xFF3b3b3b),
    activeColor: Color = Color(0xFFFFFFFF),
    thumbRadius: Dp = 8.dp,
    zeroMarkerRadius: Dp = 4.dp,
    strokeSize: Dp = 1.dp,
) {
    val density = LocalDensity.current
    val thumbRadiusPx = with(density) { thumbRadius.toPx() }
    val zeroMarkerRadiusPx = with(density) { zeroMarkerRadius.toPx() }
    val strokePx = with(density) { strokeSize.toPx() }
    val lineActivePx = with(density) { 2.dp.toPx() }
    val lineBgPx = with(density) { 4.dp.toPx() }

    val coercedValue = value.coerceIn(valueRange.start, valueRange.endInclusive)

    BoxWithConstraints(modifier = modifier) {
        val mainAxisSize =
            if (isVertical) constraints.maxHeight.toFloat() else constraints.maxWidth.toFloat()
        val crossAxisSizeForCanvas = (thumbRadius.coerceAtLeast(zeroMarkerRadius)) * 2 + strokeSize

        val minRequiredMainAxisSize = (thumbRadiusPx * 2) + (zeroMarkerRadiusPx * 2)
        if (mainAxisSize < minRequiredMainAxisSize) {
            return@BoxWithConstraints
        }

        Canvas(
            modifier =
                Modifier.then(
                        if (isVertical) {
                            Modifier.fillMaxHeight().width(crossAxisSizeForCanvas)
                        } else {
                            Modifier.fillMaxWidth().height(crossAxisSizeForCanvas)
                        }
                    )
                    .sliderGesture(
                        isVertical = isVertical,
                        thumbRadiusPx = thumbRadiusPx,
                        valueRangeStart = valueRange.start,
                        valueRangeEnd = valueRange.endInclusive,
                        currentValue = value,
                        onValueChange = onValueChange,
                        onStopTrackingTouch = onStopTrackingTouch,
                    )
        ) {
            val actualCanvasMainAxisSize = if (isVertical) size.height else size.width
            val actualDraggableMainAxisSize = actualCanvasMainAxisSize - 2 * thumbRadiusPx

            if (actualDraggableMainAxisSize <= 0f) {
                return@Canvas
            }

            val actualValueToPxRatio =
                actualDraggableMainAxisSize / (valueRange.endInclusive - valueRange.start)
            val actualZeroPxOnCanvas = (-valueRange.start * actualValueToPxRatio) + thumbRadiusPx
            val actualThumbPxOnCanvas =
                if (isVertical) {
                    actualCanvasMainAxisSize -
                        ((coercedValue - valueRange.start) * actualValueToPxRatio + thumbRadiusPx)
                } else {
                    (coercedValue - valueRange.start) * actualValueToPxRatio + thumbRadiusPx
                }

            val inactiveStartOffset =
                if (isVertical) Offset(center.x, thumbRadiusPx) else Offset(thumbRadiusPx, center.y)
            val inactiveEndOffset =
                if (isVertical) Offset(center.x, actualCanvasMainAxisSize - thumbRadiusPx)
                else Offset(actualCanvasMainAxisSize - thumbRadiusPx, center.y)
            drawLine(
                color = inactiveColor,
                start = inactiveStartOffset,
                end = inactiveEndOffset,
                strokeWidth = lineBgPx,
                cap = StrokeCap.Round,
            )

            val activeStartMainAxisPx = actualZeroPxOnCanvas.coerceAtMost(actualThumbPxOnCanvas)
            val activeEndMainAxisPx = actualZeroPxOnCanvas.coerceAtLeast(actualThumbPxOnCanvas)

            val finalActiveStartMainAxisPx =
                activeStartMainAxisPx.coerceIn(
                    thumbRadiusPx,
                    actualCanvasMainAxisSize - thumbRadiusPx,
                )
            val finalActiveEndMainAxisPx =
                activeEndMainAxisPx.coerceIn(
                    thumbRadiusPx,
                    actualCanvasMainAxisSize - thumbRadiusPx,
                )

            if (coercedValue != 0) {
                if (finalActiveStartMainAxisPx != finalActiveEndMainAxisPx) {
                    val activeStartOffset =
                        if (isVertical) Offset(center.x, finalActiveStartMainAxisPx)
                        else Offset(finalActiveStartMainAxisPx, center.y)
                    val activeEndOffset =
                        if (isVertical) Offset(center.x, finalActiveEndMainAxisPx)
                        else Offset(finalActiveEndMainAxisPx, center.y)
                    drawLine(
                        color = activeColor,
                        start = activeStartOffset,
                        end = activeEndOffset,
                        strokeWidth = lineActivePx,
                        cap = StrokeCap.Round,
                    )
                }
            }

            val clampedZeroMarkerMainAxisPx =
                actualZeroPxOnCanvas.coerceIn(
                    zeroMarkerRadiusPx + strokePx / 2,
                    actualCanvasMainAxisSize - (zeroMarkerRadiusPx + strokePx / 2),
                )
            val zeroMarkerCenter =
                if (isVertical) Offset(center.x, clampedZeroMarkerMainAxisPx)
                else Offset(clampedZeroMarkerMainAxisPx, center.y)
            drawCircle(color = activeColor, radius = zeroMarkerRadiusPx, center = zeroMarkerCenter)
            drawCircle(
                color = inactiveColor,
                radius = zeroMarkerRadiusPx,
                center = zeroMarkerCenter,
                style = Stroke(width = strokePx),
            )

            val clampedThumbMainAxisPx =
                actualThumbPxOnCanvas.coerceIn(
                    thumbRadiusPx,
                    actualCanvasMainAxisSize - thumbRadiusPx,
                )
            val thumbCenter =
                if (isVertical) Offset(center.x, clampedThumbMainAxisPx)
                else Offset(clampedThumbMainAxisPx, center.y)
            drawCircle(color = activeColor, radius = thumbRadiusPx, center = thumbCenter)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, name = "Horizontal At Zero")
@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
    name = "Horizontal At Zero Landscape",
    device = "spec:parent=pixel_tablet,orientation=landscape",
)
@Composable
fun PreviewHorizontalSliderAtZero() {
    var sliderValue by remember { mutableIntStateOf(-30) }

    TwoDirectionalSlider(
        value = sliderValue,
        onValueChange = { sliderValue = it },
        name = "Slider",
        valueTextStyle = TextStyle(),
        nameTextStyle = TextStyle(),
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, name = "Horizontal Positive")
@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
    name = "Horizontal Positive Landscape",
    device = "spec:parent=pixel_tablet,orientation=landscape",
)
@Composable
fun PreviewHorizontalSliderPositive() {
    var sliderValue by remember { mutableIntStateOf(30) }
    TwoDirectionalSlider(
        value = sliderValue,
        onValueChange = { sliderValue = it },
        name = "Slider",
        valueTextStyle = TextStyle(),
        nameTextStyle = TextStyle(),
    )
}
