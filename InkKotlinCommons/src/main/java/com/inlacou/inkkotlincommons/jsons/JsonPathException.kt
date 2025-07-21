package com.inlacou.inkkotlincommons.jsons

import kotlinx.serialization.json.JsonElement

/**
 * Parent class for all the expected [Exception]s raised on the JSONPATH implementation.
 */
abstract class JsonPathException(override val message: String? = null) : Exception(message)

/**
 * [Exception] raised when parameter is required (as non-nullable) and it is not found.
 */
class JsonPathNotFoundException(notFound: String, path: String, node: JsonElement) : JsonPathException("$notFound not found with path `$path` in ${node::class.simpleName} `$node`")

/**
 * [Exception] raised when parameter is requested as a format, and it is found with a non-compatible format.
 */
class JsonPathFoundWrongFormatException(message: String) : JsonPathException(message) {
    /**
     * @param [parseToWhat]
     * @param [with] found on path
     * @param [found] found data
     * @param [where] jsonObject found on
     */
    constructor(parseToWhat: String, with: String, found: String?, where: String) : this("Data found `$found` on path `$with` could not be parsed to $parseToWhat on JsonObject `$where`")
}

/**
 * [Exception] raised when:
 * 	- Parameter is required (as non-nullable) and a node leading to it is not found
 * 	- Trying to set a new value and a node leading to it's desired position is not found
 */
class JsonPathMissingNodeException(override val message: String? = null) : JsonPathException(message)

/**
 * [Exception] raised when parameter is required (as non-nullable) and designed array position not found.
 */
class JsonPathMissingArrayElementException(override val message: String? = null) : JsonPathException(message)
