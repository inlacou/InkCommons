package com.inlacou.inkanimationtypes

interface Interpolable {
	/**
	 * Transforms float to apply an interpolator
	 * Examples here: https://easings.net
	 * @param offset from 0.0 to 1.0
	 * @return float from 0.0 to 1.0
	 */
	fun getOffset(offset: Float): Float
}