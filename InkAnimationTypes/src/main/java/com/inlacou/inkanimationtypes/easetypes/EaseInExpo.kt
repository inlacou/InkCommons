package com.inlacou.inkanimationtypes.easetypes

import com.inlacou.inkanimationtypes.CubicBezier

/**
 * Created by Weiping on 2016/3/3.
 */
class EaseInExpo : CubicBezier() {
	init {
		init(0.95, 0.05, 0.795, 0.035)
	}
}