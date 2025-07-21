package com.inlacou.inkkotlincommons.jsons

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.float
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.longOrNull
import kotlinx.serialization.json.long
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.contentOrNull
import kotlin.math.max

internal fun JsonObject.Companion.empty() = JsonObject(emptyMap())
internal fun JsonArray.Companion.empty() = JsonArray(listOf())

internal fun JsonElement.getAllByPath(path: JsonPath): List<JsonElement> {
    if (path.s.contains("[*]")) {
        val paths = path.s
            .split("[*]")
            .map { if (it.startsWith(".")) "$$it" else it }
            .filter { it.isNotEmpty() }
            .map { JsonPath(it) }
        if (paths.size == 1) return this.jsonObject.getNullableJsonArrayByPath(paths.first())?.toList() ?: listOf()
        var acc: List<JsonElement> = listOf(this)
        paths.onEach { currentPath -> acc = acc.flatMap { it.getAllByPath(currentPath) } }
        return acc
    } else {
        return when (this) {
            is JsonPrimitive -> listOf()
            is JsonArray -> this.filterNot { it is JsonNull }.flatMap { it.getAllByPath(path) }
            is JsonObject -> listOfNotNull(this.getNullableByPath(path))
        }
    }
}

/**
 * This method only implements a subset of the JSONPATH specification
 *
 * @author Iñigo Lacoume
 */
internal fun JsonObject.getNullableByPath(path: JsonPath): JsonElement? {
    if (path == JsonPath("$.")) return this
    val remainingActions = path.pathElements().toMutableList()

    var inputNode: JsonElement = this
    while (remainingActions.isNotEmpty()) {
        try {
            val inputAction = remainingActions.first()
            remainingActions.remove(inputAction)
            inputNode = if (inputAction.endsWith("]")) {
                val anchorPos = inputAction.indexOf("[")
                val key = inputAction.take(anchorPos)
                val position = inputAction.drop(anchorPos + 1).dropLast(1).toInt()
                val aux = inputNode.getByJsonPathAction(key)
                    ?.let { it.jsonArrayOrNull?.getOrNull(position) }
                if (remainingActions.isEmpty() && aux == null) {
                    /* break - safely as there is no more remaining actions */
                    return aux
                } else {
                    aux!!
                }
            } else if (remainingActions.isEmpty()) {
                /* break - safely as there is no more remaining actions */
                return inputNode.getByJsonPathAction(inputAction)!!
            } else {
                inputNode.getByJsonPathAction(inputAction)!!
            }
        } catch (npe: NullPointerException) {
            return null
        }
    }
    return inputNode
}

internal fun JsonElement.jsonArrayOrThrow(cb: (JsonElement) -> JsonPathFoundWrongFormatException) =
    this as? JsonArray ?: throw cb.invoke(this)
internal val JsonElement.jsonArrayOrNull get() = this as? JsonArray
internal val JsonElement.jsonObjectOrNull: JsonObject? get() = this as? JsonObject
internal val JsonElement.contentOrNull: String? get() = (this as? JsonPrimitive)?.contentOrNull

/**
 * When getting by action, the raw action can be a normal action or a regex one.
 */
private fun JsonElement.getByJsonPathAction(inputAction: String): JsonElement? {
    return if (inputAction.startsWith("R={") && inputAction.endsWith("}")) {
        val regex = Regex(inputAction.drop(3).dropLast(1))
        jsonObject.keys.firstOrNull { regex.matchEntire(it)?.groups?.isNotEmpty() ?: false }?.let {
            jsonObject[it]
        }
    } else if (this is JsonObject) {
        jsonObject[inputAction]
    } else null
}

internal fun JsonObject.getNullableStringByPath(path: JsonPath): String? = getNullableByPath(path)?.jsonPrimitive?.contentOrNull
internal fun JsonObject.getNullableFloatByPath(path: JsonPath): Float? = getNullableByPath(path)?.jsonPrimitive?.floatOrNull
internal fun JsonObject.getNullableDoubleByPath(path: JsonPath): Double? = getNullableByPath(path)?.jsonPrimitive?.doubleOrNull
internal fun JsonObject.getNullableIntByPath(path: JsonPath): Int? = getNullableByPath(path)?.jsonPrimitive?.intOrNull
internal fun JsonObject.getNullableLongByPath(path: JsonPath): Long? = getNullableByPath(path)?.jsonPrimitive?.longOrNull
internal fun JsonObject.getNullableBooleanByPath(path: JsonPath): Boolean? = getNullableByPath(path)?.jsonPrimitive?.booleanOrNull
internal fun JsonObject.getNullableJsonObjectByPath(path: JsonPath): JsonObject? = getNullableByPath(path)?.jsonObjectOrNull
internal fun JsonObject.getNullableJsonArrayByPath(path: JsonPath): JsonArray? = getNullableByPath(path)?.jsonArrayOrNull
internal fun JsonObject.getNullableJsonPrimitiveByPath(path: JsonPath): JsonPrimitive? = getNullableByPath(path)?.jsonPrimitive

/**
 * @throws [JsonPathNotFoundException] when the requested JSONPATH is not found on receiver json.
 */
@Throws(JsonPathNotFoundException::class)
internal fun JsonObject.getStringByPath(path: JsonPath): String {
    return getNullableJsonPrimitiveByPathOrThrow(path, "String")?.content ?: throw JsonPathNotFoundException("String", path.s, this)
}

/**
 * @throws [JsonPathFoundWrongFormatException] when the requested JSONPATH is found, but it is not a valid [Float].
 * @throws [JsonPathNotFoundException] when the requested JSONPATH is not found on receiver json.
 */
@Throws(JsonPathFoundWrongFormatException::class, JsonPathNotFoundException::class)
internal fun JsonObject.getFloatByPath(path: JsonPath): Float {
    val primitive = getNullableJsonPrimitiveByPathOrThrow(path, "Float")
    try {
        return primitive?.float ?: throw JsonPathNotFoundException("Float", path.s, this)
    } catch (ise: NumberFormatException) {
        throw JsonPathFoundWrongFormatException("Float", path.s, primitive?.content, this.toString())
    }
}

/**
 * @throws [JsonPathFoundWrongFormatException] when the requested JSONPATH is found, but it is not a valid [Double].
 * @throws [JsonPathNotFoundException] when the requested JSONPATH is not found on receiver json.
 */
@Throws(JsonPathFoundWrongFormatException::class, JsonPathNotFoundException::class)
internal fun JsonObject.getDoubleByPath(path: JsonPath): Double {
    val primitive = getNullableJsonPrimitiveByPathOrThrow(path, "Double")
    try {
        return primitive?.double ?: throw JsonPathNotFoundException("Double", path.s, this)
    } catch (ise: NumberFormatException) {
        throw JsonPathFoundWrongFormatException("Double", path.s, primitive?.content, this.toString())
    }
}

/**
 * @throws [JsonPathFoundWrongFormatException] when the requested JSONPATH is found, but it is not a valid [Int].
 * @throws [JsonPathNotFoundException] when the requested JSONPATH is not found on receiver json.
 */
@Throws(JsonPathFoundWrongFormatException::class, JsonPathNotFoundException::class)
internal fun JsonObject.getIntByPath(path: JsonPath): Int {
    val primitive = getNullableJsonPrimitiveByPathOrThrow(path, "Int")
    try {
        return primitive?.int ?: throw JsonPathNotFoundException("Int", path.s, this)
    } catch (ise: NumberFormatException) {
        throw JsonPathFoundWrongFormatException("Int", path.s, primitive?.content, this.toString())
    }
}

/**
 * @throws [JsonPathFoundWrongFormatException] when the requested JSONPATH is found, but it is not a valid [Long].
 * @throws [JsonPathNotFoundException] when the requested JSONPATH is not found on receiver json.
 */
@Throws(JsonPathFoundWrongFormatException::class, JsonPathNotFoundException::class)
internal fun JsonObject.getLongByPath(path: JsonPath): Long {
    val primitive = getNullableJsonPrimitiveByPathOrThrow(path, "Long")
    try {
        return primitive?.long ?: throw JsonPathNotFoundException("Long", path.s, this)
    } catch (ise: NumberFormatException) {
        throw JsonPathFoundWrongFormatException("Long", path.s, primitive?.content, this.toString())
    }
}

/**
 * @throws [JsonPathFoundWrongFormatException] when the requested JSONPATH is found, but it is not a valid [Boolean].
 * @throws [JsonPathNotFoundException] when the requested JSONPATH is not found on receiver json.
 */
@Throws(JsonPathFoundWrongFormatException::class, JsonPathNotFoundException::class)
internal fun JsonObject.getBooleanByPath(path: JsonPath): Boolean {
    val primitive = getNullableJsonPrimitiveByPathOrThrow(path, "Boolean")
    try {
        return primitive?.boolean ?: throw JsonPathNotFoundException("Boolean", path.s, this)
    } catch (ise: IllegalStateException) {
        throw JsonPathFoundWrongFormatException("Boolean", path.s, primitive?.content, this.toString())
    }
}

/**
 * @throws [JsonPathFoundWrongFormatException] when the requested JSONPATH is found, but it is not a valid [JsonPrimitive].
 */
@Throws(JsonPathFoundWrongFormatException::class)
private fun JsonObject.getNullableJsonPrimitiveByPathOrThrow(path: JsonPath, type: String): JsonPrimitive? {
    val found = getNullableByPath(path)
    try {
        return found?.jsonPrimitive
    } catch (e: IllegalArgumentException) {
        throw if (
            e.message == "Element class kotlinx.serialization.json.JsonObject is not a JsonPrimitive" ||
            e.message == "Element class kotlinx.serialization.json.JsonArray is not a JsonPrimitive"
        ) {
            JsonPathFoundWrongFormatException(type, path.s, found.toString(), this.toString())
        } else e
    }
}

/**
 * @throws [JsonPathNotFoundException] when the requested JSONPATH is not found on receiver json.
 */
@Throws(JsonPathNotFoundException::class)
internal fun JsonObject.getJsonObjectByPath(path: JsonPath): JsonObject = getNullableByPath(path)?.jsonObject ?: throw JsonPathNotFoundException("JsonObject", path.s, this)

/**
 * @throws [JsonPathNotFoundException] when the requested JSONPATH is not found on receiver json.
 */
@Throws(JsonPathNotFoundException::class)
internal fun JsonObject.getJsonArrayByPath(path: JsonPath): JsonArray = getNullableByPath(path)?.jsonArray ?: throw JsonPathNotFoundException("JsonArray", path.s, this)

/**
 * @throws [JsonPathNotFoundException] when the requested JSONPATH is not found on receiver json.
 */
@Throws(JsonPathNotFoundException::class)
internal fun JsonObject.getJsonPrimitiveByPath(path: JsonPath): JsonPrimitive = getNullableByPath(path)?.jsonPrimitive ?: throw JsonPathNotFoundException("JsonPrimitive", path.s, this)

/**
 * See [JsonObject.getNullableByPath]
 *
 * @author Iñigo Lacoume
 */
internal fun JsonArray.getNullableByPath(path: JsonPath): JsonElement? {
    val actions = path.pathElements().toList()
    val action = actions.first()
    val index = action.drop(1).dropLast(1).toInt() // For '[10]', drop '[', drop ']'
    val jsonO = get(index).jsonObject
    val newPath = (actions - action).jsonPathElementsToString()
    return jsonO.getNullableByPath(JsonPath(newPath))
}
internal fun JsonArray.getNullableStringByPath(path: JsonPath): String? = getNullableByPath(path)?.jsonPrimitive?.content
internal fun JsonArray.getNullableFloatByPath(path: JsonPath): Float? = getNullableByPath(path)?.jsonPrimitive?.content?.toFloatOrNull()
internal fun JsonArray.getNullableDoubleByPath(path: JsonPath): Double? = getNullableByPath(path)?.jsonPrimitive?.content?.toDoubleOrNull()
internal fun JsonArray.getNullableIntByPath(path: JsonPath): Int? = getNullableByPath(path)?.jsonPrimitive?.content?.toIntOrNull()
internal fun JsonArray.getNullableLongByPath(path: JsonPath): Long? = getNullableByPath(path)?.jsonPrimitive?.content?.toLongOrNull()
internal fun JsonArray.getNullableBooleanByPath(path: JsonPath): Boolean? = getNullableByPath(path)?.jsonPrimitive?.content?.toBooleanStrictOrNull()
internal fun JsonArray.getNullableJsonObjectByPath(path: JsonPath): JsonObject? = getNullableByPath(path)?.jsonObject
internal fun JsonArray.getNullableJsonArrayByPath(path: JsonPath): JsonArray? = getNullableByPath(path)?.jsonArray
internal fun JsonArray.getNullableJsonPrimitiveByPath(path: JsonPath): JsonPrimitive? = getNullableByPath(path)?.jsonPrimitive
internal fun JsonArray.getStringByPath(path: JsonPath): String = getNullableByPath(path)!!.jsonPrimitive.content
internal fun JsonArray.getFloatByPath(path: JsonPath): Float = getNullableByPath(path)!!.jsonPrimitive.content.toFloat()
internal fun JsonArray.getDoubleByPath(path: JsonPath): Double = getNullableByPath(path)!!.jsonPrimitive.content.toDouble()
internal fun JsonArray.getIntByPath(path: JsonPath): Int = getNullableByPath(path)!!.jsonPrimitive.content.toInt()
internal fun JsonArray.getLongByPath(path: JsonPath): Long = getNullableByPath(path)!!.jsonPrimitive.content.toLong()
internal fun JsonArray.getBooleanByPath(path: JsonPath): Boolean = getNullableByPath(path)!!.jsonPrimitive.content.toBoolean()
internal fun JsonArray.getJsonObjectByPath(path: JsonPath): JsonObject = getNullableByPath(path)!!.jsonObject
internal fun JsonArray.getJsonArrayByPath(path: JsonPath): JsonArray = getNullableByPath(path)!!.jsonArray
internal fun JsonArray.getJsonPrimitiveByPath(path: JsonPath): JsonPrimitive? = getNullableByPath(path)?.jsonPrimitive

/**
 * This method returns a new JsonObject with the given JsonElement added
 * @param key [String]
 * @param jsonElement [JsonElement]
 * @return [JsonObject]
 */
private fun JsonObject.put(key: String, jsonElement: JsonElement?): JsonObject {
    return if (key.startsWith("R={") && key.endsWith("}")) {
        val regex = Regex(key.drop(3).dropLast(1))
        jsonObject.keys.firstOrNull { regex.matchEntire(it)?.groups?.isNotEmpty() ?: false }?.let { keyByRegex ->
            JsonObject(
                (this as Map<String, JsonElement>).toMutableMap().apply {
                    if (jsonElement != null) {
                        put(keyByRegex, jsonElement)
                    } else {
                        remove(keyByRegex)
                    }
                }
            )
        } ?: this
    } else {
        JsonObject(
            (this as Map<String, JsonElement>).toMutableMap().apply {
                if (jsonElement != null) {
                    put(key, jsonElement)
                } else {
                    remove(key)
                }
            }
        )
    }
}

fun JsonArray.add(jsonElement: JsonElement): JsonArray {
    return JsonArray((this as List<JsonElement>).toMutableList().apply { add(jsonElement) })
}

fun JsonArray.add(index: Int, jsonElement: JsonElement): JsonArray {
    return JsonArray((this as List<JsonElement>).toMutableList().apply { add(index, jsonElement) })
}

private fun JsonArray.set(index: Int, jsonElement: JsonElement?): JsonArray {
    return JsonArray(
        (this as List<JsonElement>).toMutableList().apply {
            if (jsonElement != null) {
                tryRemoveAt(index)
                add(index, jsonElement)
            } else tryRemoveAt(index)
        }
    )
}

private fun <T> MutableList<T>.tryRemoveAt(index: Int): T? {
    return if (index < size) removeAt(index) else null
}

internal fun JsonObject.add(path: JsonPath, value: JsonElement?, addMiddlePathsIfNotExist: Boolean = false): JsonObject =
    updateRecursively(path.pathElements().toMutableList(), value, replace = false, addMiddlePathsIfNotExist = addMiddlePathsIfNotExist)
internal fun JsonObject.add(path: JsonPath, value: String, addMiddlePathsIfNotExist: Boolean = false): JsonObject = add(path, JsonPrimitive(value), addMiddlePathsIfNotExist)

internal fun JsonObject.set(path: JsonPath, value: JsonElement?, addMiddlePathsIfNotExist: Boolean = false): JsonObject =
    updateRecursively(path.pathElements().toMutableList(), value, replace = true, addMiddlePathsIfNotExist = addMiddlePathsIfNotExist)
internal fun JsonObject.set(path: JsonPath, value: String, addMiddlePathsIfNotExist: Boolean = false): JsonObject = set(path, JsonPrimitive(value), addMiddlePathsIfNotExist)
internal fun JsonObject.set(path: JsonPath, value: Int, addMiddlePathsIfNotExist: Boolean = false): JsonObject = set(path, JsonPrimitive(value), addMiddlePathsIfNotExist)
internal fun JsonObject.set(path: JsonPath, value: Long, addMiddlePathsIfNotExist: Boolean = false): JsonObject = set(path, JsonPrimitive(value), addMiddlePathsIfNotExist)
internal fun JsonObject.set(path: JsonPath, value: Double, addMiddlePathsIfNotExist: Boolean = false): JsonObject = set(path, JsonPrimitive(value), addMiddlePathsIfNotExist)
internal fun JsonObject.set(path: JsonPath, value: Float, addMiddlePathsIfNotExist: Boolean = false): JsonObject = set(path, JsonPrimitive(value), addMiddlePathsIfNotExist)
internal fun JsonObject.set(path: JsonPath, value: Boolean, addMiddlePathsIfNotExist: Boolean = false): JsonObject = set(path, JsonPrimitive(value), addMiddlePathsIfNotExist)
internal fun JsonObject.set(path: JsonPath, value: Number, addMiddlePathsIfNotExist: Boolean = false): JsonObject = set(path, JsonPrimitive(value), addMiddlePathsIfNotExist)
internal fun JsonObject.remove(path: JsonPath): JsonObject = try { set(path, null) } catch (
    jpmne: JsonPathMissingNodeException
) { this }

@Throws(JsonPathMissingNodeException::class, JsonPathMissingArrayElementException::class)
private fun JsonElement.updateRecursively(
    path: List<String>,
    value: JsonElement?,
    replace: Boolean,
    addMiddlePathsIfNotExist: Boolean,
): JsonElement = when (this) {
    is JsonObject -> updateRecursively(path, value, replace, addMiddlePathsIfNotExist)
    is JsonArray -> updateRecursively(path, value, replace, addMiddlePathsIfNotExist)
    is JsonPrimitive -> updateRecursively(path, value)
}

@Throws(JsonPathMissingNodeException::class, JsonPathMissingArrayElementException::class)
private fun JsonArray.updateRecursively(
    path: List<String>,
    value: JsonElement?,
    replace: Boolean,
    addMiddlePathsIfNotExist: Boolean,
): JsonArray {
    val remainingActions = path.drop(1)
    val currentAction = path[0]
    val aux = this
    val positionAsString = currentAction.drop(currentAction.indexOf("[") + 1).dropLast(1)
    val index = if (positionAsString == "last") max(0, aux.lastIndex) else positionAsString.toInt()
    val nextNode = aux.getOrNull(index)
    return if (nextNode == null) {
        if (remainingActions.isEmpty()) {
            aux.setOrAdd(index, value, replace)
        } else if (addMiddlePathsIfNotExist) {
            aux.setOrAdd(index, JsonObject.empty().updateRecursively(remainingActions, value, replace, addMiddlePathsIfNotExist), replace)
        } else {
            throw JsonPathMissingArrayElementException("remainingActions '$remainingActions' currentAction '$currentAction' for json array '$this'")
        }
    } else {
        if (remainingActions.isEmpty()) {
            aux.setOrAdd(if (positionAsString == "last") index + 1 else index, value, replace)
        } else {
            aux.setOrAdd(index, nextNode.updateRecursively(remainingActions, value, replace, addMiddlePathsIfNotExist), replace)
        }
    }
}

private fun JsonArray.setOrAdd(index: Int, value: JsonElement?, replace: Boolean): JsonArray {
    val aux = this
    return if (replace) aux.set(index, value)
    else if (value != null) aux.add(index, value)
    else aux
}

@Throws(JsonPathMissingNodeException::class, JsonPathMissingArrayElementException::class)
private fun JsonObject.updateRecursively(
    path: List<String>,
    value: JsonElement?,
    replace: Boolean,
    addMiddlePathsIfNotExist: Boolean,
): JsonObject {
    val remainingActions = path.drop(1).toMutableList()
    var currentAction = path[0]
    if (currentAction == "[0]") throw Exception("INLACOU")
    val aux = this
    val nextNode = if (currentAction.endsWith("]")) {
        // Get the item from Array
        val bracketStart = currentAction.indexOf("[")
        val key = currentAction.take(bracketStart)
        remainingActions.add(0, currentAction.substring(bracketStart))
        currentAction = key
        aux.getNullableByPath(JsonPath("$.$key"))
    } else aux.getNullableByPath(JsonPath("$.$currentAction"))
    return if (nextNode == null) {
        if (remainingActions.isEmpty()) {
            aux.put(currentAction, value)
        } else if (addMiddlePathsIfNotExist) {
            aux.put(
                currentAction,
                (
                    if (path[0].endsWith("]")) JsonArray(emptyList())
                    else JsonObject.empty()
                    ).updateRecursively(remainingActions, value, replace, addMiddlePathsIfNotExist)
            )
        } else {
            throw JsonPathMissingNodeException("remainingActions '$remainingActions' currentAction '$currentAction' for json object '$this' trying to set $value")
        }
    } else {
        if (remainingActions.isEmpty()) {
            aux.put(currentAction, value)
        } else {
            aux.put(currentAction, nextNode.updateRecursively(remainingActions, value, replace, addMiddlePathsIfNotExist))
        }
    }
}

@Throws(JsonPathMissingNodeException::class, JsonPathMissingArrayElementException::class)
private fun JsonPrimitive.updateRecursively(
    path: List<String>,
    value: JsonElement?,
): JsonPrimitive {
    return if (path.size > 1) throw JsonPathMissingNodeException("Multiple paths '$path' on JsonPrimitive node '$this'. Can't satisfy given paths as JsonPrimitive is a leaf node.")
    else if (value == null) JsonNull
    else if (value !is JsonPrimitive) throw JsonPathFoundWrongFormatException("value '$value' is ${value::class.simpleName} on JsonPrimitive node.")
    else value
}

/**
 * Utility function to make code more readable.
 * It will return you the value in a nullable value or throw an exception if it is null.
 *
 * DO NOT change the argument from [JsonPathMissingNodeException] to a more generic [java.lang.Exception], overload the function instead.
 *
 * @throws [JsonPathMissingNodeException] as it is what it gets fed
 */
@Throws(JsonPathMissingNodeException::class)
private fun <T> T?.getOrThrow(exception: JsonPathMissingNodeException): T = this ?: throw exception

private fun List<String>.jsonPathElementsToString(): String = "\$.${this.reduce { acc, s -> "$acc.$s" }}"

/**
 * This way we can do List<String>-String to remove the String item from the List.
 */
private operator fun List<String>.minus(s: String): List<String> = toMutableList().apply { removeAll { it == s } }

/**
 * Iterates a JsonElement removing each instance of empty Json '{}' and empty Array '[]'.
 *
 * @return [JsonObject] without those empty fields.
 */
internal fun JsonObject.removeEmptyJsonObjectFields(): JsonObject? {
    var previous: JsonObject? = this
    var aux: JsonObject? = this
    if (keys.isEmpty()) return null
    keys.forEach {
        when (val item = get(it)) {
            is JsonObject -> { aux = aux!!.set(JsonPath("$.$it"), item.removeEmptyJsonObjectFields()) }
            is JsonArray -> {
                var array: JsonArray? = item
                if (item.isEmpty()) array = null
                else item.forEachIndexedReversed { index, jsonElement ->
                    if (jsonElement is JsonObject) {
                        array = array!!.set(index, jsonElement.removeEmptyJsonObjectFields())
                    }
                }
                aux = aux!!.set(JsonPath("$.$it"), array)
            }
            is JsonElement -> if (item is JsonNull) aux = aux!!.remove(JsonPath("$.$it"))
            else -> {}
        }
    }
    while (aux != previous) {
        previous = aux
        aux = aux?.removeEmptyJsonObjectFields()
    }
    return aux
}

/**
 * Same as [Iterable.forEachIndexed] but on [Iterable.reversed] direction.
 *
 * The indexes are reversed too, so it will start with the index being the max index and go until 0.
 *
 * @author Iñigo Lacoume
 */
private inline fun <T> Iterable<T>.forEachIndexedReversed(action: (index: Int, T) -> Unit) {
    var index = toList().size - 1
    this.reversed().forEach { action(index--, it) }
}

/**
 * Transforms a [JsonObject] to a list of final JSONPATHs. That is, JSONPATHs from root to leaf.
 *
 * @return [List]<[String]>
 */
internal fun JsonObject.toJsonPaths(): List<JsonPath> = toJsonPaths(JsonPath("$."))

/**
 * This is the recursive function to be used by the public function [toJsonPaths]
 */
private fun JsonElement.toJsonPaths(inheritedPath: JsonPath): List<JsonPath> {
    val aux = mutableListOf<JsonPath>()
    when (val item = this) {
        is JsonObject -> item.keys.map { Pair(it, item[it]!!) }.forEach {
            aux.addAll(it.second.toJsonPaths(inheritedPath.mergePaths(JsonPath("$.${it.first}"))))
        }
        is JsonArray -> {
            item.forEachIndexed { index, item -> aux.addAll(item.toJsonPaths(JsonPath("$inheritedPath[$index]"))) }
        }
        is JsonPrimitive -> {
            aux.add(inheritedPath)
        }
    }
    return aux
}

/**
 * Merge two [JsonObject]s. When merging [JsonArray]s inside the [JsonObject], it will keep all the items, does not
 * modify them.
 *
 * @receiver [JsonObject]
 *
 * @param [jsonObject] [JsonObject]
 *
 * @return [JsonObject]
 *
 * @author Iñigo Lacoume
 */
internal fun JsonObject.merge(jsonObject: JsonObject): JsonObject {
    var result = this
    jsonObject.toJsonPaths()
        .map {
            // Clean all the JSONPATHs of the part after the Array
            if (it.s.contains(Regex("\\[\\d+\\]"))) {
                JsonPath(it.s.substring(0, it.s.lastIndexOf("]") + 1))
            } else it
        }
        .toSet() // Remove duplicates
        .map { Pair(it, jsonObject.getNullableByPath(it)) }
        .onEach {
            val path = if (it.first.s.contains(Regex("\\[\\d+\\]"))) {
                // If we are on an Array path, we need to change the index for "last"
                JsonPath(it.first.s.substring(0, it.first.s.lastIndexOf("[")) + "[last]")
            } else it.first
            result = result.add(path, it.second, addMiddlePathsIfNotExist = true)
        }
    return result
}

/**
 * Merge a [JsonElement] into a [JsonObject]s specified location.
 * When merging [JsonArray]s inside a [JsonObject], it will keep all the items, does not modify them.
 *
 * @receiver [JsonObject]
 *
 * @param [targetPath] [JsonPath] location to merge into
 * @param [jsonElement] [JsonElement] to merge
 *
 * @return [JsonObject]
 *
 * @author Iñigo Lacoume
 */
internal fun JsonObject.merge(targetPath: JsonPath, jsonElement: JsonElement): JsonObject =
    if (this.contains(targetPath)) {
        this.merge(
            if (targetPath.isEmpty()) jsonElement.jsonObject
            else JsonObject.empty().set(targetPath, jsonElement, addMiddlePathsIfNotExist = true)
        )
    } else {
        this.set(targetPath, jsonElement, addMiddlePathsIfNotExist = true)
    }

internal fun JsonObject.contains(path: JsonPath) = getNullableByPath(path) != null
