package com.inlacou.inkanimationtypes.easetypes

import com.inlacou.inkanimationtypes.CubicBezier

/**
 * Created by Weiping on 2016/3/3.
 */
class EaseInBack : CubicBezier() {
	init {
		init(0.6, -0.28, 0.735, 0.045)
	}
}