package com.inlacou.inkanimationtypes

import android.graphics.PointF
import android.view.animation.Interpolator
import kotlin.math.abs

/**
 * Created by Weiping on 2016/3/3.
 */
abstract class CubicBezier: Interpolable, Interpolator {

	private lateinit var start: PointF
	private lateinit var end: PointF
	private val a = PointF()
	private val b = PointF()
	private val c = PointF()

	/**
	 * init the 4 values of the cubic-bezier
	 * @param startX x of start
	 * @param startY y of start
	 * @param endX x of end
	 * @param endY y of end
	 */
	fun init(startX: Float, startY: Float, endX: Float, endY: Float) {
		start = PointF(startX, startY)
		end = PointF(endX, endY)
	}

	fun init(startX: Double, startY: Double, endX: Double, endY: Double) {
		init(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat())
	}
	
	override fun getInterpolation(p0: Float): Float = getOffset(p0)
	
	override fun getOffset(offset: Float): Float {
		return getBezierCoordinateY(getXForTime(offset))
	}

	private fun getBezierCoordinateY(time: Float): Float {
		c.y = 3 * start.y
		b.y = 3 * (end.y - start.y) - c.y
		a.y = 1f - c.y - b.y
		return time * (c.y + time * (b.y + time * a.y))
	}

	private fun getXForTime(time: Float): Float {
		var x = time
		var z: Float
		for (i in 1..13) {
			z = getBezierCoordinateX(x) - time
			if (abs(z) < 1e-3) {
				break
			}
			x -= z / getXDerivate(x)
		}
		return x
	}

	private fun getXDerivate(t: Float): Float {
		return c.x + t * (2 * b.x + 3f * a.x * t)
	}

	private fun getBezierCoordinateX(time: Float): Float {
		c.x = 3 * start.x
		b.x = 3 * (end.x - start.x) - c.x
		a.x = 1f - c.x - b.x
		return time * (c.x + time * (b.x + time * a.x))
	}
}
