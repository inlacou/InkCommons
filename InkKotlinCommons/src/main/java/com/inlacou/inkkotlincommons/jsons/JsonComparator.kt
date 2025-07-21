package com.inlacou.inkkotlincommons.jsons

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.math.max

/**
 * This class is used to compare two JSONs and produce a side by side comparison as String.
 *
 * This way you can directly see differences between expected JSON and actual JSON on a failing test.
 *
 * @author Iñigo Lacoume
 */
internal class JsonComparator(
    private val left: JsonObject,
    private val right: JsonObject,
    private val indent: String = "    ",
    private val maxLineLength: Int = 120,
    private val normalColorLeft: String = "\u001B[36m", // Cyan
    private val normalColorRight: String = "\u001B[34m", // Blue
    private val differentColor: String = "\u001B[31m", // Red
    private val notFoundColor: String = "\u001B[33m", // Yellow
    /**
     * I suggest not to change this one.
     */
    private val resetColor: String = "\u001B[0m", // Reset
) {

    override fun toString(): String = printCompare(normalColorLeft, left, normalColorRight, right) // jsonToString(left, right)

    private fun printCompare(
        color1: String,
        s1: JsonObject,
        color2: String,
        s2: JsonObject,
        equalFunc: ((key: JsonPath, expVal: JsonElement, actVal: JsonElement) -> Boolean?)? = null
    ): String {
        val margin = 2
        val longestLineLength = max(s1.toPrettyString().lines().maxOf { it.length }, s2.toPrettyString().lines().maxOf { it.length })
        return printJsonsInternal(longestLineLength + margin, color1, color2, 0, "", s1, s2, JsonPath("$."), equalFunc)
    }

    private fun compareJsonPrimitive(
        longest: Int,
        color1: String,
        color2: String,
        key: String,
        tabLvl: Int,
        expected: JsonPrimitive?,
        actual: JsonPrimitive?,
        inheritedPath: JsonPath,
        equalFunc: ((key: JsonPath, expVal: JsonElement, actVal: JsonElement) -> Boolean?)? = null
    ): String {
        val equalFuncResult = equalFunc?.invoke(inheritedPath, expected ?: JsonPrimitive(""), actual ?: JsonPrimitive(""))
        var color1 = color1
        var color2 = color2
        if (equalFuncResult == false || (equalFuncResult == null && expected != actual)) {
            if (expected == null || actual == null) {
                color1 = notFoundColor
                color2 = notFoundColor
            } else {
                color1 = differentColor
                color2 = differentColor
            }
        }
        return compareLine(
            color1,
            if (inheritedPath.endsWith("]")) "" else """"$key": """,
            ",",
            expected?.toString() ?: "",
            color2,
            actual?.toString() ?: "",
            longest,
            tabLvl,
        )
    }

    private fun printJsonsInternal(
        longest: Int,
        color1: String,
        color2: String,
        tabLvl: Int = 0,
        key: String,
        expected: JsonElement?,
        actual: JsonElement?,
        inheritedPath: JsonPath,
        equalFunc: ((key: JsonPath, expVal: JsonElement, actVal: JsonElement) -> Boolean?)? = null
    ): String {
        return when {
            expected is JsonPrimitive? && actual is JsonPrimitive? -> { compareJsonPrimitive(longest, color1, color2, key, tabLvl, expected, actual, inheritedPath, equalFunc) }
            expected is JsonObject? && actual is JsonObject? -> {
                val keysOnlyInExpected = (expected?.keys ?: listOf()) - (actual?.keys ?: listOf()).toSet()
                val keysOnlyInActual = (actual?.keys ?: listOf()) - (expected?.keys ?: listOf()).toSet()
                val keysInBoth = (expected?.keys ?: listOf()) - keysOnlyInExpected.toSet()
                if (keysOnlyInExpected.isNotEmpty() || keysOnlyInActual.isNotEmpty() || keysInBoth.isNotEmpty()) {
                    val start = (if (key.isNotEmpty() && !inheritedPath.endsWith("]")) """"$key": {""" else """{""")
                    var result = if (keysInBoth.isNotEmpty() || (keysOnlyInExpected.isNotEmpty() && keysOnlyInActual.isNotEmpty())) compareLine(color1, start, color2, start, longest, tabLvl) + "\n"
                    else if (keysOnlyInExpected.isNotEmpty()) compareLine(color1, start, color2, "", longest, tabLvl) + "\n"
                    else if (keysOnlyInActual.isNotEmpty()) compareLine(color1, "", color2, start, longest, tabLvl) + "\n"
                    else throw Exception("'keysOnlyInActual' is empty, and 'keysOnlyInExpected' is empty, and 'keysInBoth' is empty. We have nothing to work with here.")
                    keysInBoth.forEach { newKey ->
                        result += printJsonsInternal(longest, color1, color2, tabLvl + 1, newKey, expected!![newKey], actual!![newKey], inheritedPath.mergePaths(
                            JsonPath("$.$newKey")
                        ), equalFunc) +
                            "\n"
                    }
                    keysOnlyInExpected.forEach { newKey ->
                        result += printJsonsInternal(longest, color1, color2, tabLvl + 1, newKey, expected!![newKey], null, inheritedPath.mergePaths(
                            JsonPath("$.$newKey")
                        ), equalFunc) +
                            "\n"
                    }
                    keysOnlyInActual.forEach { newKey ->
                        result += printJsonsInternal(longest, color1, color2, tabLvl + 1, newKey, null, actual!![newKey], inheritedPath.mergePaths(
                            JsonPath("$.$newKey")
                        ), equalFunc) +
                            "\n"
                    }
                    result += compareLine(
                        color1,
                        if (keysInBoth.isNotEmpty() || keysOnlyInExpected.isNotEmpty()) "}," else "",
                        color2,
                        if (keysInBoth.isNotEmpty() || keysOnlyInActual.isNotEmpty()) "}," else "",
                        longest,
                        tabLvl
                    )
                    result.removeLast(",")
                    result
                } else {
                    compareLine(
                        color1,
                        if (expected != null) """"$key": {}""" else "",
                        color2,
                        if (actual != null) """"$key": {}""" else "",
                        longest,
                        tabLvl,
                    )
                }
            }
            expected is JsonArray? && actual is JsonArray? -> {
                if (max(expected?.size ?: 0, actual?.size ?: 0) > 0) {
                    val start = (if (key.isNotEmpty()) """"$key": [""" else """"[""")
                    var result = compareLine(color1, if (expected == null) "" else start, color2, if (actual == null) "" else start, longest, tabLvl)
                    result += "\n"
                    repeat(max(expected?.size ?: 0, actual?.size ?: 0)) {
                        result += printJsonsInternal(
                            longest,
                            color1,
                            color2,
                            tabLvl + 1,
                            key,
                            expected?.getOrNull(it),
                            actual?.getOrNull(it),
                            inheritedPath.mergePaths(JsonPath("$.[$it]")), equalFunc
                        ) + "\n"
                    }
                    result += compareLine(color1, if (expected == null) "" else "],", color2, if (actual == null) "" else "],", longest, tabLvl)
                    result.removeLast(",")
                    result
                } else {
                    val start = (if (key.isNotEmpty()) """$key": [],""" else """"[],""")
                    compareLine(color1, start, color2, start, longest, tabLvl)
                }
            }
            expected is JsonPrimitive? && actual is JsonObject -> {
                val start = (if (key.isNotEmpty()) """"$key": """ else """""")
                (
                    actual.toPrettyString().addBefore(start).add(",").lines().mapIndexed { index, actualLine ->
                        compareLine(
                            color1,
                            if (index == 0) expected?.toPrettyString()?.add(",")?.addBefore(start) ?: "" else "",
                            color2,
                            actualLine,
                            longest,
                            tabLvl,
                        )
                    }.reduceOrNull { acc, s -> "$acc\n$s" } ?: ""
                    )
            }
            expected is JsonObject && actual is JsonPrimitive? -> {
                val start = (if (key.isNotEmpty()) """"$key": """ else """""")
                (
                    expected.toPrettyString().add(",").addBefore(start).lines().mapIndexed { index, expectedLine ->
                        compareLine(
                            color1,
                            expectedLine,
                            color2,
                            if (index == 0) actual?.toPrettyString()?.addBefore(start) ?: "" else "",
                            longest,
                            tabLvl,
                        )
                    }.reduceOrNull { acc, s -> "$acc\n$s" } ?: ""
                    )
            }
            else -> throw Exception("expected is ${if (expected is JsonNull) "JsonNull" else expected!!::class.simpleName} | actual is ${if (actual is JsonNull) "JsonNull" else actual!!::class.simpleName}")
        }
    }

    private fun String.add(s: String) = this + s

    private fun String.addBefore(s: String) = s + this

    private fun compareLine(
        color1: String,
        prefix: String,
        postFix: String,
        s1: String,
        color2: String,
        s2: String,
        longest: Int,
        tabLvl: Int,
    ): String {
        require(s1.lines().size <= 1 && s2.lines().size <= 1)
        val tabs: String = (0 until tabLvl).map { indent }.reduceOrNull { acc, s -> acc + s } ?: ""
        return if (s1.isEmpty()) {
            val l1 = "".padEnd(longest).maxTo(maxLineLength)
            val l2 = "$tabs$prefix$s2$postFix".maxTo(maxLineLength)
            "$l1 | ${color2}$l2${resetColor}"
        } else if (s2.isEmpty()) {
            val l1 = "$tabs$prefix$s1${postFix.padEnd(longest - tabs.length - prefix.length - s1.length)}".maxTo(maxLineLength)
            "${color1}$l1${resetColor} |"
        } else {
            val l1 = "$tabs$prefix$s1${postFix.padEnd(longest - tabs.length - prefix.length - s1.length)}".maxTo(maxLineLength)
            val l2 = "$tabs$prefix$s2$postFix".maxTo(maxLineLength)
            "${color1}$l1${resetColor} | ${color2}$l2${resetColor}"
        }
    }

    private fun compareLine(
        color1: String,
        s1: String,
        color2: String,
        s2: String,
        longest: Int,
        tabLvl: Int,
    ): String {
        require(s1.lines().size <= 1 && s2.lines().size <= 1)
        val tabs: String = (0 until tabLvl).map { indent }.reduceOrNull { acc, s -> acc + s } ?: ""
        val l1 = "$tabs${s1.padEnd(longest - tabs.length)}".maxTo(maxLineLength)
        val l2 = "$tabs$s2".maxTo(maxLineLength)
        return "${color1}$l1${resetColor} | ${color2}$l2${resetColor}"
    }

    private fun String.maxTo(max: Int): String =
        if (length > max) take(max) else this

    private fun String.removeLast(s: String): String {
        val index = this.lastIndexOf(s, length)
        return if (index != -1) this.removeRange(index, index + s.length)
        else this
    }
}

internal fun JsonObject.compareTo(jsonE: JsonObject, maxLineLength: Int = 100) =
    JsonComparator(this, jsonE, maxLineLength = maxLineLength)
