package com.inlacou.inkanimationtypes.easetypes

import com.inlacou.inkanimationtypes.CubicBezier

/**
 * Created by Weiping on 2016/3/3.
 */
class EaseOutExpo : CubicBezier() {
	init {
		init(0.19, 1.0, 0.22, 1.0)
	}
}