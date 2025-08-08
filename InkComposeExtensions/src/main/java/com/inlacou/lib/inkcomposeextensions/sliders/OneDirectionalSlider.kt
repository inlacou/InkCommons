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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inlacou.lib.inkcomposeextensions.Config
import isLandScape
import sliderGesture

@Composable
fun OneDirectionalSlider(
    modifier: Modifier = Modifier,
    value: Int,
    onValueChange: (Int) -> Unit,
    onStopTrackingTouch: () -> Unit = {},
    name: String? = null,
    valueRange: IntRange = 0..100,
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
        CustomOneDirectionalSlider(
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
fun CustomOneDirectionalSlider(
    modifier: Modifier = Modifier,
    value: Int,
    onValueChange: (Int) -> Unit,
    onStopTrackingTouch: () -> Unit = {},
    isVertical: Boolean = false,
    valueRange: IntRange = 0..100,
    inactiveColor: Color = Color(0xFF3b3b3b),
    activeColor: Color = Color(0xFFFFFFFF),
    thumbRadius: Dp = 8.dp,
    strokeSize: Dp = 1.dp,
) {
    val density = LocalDensity.current
    val thumbRadiusPx = with(density) { thumbRadius.toPx() }
    val lineActivePx = with(density) { 2.dp.toPx() }
    val lineBgPx = with(density) { 4.dp.toPx() }

    val coercedValue = value.coerceIn(valueRange.first, valueRange.last)

    BoxWithConstraints(modifier = modifier) {
        val mainAxisSize =
            if (isVertical) constraints.maxHeight.toFloat() else constraints.maxWidth.toFloat()
        val crossAxisSizeForCanvas = thumbRadius * 2 + strokeSize

        if (mainAxisSize < thumbRadiusPx * 2) return@BoxWithConstraints

        Canvas(
            modifier =
                Modifier.then(
                        if (isVertical) Modifier.fillMaxHeight().width(crossAxisSizeForCanvas)
                        else Modifier.fillMaxWidth().height(crossAxisSizeForCanvas)
                    )
                    .sliderGesture(
                        isVertical = isVertical,
                        thumbRadiusPx = thumbRadiusPx,
                        valueRangeStart = valueRange.first,
                        valueRangeEnd = valueRange.last,
                        currentValue = value,
                        onValueChange = onValueChange,
                        onStopTrackingTouch = onStopTrackingTouch,
                    )
        ) {
            val canvasMainAxis = if (isVertical) size.height else size.width
            val usableAxisSize = canvasMainAxis - 2 * thumbRadiusPx

            val pxRatio = usableAxisSize / (valueRange.last - valueRange.first)
            val thumbPositionPx =
                if (isVertical) {
                    canvasMainAxis - ((coercedValue - valueRange.first) * pxRatio + thumbRadiusPx)
                } else {
                    (coercedValue - valueRange.first) * pxRatio + thumbRadiusPx
                }

            val trackStart =
                if (isVertical) Offset(center.x, thumbRadiusPx) else Offset(thumbRadiusPx, center.y)
            val trackEnd =
                if (isVertical) Offset(center.x, canvasMainAxis - thumbRadiusPx)
                else Offset(canvasMainAxis - thumbRadiusPx, center.y)

            drawLine(
                color = inactiveColor,
                start = trackStart,
                end = trackEnd,
                strokeWidth = lineBgPx,
                cap = StrokeCap.Round,
            )

            val activeStart =
                if (isVertical) Offset(center.x, size.height - thumbRadiusPx)
                else Offset(thumbRadiusPx, center.y)
            val activeEnd =
                if (isVertical) Offset(center.x, thumbPositionPx)
                else Offset(thumbPositionPx, center.y)

            drawLine(
                color = activeColor,
                start = activeStart,
                end = activeEnd,
                strokeWidth = lineActivePx,
                cap = StrokeCap.Round,
            )

            val thumbCenter =
                if (isVertical) Offset(center.x, thumbPositionPx)
                else Offset(thumbPositionPx, center.y)

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
fun PreviewHorizontalOneDirectionalSliderAtZero() {
    var sliderValue by remember { mutableIntStateOf(0) }

    OneDirectionalSlider(
        value = sliderValue,
        onValueChange = { sliderValue = it },
        name = "Slider",
        valueTextStyle = valueTextStyle,
        nameTextStyle = nameTextStyle,
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
fun PreviewHorizontalOneDirectionalSliderPositive() {
    var sliderValue by remember { mutableIntStateOf(30) }
    OneDirectionalSlider(
        value = sliderValue,
        onValueChange = { sliderValue = it },
        name = "Slider",
        valueTextStyle = valueTextStyle,
        nameTextStyle = nameTextStyle,
    )
}

private val valueTextStyle by lazy { TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium) }
private val nameTextStyle by lazy { TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold) }
