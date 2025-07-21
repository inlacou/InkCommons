package com.inlacou.inkkotlincommons.jsons

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonPrimitive

fun Double.toJsonElement(): JsonElement = JsonPrimitive(this)
fun Float.toJsonElement(): JsonElement = JsonPrimitive(this)
fun Boolean.toJsonElement(): JsonElement = JsonPrimitive(this)
fun Int.toJsonElement(): JsonElement = JsonPrimitive(this)
fun String.toJsonElement(): JsonElement {
    return if (startsWith("{") && endsWith("}")) this.toJsonObject()
    else if (startsWith("[") && endsWith("]")) this.toJsonArray()
    else JsonPrimitive(this)
}
fun String.isJsonObject(): Boolean = Json.parseToJsonElement(this) is JsonObject
fun String.toJsonObject(): JsonObject = Json.parseToJsonElement(this) as JsonObject
fun String.toJsonArray(): JsonArray = Json.parseToJsonElement(this) as JsonArray
fun String.toJsonArrayOrNull(): JsonArray? = Json.parseToJsonElement(this) as? JsonArray

/**
 * Syntactic sugar to write less code.
 */
fun JsonElement.toDouble(): Double = jsonPrimitive.double
