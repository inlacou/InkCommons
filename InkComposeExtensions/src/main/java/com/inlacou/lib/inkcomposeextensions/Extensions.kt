import android.content.res.Configuration
import android.content.res.Resources
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput

@Composable fun isLandScape() = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
@Composable fun isPortrait() = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

/**
 * Converts pixels(int) to dp
 */
val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

/**
 * Converts dp(int) to pixels
 */
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

/**
 * Converts dp(float) to pixels
 */
fun Float.toPixel(): Int {
    val metrics = Resources.getSystem().displayMetrics
    return (this * metrics.density).toInt()
}

suspend fun LazyListState.scrollToIndexIfNotVisible(index: Int?) {
    index ?: return
    snapshotFlow { layoutInfo.visibleItemsInfo }.filter { it.isNotEmpty() }.first()

    if (!layoutInfo.visibleItemsInfo.any { it.index == index }) {
        animateScrollToItem(index)
    }
}

internal fun Modifier.sliderGesture(
    isVertical: Boolean,
    thumbRadiusPx: Float,
    valueRangeStart: Int,
    valueRangeEnd: Int,
    currentValue: Int,
    onValueChange: (Int) -> Unit,
    onStopTrackingTouch: () -> Unit,
): Modifier =
    this.pointerInput(valueRangeStart, valueRangeEnd, thumbRadiusPx, isVertical) {
        fun positionToValue(position: Offset): Int {
            val canvasAxisSize = if (isVertical) size.height else size.width
            val changePos = if (isVertical) canvasAxisSize - position.y else position.x
            val draggableMainAxisSize = canvasAxisSize - 2 * thumbRadiusPx
            if (draggableMainAxisSize <= 0) return currentValue

            val posPx = (changePos - thumbRadiusPx).coerceIn(0f, draggableMainAxisSize)
            val pxRatio = draggableMainAxisSize / (valueRangeEnd - valueRangeStart)
            val newValue = ((posPx / pxRatio) + valueRangeStart).toInt()
            return newValue.coerceIn(valueRangeStart, valueRangeEnd)
        }

        awaitEachGesture {
            val down = awaitFirstDown()

            val initialValue = positionToValue(down.position)
            onValueChange(initialValue)

            drag(down.id) { change ->
                val newValue = positionToValue(change.position)
                onValueChange(newValue)
                change.consume()
            }

            onStopTrackingTouch()
        }
    }
