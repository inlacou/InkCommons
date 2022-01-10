package com.inlacou.commons.business

import com.inlacou.inkbetterandroidviews.spinners.BetterSpinner

data class Tag(
		var name: String,
		var typeName: String?,
		/**
		 * #FFFFFFFF
		 * # + alpha (2) + R (2) + G (2) + B (2)
		 */
		var colorHexadecimal: String,
		var iconResourceName: String,
): BetterSpinner.ComplexItem {
	override val display: String get() = name

	override fun filter(s: String): Boolean {
		return name.contains(s, ignoreCase = true) || typeName?.contains(s, ignoreCase = true)==true
	}
}