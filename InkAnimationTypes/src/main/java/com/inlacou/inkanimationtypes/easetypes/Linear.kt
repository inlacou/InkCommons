package com.inlacou.inkanimationtypes.easetypes

import android.view.animation.Interpolator
import com.inlacou.inkanimationtypes.CubicBezier

/**
 * Created by Weiping on 2016/3/3.
 */

class Linear : CubicBezier(), Interpolator {
	init {
		init(0f, 0f, 1f, 1f)
	}
	override fun getOffset(offset: Float): Float = offset
}
