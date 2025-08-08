package com.inlacou.lib.inkcomposeextensions.background.gradient

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inlacou.lib.inkcomposeextensions.Config
import com.inlacou.lib.inkcomposeextensions.DefaultColors
import com.inlacou.lib.inkcomposeextensions.background.BORDER_WIDTH
import com.inlacou.lib.inkcomposeextensions.background.BOX_PADDING
import com.inlacou.lib.inkcomposeextensions.background.BOX_SIZE
import com.inlacou.lib.inkcomposeextensions.background.BoxItem
import com.inlacou.lib.inkcomposeextensions.background.GradientColorBoxItem
import com.inlacou.lib.inkcomposeextensions.background.INNER_RADIUS
import com.inlacou.lib.inkcomposeextensions.background.OUTER_RADIUS
import com.inlacou.lib.inkcomposeextensions.background.SIZE_DIFF
import px
import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GradientColorBox(
    item: GradientColorBoxItem,
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
        Box(
            modifier =
                Modifier.size(innerSize.dp)
                    .clip(RoundedCornerShape(radius.dp))
                    .background(
                        brush =
                            getLinearGradientBrushTrigonometric(
                                width = innerSize.px.toFloat(),
                                height = innerSize.px.toFloat(),
                                colors = item.colors,
                                angle = item.angle.toDouble(),
                            )
                    )
        )
    }
}

/**
 * Creates a linear gradient brush using normalized trigonometric functions. This approach is
 * simpler, more performant, and provides excellent results for standard UI gradient needs.
 *
 * Algorithm:
 * 1. Normalizes sin/cos values from [-1,1] to [0,1] using (value + 1) / 2
 * 2. Multiplies by width/height to get pixel coordinates
 * 3. Uses angle + 180° for the opposite endpoint
 * 4. Creates edge-to-edge gradient line
 *
 * Benefits:
 * - Simple and direct calculation
 * - High performance (minimal computational overhead)
 * - Self-contained (no external dependencies)
 * - Excellent for UI gradients
 *
 * @param width The width of the gradient area
 * @param height The height of the gradient area
 * @param colors List of colors for the gradient
 * @param angle Angle in degrees for the gradient direction
 * @return Brush.linearGradient configured with proper start/end points
 */
private fun getLinearGradientBrushTrigonometric(
    width: Float,
    height: Float,
    colors: List<Color>,
    angle: Double,
): Brush {
    val angleRad = toRadians(angle)
    val oppositeAngleRad = toRadians(angle + 180)

    // Normalize trigonometric values from [-1,1] to [0,1] range
    val startX = width * (sin(angleRad) + 1).toFloat() / 2
    val startY = height * (cos(angleRad) + 1).toFloat() / 2
    val endX = width * (sin(oppositeAngleRad) + 1).toFloat() / 2
    val endY = height * (cos(oppositeAngleRad) + 1).toFloat() / 2

    return Brush.linearGradient(
        colors = colors,
        start = Offset(startX, startY),
        end = Offset(endX, endY),
    )
}

@Preview(showBackground = true)
@Composable
fun GradientColorBoxPreview() {
    GradientColorBox(
        GradientColorBoxItem(
            colors = listOf(Color.Yellow, Color.Blue),
            angle = 45,
            isSelected = true,
        ),
        borderColor = Color(DefaultColors.textSelected),
        onSelect = {},
    )
}
