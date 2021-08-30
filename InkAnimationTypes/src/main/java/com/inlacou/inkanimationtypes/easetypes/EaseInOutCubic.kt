package com.inlacou.inkanimationtypes.easetypes

import com.inlacou.inkanimationtypes.CubicBezier

/**
 * Created by Weiping on 2016/3/3.
 */
class EaseInOutCubic : CubicBezier() {
	init {
		init(0.645, 0.045, 0.355, 1.0)
	}
}