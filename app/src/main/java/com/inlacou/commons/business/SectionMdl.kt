package com.inlacou.commons.business

data class SectionMdl(
    var hideable: Boolean,
    var textResId: Int? = null,
    var text: String? = null,
    var iconResId: Int? = null,
    var isTitle: Boolean = false,
    /**
     * how many dps
     */
    val paddingBot: Int = 16,
    /**
     * how many dps
     */
    val paddingTop: Int = 16
)