package com.inlacou.inkanimationtypes.easetypes

import com.inlacou.inkanimationtypes.CubicBezier

/**
 * Created by Weiping on 2016/3/3.
 */
class EaseInOutElastic : CubicBezier() {
	override fun getOffset(t: Float): Float {
		var t = t
		val b = 0f
		val c = 1f
		val d = 1f
		if (t == 0f) return b
		if (d / 2.let { t /= it; t } == 2f) return b + c
		val p = d * (.3f * 1.5f)
		val s = p / 4
		return if (t < 1) -.5f * (c * Math.pow(2.0, 10 * 1.let { t -= it; t }.toDouble()).toFloat() * Math.sin((t * d - s) * (2 * Math.PI.toFloat()) / p.toDouble()).toFloat()) + b else c * Math.pow(2.0, -10 * 1.let { t -= it; t }.toDouble()).toFloat() * Math.sin((t * d - s) * (2 * Math.PI.toFloat()) / p.toDouble()).toFloat() * .5f + c + b
	}
}