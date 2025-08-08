package com.inlacou.inkkotlincommons.jsons

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Put given [value] on [JsonArray] identified by [key] at [index].
 * If the [JsonArray] is not found by [key], it will be created.
 */
internal fun JsonObject.put(index: Int, key: String, value: JsonElement) =
    JsonObject(
        toMutableMap().apply {
            if(containsKey(key)) {
                put(key, JsonArray(get(key)!!.jsonArray.toMutableList().apply { add(index, value) }))
            } else {
                put(key, JsonArray(listOf(value)))
            }
        }
    )
internal fun JsonObject.putAll(key: String, values: List<JsonElement>) = JsonObject(toMutableMap().apply {
    put(key, JsonArray(values))
})
internal fun JsonObject.put(key: String, value: JsonElement) = JsonObject(toMutableMap().apply { put(key, value) })
internal fun JsonObject.put(key1: String, key2: String, value: JsonElement): JsonObject =
    put(
        key1,
        (
                (get(key2) as? JsonObject)
                    ?: JsonObject(mapOf())
                )
            .put(key2, value)
    )
internal fun JsonObject.remove(key: String) = JsonObject(toMutableMap().apply { remove(key) })
internal fun JsonObject.remove(index: Int, key: String) = JsonObject(
    toMutableMap().apply { put(key, JsonArray(get(key)!!.jsonArray.toMutableList().apply { removeAt(index) })) }
)
internal fun JsonArray.remove(index: Int) = JsonArray(toMutableList().apply { removeAt(index) })
internal fun JsonArray.removeAll(cb: (JsonElement) -> Boolean) = JsonArray(toMutableList().apply { this.removeAll(cb) })
internal val JsonElement.jsonPrimitiveOrNull: JsonPrimitive? get() = if(this is JsonPrimitive) this.jsonPrimitive else null

fun <UUID> JsonObject.humanReadableAndDigest(
    uuidRegex: Regex,
    printCb: (whatToPrint: Any) -> Unit,
    targetRootRetriever: (JsonObject) -> JsonObject,
    selfUuidRetriever: (JsonObject) -> UUID,
    selfRetriever: (JsonObject) -> JsonElement?,
    childUuidRetriever: (JsonObject) -> List<UUID>,
    replacementMode: ReplacementMode = ReplacementMode.RANDOM
) {
    val replacements = mutableListOf<String>()
    val jsonWithReplacedUUIDs = this.toString().replaceUUIDs(
        uuidRegex,
        { what, with -> replacements.add("replaced '$what' with '$with'") },
        replacementMode = replacementMode
    )
    //printCb("Json with replaced uuids: $jsonWithReplacedUUIDs")
    printCb("")
    replacements.onEach { printCb(it) }
    printCb("")
    val layers = targetRootRetriever(jsonWithReplacedUUIDs.toJsonObject())
    layers.toInitialRelationshipNodes(
        selfUuidRetriever = selfUuidRetriever,
        selfRetriever = selfRetriever,
        parentUuidRetriever = { null },
        childUuidRetriever = childUuidRetriever,
    )
        //.also(printCb)
        .toFinal()
        .also { printCb(it) }
    printCb("")
}
