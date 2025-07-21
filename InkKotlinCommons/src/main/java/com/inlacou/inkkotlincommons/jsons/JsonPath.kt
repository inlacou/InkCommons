package com.inlacou.inkkotlincommons.jsons

import kotlin.jvm.JvmInline
import kotlin.math.min

@JvmInline
internal value class JsonPath(val s: String) : Comparable<JsonPath> {

    constructor(list: List<String>) : this("$.${list.reduce { acc, s -> "$acc.$s" }}")

    init {
        require(s.startsWith("$")) { "All JSONPATHs must start with $. Received: '$s'" }
    }

    /**
     * This is used to merge two paths where the first one will be truncated to match "$.actions[\D*]"
     * If that match is not found, then the second of the paths to merge will be returned instead, without modification.
     * This is used when, in some action/item converter we are generating resources that need to
     * be placed at a more surface lvl.
     * For example, we generate a resource to put the ID/UUID at some level, but the resource
     * must be at '$.actions[0].in.resources' or at '$.in.resources'.
     */
    fun mergePathsAtActionLevel(childPath: JsonPath): JsonPath {
        return JsonPath(Regex("\\\$\\.actions\\[[0-9]*\\]").find(this.s)?.groupValues?.get(0) ?: "$").mergePaths(childPath)
    }

    fun mergePaths(childPath: JsonPath): JsonPath {
        return when {
            this.isEmpty() && childPath.isEmpty() -> JsonPath("$.")
            this.isEmpty() && childPath.startsWith("$.") -> childPath
            this.s == "$." && childPath.startsWith("$.") -> childPath
            childPath.s == ("$.") && this.startsWith("$.") -> this
            this.startsWith("$.") && childPath.isEmpty() -> this
            this.s == "$." && childPath.startsWith("$.") -> childPath
            this.startsWith("$.") && childPath.startsWith("$.") -> this + childPath /*drops '$'*/
            else -> throw IllegalArgumentException("Don't know how to merge '${this.s}' and '${childPath.s}'")
        }
    }

    fun pathElements(): List<String> {
        var input = if (startsWith("$")) this.s.drop(1)
        else throw IllegalArgumentException("The receiver String '$this' is not a valid JSONPATH. JSONPATH format starts with '$'")
        val result = mutableListOf<String>()
        while (input.isNotEmpty()) {
            val start: Int
            var end: Int
            if (input.startsWith("[")) input = ".$input"
            if (input.startsWith(".R={") && input.contains("}")) {
                start = input.indexOf(".R={")
                end = input.indexOf("}.")
                if (end == -1) end = input.lastIndex
                end++
            } else {
                start = input.indexOf(".")
                end = input.indexOf(".", start + 1)
                if (end == -1) end = input.lastIndex + 1
            }
            result.add(input.substring(start, end).drop(1))
            input = input.removeRange(start, end)
        }
        return result
    }

    fun isEmpty() = s == "$" || s == "$."
    fun isNotEmpty() = s.isNotEmpty()
    fun startsWith(start: String) = s.startsWith(start)
    operator fun plus(jsonPath: JsonPath): JsonPath = JsonPath(s + jsonPath.s.drop(1) /*drops '$'*/)
    operator fun plus(string: String): JsonPath = this + JsonPath(string)

    override fun compareTo(other: JsonPath): Int {
        val a = this
        val b = other
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

    override fun toString(): String = s

    fun endsWith(end: String) = s.endsWith(end)
}
