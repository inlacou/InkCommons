package com.inlacou.inkkotlincommons.jsons

import kotlin.math.min

/**
 * Custom comparator for String (until we have the JSONPATH inline class) needed because '$.anchor[9]' should come before '$.anchor[10]' and by simply comparing
 *  Strings with default comparator that would not be true.
 */
internal object JsonPathComparator : Comparator<JsonPath> {
    override fun compare(a: JsonPath, b: JsonPath): Int {
        if (a == b) return 0
        val t1 = a.jsonPathSplitByArrayIndex()
        val t2 = b.jsonPathSplitByArrayIndex()
        repeat(min(t1.size, t2.size)) {
            val s1 = t1[it]
            val s2 = t2[it]
            if (s1 != s2) {
                val n1 = s1.toIntOrNull()
                val n2 = s2.toIntOrNull()
                return (if (n1 != null && n2 != null) n1 < n2 else s1 < s2).let {
                    if (it) -1 else 1
                }
            }
        }
        return 1
    }
}

/**
 * We only have to split if the number is inside '[]' and just before a '.'
 */
internal fun JsonPath.jsonPathSplitByArrayIndex(): List<String> {
    val match = Regex("\\[\\d+\\](\\.|\$)").find(this.toString())
    return if (match != null) {
        val indexOfMatch = toString().indexOf(match.value)
        val matchStartMod = 1 /* [ */
        val matchEndMod = if (match.value.endsWith(".")) 2 /* ]. */ else 1 /* ] */
        listOf(
            toString().substring(0, indexOfMatch + matchStartMod),
            toString().substring(indexOfMatch + matchStartMod, indexOfMatch + match.value.length - matchEndMod),
            toString().substring(indexOfMatch + match.value.length - matchEndMod)
        )
    } else listOf(this.toString())
}