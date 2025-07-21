package com.inlacou.inkkotlincommons.jsons

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/**
 * Class responsible for converting a [JsonElement] to a human-readable [String].
 * It is the same as [JsonElement.toString] but adding indentation and line breaks so it is readable :).
 *
 * @author Iñigo Lacoume
 */
internal class JsonReader(private val data: JsonElement, private val initialTabLvl: Int = 0, private val indent: String = "    ") {

    override fun toString(): String = jsonToString(data, tabLvl = initialTabLvl)

    /**
     * Parses given json (recursively) to human-readable format String
     * @param json element to work on
     * @param tabLvl accumulation param to hold the current tab lvl
     * @return human-readable string for given json
     */
    private fun jsonToString(json: JsonElement, tabLvl: Int = 0): String {
        return when (json) {
            is JsonPrimitive -> json.toString()
            is JsonObject -> {
                var result = "{\n"
                json.forEach { result += """${indent * (tabLvl + 1)}"${it.key}": ${jsonToString(it.value, tabLvl + 1)},""".trimMargin() + "\n" }
                result = result.removeLast(",")
                result += "${indent * tabLvl}}"
                result
            }
            is JsonArray -> {
                var result = "[\n"
                json.forEach { result += """${indent * (tabLvl + 1)}${jsonToString(it, tabLvl + 1)},""".trimMargin() + "\n" }
                result = result.removeLast(",")
                result += "${indent * tabLvl}]"
                result
            }
        }
    }

    private fun String.removeLast(s: String): String {
        val index = this.lastIndexOf(s, length)
        return if (index != -1) this.removeRange(index, index + s.length)
        else this
    }

    /**
     * Allows us to multiply Strings Int times (String*int, "aB"*3 => "aBaBaB")
     */
    operator fun String.times(int: Int): String {
        var result = ""
        repeat(int) { result += this }
        return result
    }
}

internal fun JsonElement.toPrettyString(initialTabLvl: Int = 0, indent: String = "    ") =
    JsonReader(this, initialTabLvl, indent).toString()
