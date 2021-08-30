package com.inlacou.inkanimationtypes.easetypes

import com.inlacou.inkanimationtypes.CubicBezier

/**
 * Created by Weiping on 2016/3/3.
 */

class NoEase : CubicBezier() {
	init {
		init(0f, 0f, 1f, 1f)
	}
	override fun getOffset(offset: Float): Float = 1f
}
