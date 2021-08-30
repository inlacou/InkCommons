package com.inlacou.inkanimationtypes.easetypes

import com.inlacou.inkanimationtypes.CubicBezier

/**
 * Created by Weiping on 2016/3/3.
 */
class EaseInOutBounce : CubicBezier() {
	override fun getOffset(t: Float): Float {
		val b = 0f
		val c = 1f
		val d = 1f
		return if (t < d / 2) easeInBounce(t * 2, 0f, c, d) * .5f + b else easeOutBounce(t * 2 - d, 0f, c, d) * .5f + c * .5f + b
	}

	private fun easeInBounce(t: Float, b: Float, c: Float, d: Float): Float {
		return c - easeOutBounce(d - t, 0f, c, d) + b
	}

	private fun easeOutBounce(t: Float, b: Float, c: Float, d: Float): Float {
		var t = t
		return if (d.let { t /= it; t } < 1 / 2.75f) {
			c * (7.5625f * t * t) + b
		} else if (t < 2 / 2.75f) {
			c * (7.5625f * (1.5f / 2.75f).let { t -= it; t } * t + .75f) + b
		} else if (t < 2.5 / 2.75) {
			c * (7.5625f * (2.25f / 2.75f).let { t -= it; t } * t + .9375f) + b
		} else {
			c * (7.5625f * (2.625f / 2.75f).let { t -= it; t } * t + .984375f) + b
		}
	}
}