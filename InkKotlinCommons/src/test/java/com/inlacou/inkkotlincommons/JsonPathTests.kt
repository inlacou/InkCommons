package com.inlacou.inkkotlincommons

import com.inlacou.inkkotlincommons.jsons.JsonPath
import com.inlacou.inkkotlincommons.jsons.JsonPathFoundWrongFormatException
import com.inlacou.inkkotlincommons.jsons.JsonPathMissingArrayElementException
import com.inlacou.inkkotlincommons.jsons.JsonPathMissingNodeException
import com.inlacou.inkkotlincommons.jsons.JsonPathNotFoundException
import com.inlacou.inkkotlincommons.jsons.add
import com.inlacou.inkkotlincommons.jsons.compareTo
import com.inlacou.inkkotlincommons.jsons.getAllByPath
import com.inlacou.inkkotlincommons.jsons.getBooleanByPath
import com.inlacou.inkkotlincommons.jsons.getDoubleByPath
import com.inlacou.inkkotlincommons.jsons.getFloatByPath
import com.inlacou.inkkotlincommons.jsons.getIntByPath
import com.inlacou.inkkotlincommons.jsons.getLongByPath
import com.inlacou.inkkotlincommons.jsons.getNullableByPath
import com.inlacou.inkkotlincommons.jsons.getNullableJsonArrayByPath
import com.inlacou.inkkotlincommons.jsons.getNullableJsonObjectByPath
import com.inlacou.inkkotlincommons.jsons.getNullableStringByPath
import com.inlacou.inkkotlincommons.jsons.getStringByPath
import com.inlacou.inkkotlincommons.jsons.merge
import com.inlacou.inkkotlincommons.jsons.remove
import com.inlacou.inkkotlincommons.jsons.removeEmptyJsonObjectFields
import com.inlacou.inkkotlincommons.jsons.set
import com.inlacou.inkkotlincommons.jsons.toJsonArray
import com.inlacou.inkkotlincommons.jsons.toJsonElement
import com.inlacou.inkkotlincommons.jsons.toJsonObject
import com.inlacou.inkkotlincommons.jsons.toJsonPaths
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class JsonPathTests {
    @Test fun `getNullableByPath - target`() {
        assertEquals(
            "El principito",
            """{"title":"El principito"}""".toJsonObject().getNullableByPath(JsonPath("$.title"))?.jsonPrimitive?.content
        )
    }

    @Test fun `getNullableByPath - object - target`() {
        assertEquals(
            "El principito",
            """{"item":{"title":"El principito"}}""".toJsonObject().getNullableByPath(JsonPath("$.item.title"))?.jsonPrimitive?.content
        )
    }

    @Test fun `getNullableByPath - array - target`() {
        assertEquals(
            "El principito",
            """[{"title":"El principito"}]""".toJsonArray().getNullableByPath(JsonPath("$.[0].title"))?.jsonPrimitive?.content
        )
    }

    @Test fun `getNullableByPath - array - object- target (no dot before Square-Bracket first item)`() {
        assertEquals(
            "El principito",
            """[{"book": {"title":"El principito"}}]""".toJsonArray().getNullableByPath(JsonPath("\$[0].book.title"))?.jsonPrimitive?.content
        )
    }

    @Test fun `getNullableByPath - array - object- target (dot before Square-Bracket first item)`() {
        assertEquals(
            "El principito",
            """[{"book": {"title":"El principito"}}]""".toJsonArray().getNullableByPath(JsonPath("\$.[0].book.title"))?.jsonPrimitive?.content
        )
    }

    @Test fun `getNullableByPath - object - array - target`() {
        assertEquals(
            "El principito",
            """{"items":[{"title":"El principito"}]}""".toJsonObject().getNullableByPath(JsonPath("\$.items[0].title"))?.jsonPrimitive?.content
        )
    }

    @Test fun `set Int (JsonPrimitive) into json`() {
        assertEquals(
            """{"items":[{"title":"El principito"}],"uuid":1}""".toJsonObject(),
            """{"items":[{"title":"El principito"}]}""".toJsonObject().set(JsonPath("$.uuid"), JsonPrimitive(1))
        )
    }

    @Test fun `set Double (JsonPrimitive) into json`() {
        assertEquals(
            """{"items":[{"title":"El principito"}],"uuid":1.0}""".toJsonObject(),
            """{"items":[{"title":"El principito"}]}""".toJsonObject().set(JsonPath("\$.uuid"), JsonPrimitive(1.0))
        )
    }

    @Test fun `set Float (JsonPrimitive) into json`() {
        assertEquals(
            """{"items":[{"title":"El principito"}],"uuid":1.0}""".toJsonObject(),
            """{"items":[{"title":"El principito"}]}""".toJsonObject().set(JsonPath("\$.uuid"), JsonPrimitive(1F))
        )
    }

    @Test fun `set String (JsonPrimitive) into json`() {
        assertEquals(
            """{"items":[{"title":"El principito"}],"uuid":"1"}""".toJsonObject(),
            """{"items":[{"title":"El principito"}]}""".toJsonObject().set(JsonPath("\$.uuid"), JsonPrimitive("1"))
        )
    }

    @Test fun `set Int (raw) into json`() {
        assertEquals(
            """{"items":[{"title":"El principito"}],"uuid":1}""".toJsonObject(),
            """{"items":[{"title":"El principito"}]}""".toJsonObject().set(JsonPath("\$.uuid"), 1)
        )
    }

    @Test fun `set Double (raw) into json`() {
        assertEquals(
            """{"items":[{"title":"El principito"}],"uuid":1.0}""".toJsonObject(),
            """{"items":[{"title":"El principito"}]}""".toJsonObject().set(JsonPath("\$.uuid"), 1.0)
        )
    }

    @Test fun `set Float (raw) into json`() {
        assertEquals(
            """{"items":[{"title":"El principito"}],"uuid":1.0}""".toJsonObject(),
            """{"items":[{"title":"El principito"}]}""".toJsonObject().set(JsonPath("\$.uuid"), 1F)
        )
    }

    @Test fun `set String (raw) into json`() {
        assertEquals(
            """{"items":[{"title":"El principito"}],"uuid":"1"}""".toJsonObject(),
            """{"items":[{"title":"El principito"}]}""".toJsonObject().set(JsonPath("\$.uuid"), "1")
        )
    }

    @Test fun `set Json Object into json`() {
        assertEquals(
            """{"items":[{"title":"El principito"}],"newItem":{"some key":"some value"}}""".toJsonObject(),
            """{"items":[{"title":"El principito"}]}""".toJsonObject().set(JsonPath("\$.newItem"), """{"some key":"some value"}""".toJsonObject())
        )
    }

    @Test fun `set Json Array into json`() {
        assertEquals(
            """{"items":[{"title":"El principito"}],"newArray":[{"some key":"some value"}]}""".toJsonObject(),
            """{"items":[{"title":"El principito"}]}""".toJsonObject().set(JsonPath("\$.newArray"), """[{"some key":"some value"}]""".trimMargin().toJsonArray())
        )
    }

    @Test fun `set recursively - single level - String`() {
        assertEquals(
            """{"items":[{"title":"El principito"}],"newKey":"some value"}""".toJsonObject(),
            """{"items":[{"title":"El principito"}]}""".toJsonObject().set(JsonPath("\$.newKey"), "some value")
        )
    }

    @Test fun `set recursively - single level - Int`() {
        assertEquals(
            """{"items":[{"title":"El principito"}],"newKey":5}""".toJsonObject(),
            """{"items":[{"title":"El principito"}]}""".toJsonObject().set(JsonPath("\$.newKey"), 5)
        )
    }

    @Test fun `set recursively - single level - Double`() {
        assertEquals(
            """{"items":[{"title":"El principito"}],"newKey":5.0}""".toJsonObject(),
            """{"items":[{"title":"El principito"}]}""".toJsonObject().set(JsonPath("\$.newKey"), 5.0)
        )
    }

    @Test fun `set recursively - single level - Long`() {
        assertEquals(
            """{"items":[{"title":"El principito"}],"newKey":5}""".toJsonObject(),
            """{"items":[{"title":"El principito"}]}""".toJsonObject().set(JsonPath("$.newKey"), 5L)
        )
    }

    @Test fun `set recursively - single level - Float`() {
        assertEquals(
            """{"items":[{"title":"El principito"}],"newKey":5.0}""".toJsonObject(),
            """{"items":[{"title":"El principito"}]}""".toJsonObject().set(JsonPath("$.newKey"), 5f)
        )
    }

    @Test fun `set recursively - nested 1 - non existent object - String`() {
        assertThrows<JsonPathMissingNodeException> { """{"items":[{"title":"El principito"}]}""".toJsonObject().set(JsonPath("$.newItem.newKey"), "some value") }
    }

    @Test fun `set recursively - nested 1 - String`() {
        assertEquals(
            """{"items":[{"title":"El principito"}],"newItem":{"newKey":"some value"}}""".toJsonObject(),
            """{"items":[{"title":"El principito"}],"newItem":{}}""".toJsonObject().set(JsonPath("$.newItem.newKey"), "some value")
        )
    }

    @Test fun `set recursively - nested 2 - String`() {
        assertEquals(
            """{
                  "items":[{"title":"El principito"}],
                  "lvl1Item":{
                      "someKeyLvl1":"someValue",
                      "lvl2Item":{
                        "someKeyLvl2":"someValue",
                        "newKey":"newValue"
                      }
                  }
               }
            """.trimIndent().trimMargin().toJsonObject(),
            """{
                  "items":[{"title":"El principito"}],
                  "lvl1Item":{
                    "someKeyLvl1":"someValue",
                    "lvl2Item":{
                        "someKeyLvl2":"someValue"
                    }
                  }
               }
            """.trimIndent().trimMargin().toJsonObject().set(JsonPath("$.lvl1Item.lvl2Item.newKey"), "newValue")
        )
    }

    @Test fun `set recursively - nested 2 (not exists) - String`() {
        assertThrows<JsonPathMissingNodeException> ("For '$.lvl1Item.lvl2Item.newKey', '$.lvl1Item.lvl2Item' does not exist") {
            """{
                  "items":[{"title":"El principito"}],
                  "lvl1Item":{
                    "someKeyLvl1":"someValue"
                  }
               }
            """.trimIndent().trimMargin().toJsonObject().set(JsonPath("$.lvl1Item.lvl2Item.newKey"), "newValue")
        }
    }

    @Test fun `set recursively - nested 2 (not exists, create) - String`() {
        assertEquals(
            """{
                  "items":[{"title":"El principito"}],
                  "lvl1Item":{
                    "someKeyLvl1":"someValue",
                    "lvl2Item": { "newKey": "newValue" }
                  }
               }
            """.trimIndent().trimMargin().toJsonObject(),
            """{
                  "items":[{"title":"El principito"}],
                  "lvl1Item":{
                    "someKeyLvl1":"someValue"
                  }
               }
            """.trimIndent().trimMargin().toJsonObject().set(JsonPath("$.lvl1Item.lvl2Item.newKey"), "newValue", true)
        )
    }

    @Test fun `set recursively - nested 3 (not exists, create) - String`() {
        assertEquals(
            """{
                  "items":[{"title":"El principito"}],
                  "lvl1Item":{
                    "someKeyLvl1":"someValue",
                    "lvl2Item": { "lvl3Item": { "newKey": "newValue" } }
                  }
               }
            """.trimIndent().trimMargin().toJsonObject(),
            """{
                  "items":[{"title":"El principito"}],
                  "lvl1Item":{
                    "someKeyLvl1":"someValue"
                  }
               }
            """.trimIndent().trimMargin().toJsonObject().set(JsonPath("$.lvl1Item.lvl2Item.lvl3Item.newKey"), "newValue", true)
        )
    }

    @Test fun `set recursively - nested1 - list - new index`() {
        assertEquals(
            """{"items":["El principito","some value"]}""".toJsonObject(),
            """{"items":["El principito"]}""".toJsonObject().set(JsonPath("$.items[1]"), "some value")
        )
    }

    @Test fun `add recursively - nested1 - list - new index`() {
        assertEquals(
            """{"items":["El principito","some value"]}""".toJsonObject(),
            """{"items":["El principito"]}""".toJsonObject().add(JsonPath("$.items[1]"), "some value")
        )
    }

    @Test fun `set recursively - nested1 - list - existing index`() {
        assertEquals(
            """{"items":["some value"]}""".toJsonObject(),
            """{"items":["El principito"]}""".toJsonObject().set(JsonPath("$.items[0]"), "some value")
        )
    }

    @Test fun `add recursively - nested1 - list - existing index`() {
        assertEquals(
            """{"items":["some value","El principito"]}""".toJsonObject(),
            """{"items":["El principito"]}""".toJsonObject().add(JsonPath("$.items[0]"), "some value")
        )
    }

    @Test fun `recursive set remove - list, 0`() {
        assertEquals(
            """{"books":[]}""".toJsonObject(),
            """{"books":["El principito"]}""".toJsonObject().set(JsonPath("$.books[0]"), null)
        )
    }

    @Test fun `recursive set remove at position 0 on a size 1 array`() {
        assertEquals(
            """{"library":{"books":[]}}""".toJsonObject(),
            """{"library":{"books":["El principito"]}}""".toJsonObject().set(JsonPath("$.library.books[0]"), null)
        )
    }

    @Test fun `recursive remove - list, 0`() {
        assertEquals(
            """{"books":[]}""".toJsonObject(),
            """{"books":["El principito"]}""".toJsonObject().remove(JsonPath("$.books[0]"))
        )
    }

    @Test fun `recursive remove at position 0 on a size 1 array`() {
        assertEquals(
            """{"library":{"books":[]}}""".toJsonObject(),
            """{"library":{"books":["El principito"]}}""".toJsonObject().remove(JsonPath("$.library.books[0]"))
        )
    }

    @Test fun `recursive remove inside item at position 0 on a size 1 array`() {
        assertEquals(
            """{"library":{"books":[{ "surname": "Smith" }, { "name": "Max", "surname": "Smith" }]}}""".toJsonObject(),
            """{"library":{"books":[{ "name": "Joe", "surname": "Smith" }, { "name": "Max", "surname": "Smith" }]}}""".toJsonObject().remove(JsonPath("$.library.books[0].name"))
        )
    }

    @Test fun `recursive remove inside item at position 1 on a size 1 array`() {
        assertEquals(
            """{"library":{"books":[{ "name": "Joe", "surname": "Smith" }, { "surname": "Smith" }]}}""".toJsonObject(),
            """{"library":{"books":[{ "name": "Joe", "surname": "Smith" }, { "name": "Max", "surname": "Smith" }]}}""".toJsonObject().remove(JsonPath("$.library.books[1].name"))
        )
    }

    @Test fun `recursive add at position 1 on a size 1 array`() {
        assertEquals(
            """{"items":[{"title":"El principito"},{"someKey":"some value"}]}""".toJsonObject(),
            """{"items":[{"title":"El principito"}]}""".toJsonObject().add(JsonPath("$.items[1]"), """{"someKey":"some value"}""".trimMargin().toJsonObject())
        )
    }

    @Test fun `recursive set first element of array`() {
        assertEquals(
            """{"items":[{"someKey":"some value"}]}""".toJsonObject(),
            """{"items":[{"title":"El principito"}]}""".toJsonObject().set(JsonPath("$.items[0]"), """{"someKey":"some value"}""".trimMargin().toJsonObject())
        )
    }

    @Test fun `recursive modify last element of array`() {
        assertEquals(
            """{"items":[{"title":"El principito","someKey":"some value"}]}""".toJsonObject(),
            """{"items":[{"title":"El principito"}]}""".toJsonObject().set(JsonPath("$.items[last].someKey"), "some value")
        )
    }

    @Test fun `recursive set the only element of the array by first and last access - 1`() {
        assertEquals(
            """{"items":[{"title":"some value"}]}""".toJsonObject(),
            """{"items":[{"title":"El principito"}]}""".toJsonObject().set(JsonPath("$.items[0].title"), "some value"),
            """'set("$.items[0].title", "some value")' to '{"items":[{"title":"El principito"}]}' should be '{"items":[{"title":"some value"}]}'"""
        )
    }

    @Test fun `recursive set the only element of the array by first and last access - 2`() {
        assertEquals(
            """{"items":[{"title":"some value"}]}""".toJsonObject(),
            """{"items":[{"title":"El principito"}]}""".toJsonObject().set(JsonPath("$.items[last].title"), "some value"),
            """'set("$.items[last].title", "some value")' to '{"items":[{"title":"El principito"}]}' should be '{"items":[{"title":"some value"}]}'"""
        )
    }

    @Test fun `recursive set the only element of the array by first and last access - 3`() {
        assertEquals(
            """{"items":[{"title":"El principito"}]}""".toJsonObject().set(JsonPath("$.items[0].title"), "some value"),
            """{"items":[{"title":"El principito"}]}""".toJsonObject().set(JsonPath("$.items[last].title"), "some value"),
            "setting the first element or the last on a one element list should be the same"
        )
    }

    @Test fun `recursive modify element on array at first index`() {
        assertThrows<JsonPathMissingArrayElementException>("item at index 1 does not exist, so we cannot modify it") {
            """{"items":[{"title":"El principito"}]}""".toJsonObject().set(JsonPath("$.items[1].someKey"), "some value")
        }
    }

    @Test fun `recursive add element on array at first index`() {
        assertEquals(
            """{"items":["some value","El principito"]}""".toJsonObject(),
            """{"items":["El principito"]}""".toJsonObject().add(JsonPath("$.items[0]"), "some value")
        )
    }

    @Test fun `recursive add element on array at last index`() {
        assertEquals(
            """{"items":["El principito","some value"]}""".toJsonObject(),
            """{"items":["El principito"]}""".toJsonObject().add(JsonPath("$.items[last]"), "some value")
        )
    }

    @Test fun `recursive set on empty json throws exception`() {
        assertThrows<JsonPathMissingNodeException> {
            """{}""".toJsonObject().set(JsonPath("$.items[last].someKey"), "some value")
        }
    }

    @Test fun `recursive add on empty json throws exception`() {
        assertThrows<JsonPathMissingNodeException> {
            """{}""".toJsonObject().set(JsonPath("$.items[last].someKey"), "some value")
        }
    }

    @Test fun `get JsonArray by regex`() {
        assertEquals(
            """["El principito"]""".toJsonElement(),
            """{"items":["El principito"]}""".toJsonObject().getNullableByPath(JsonPath("$.R={${Regex("items?")}}"))
        )
    }

    @Test fun `get JsonArray item by regex`() {
        assertEquals(
            """El principito""".toJsonElement(),
            """{"items":["El principito"]}""".toJsonObject().getNullableByPath(JsonPath("$.R={${Regex("items?")}}[0]"))
        )
    }

    @Test fun `get JsonObject by regex`() {
        assertEquals(
            """{"title":"El principito"}""".toJsonElement(),
            """{"items":{"title":"El principito"}}""".toJsonObject().getNullableByPath(JsonPath("$.R={${Regex("items?")}}"))
        )
    }

    @Test fun `get JsonPrimitive by regex`() {
        assertEquals(
            "El principito".toJsonElement(),
            """{"item":"El principito"}""".toJsonObject().getNullableByPath(JsonPath("$.R={${Regex("items?")}}"))
        )
    }

    @Test fun `get JsonPrimitive (String)`() {
        assertEquals(
            "El principito",
            """{"item":"El principito"}""".toJsonObject().getStringByPath(JsonPath("$.item"))
        )
    }

    @Test fun `get JsonPrimitive (Int)`() {
        assertEquals(
            1,
            """{"item":1}""".toJsonObject().getIntByPath(JsonPath("$.item"))
        )
    }

    @Test fun `get JsonPrimitive (Double) (2)`() {
        assertEquals(
            2.0,
            """{"item":2}""".toJsonObject().getDoubleByPath(JsonPath("$.item"))
        )
    }

    @Test fun `get JsonPrimitive (Double) (2dot0)`() {
        assertEquals(
            2.0,
            """{"item":2.0}""".toJsonObject().getDoubleByPath(JsonPath("$.item"))
        )
    }

    @Test fun `get JsonObject - JsonNull found`() {
        assertNull(
            """{"item": null}""".toJsonObject().getNullableJsonObjectByPath(JsonPath("$.item"))
        )
    }

    @Test fun `get JsonArray - JsonNull found`() {
        assertNull(
            """{"item": null}""".toJsonObject().getNullableJsonArrayByPath(JsonPath("$.item"))
        )
    }

    @Test fun `get NullableStringByPath - JsonNull found`() {
        assertNull(
            """{"item": null}""".toJsonObject().getNullableStringByPath(JsonPath("$.item"))
        )
    }

    @Test fun `get Boolean where data is String throws`() {
        assertThrows<JsonPathFoundWrongFormatException> { """{"item":"aaa"}""".toJsonObject().getBooleanByPath(JsonPath("$.item")) }
    }
    @Test fun `get Boolean where data is JsonObject throws`() {
        assertThrows<JsonPathFoundWrongFormatException> { """{"item":{"key2":"data2"}}""".toJsonObject().getBooleanByPath(JsonPath("$.item")) }
    }
    @Test fun `get Boolean where data is JsonArray throws`() {
        assertThrows<JsonPathFoundWrongFormatException> { """{"item":[{"key2":"data2"}]}""".toJsonObject().getBooleanByPath(JsonPath("$.item")) }
    }
    @Test fun `get Boolean where key is not found throws`() {
        assertThrows<JsonPathNotFoundException> { """{"item":"aaa"}""".toJsonObject().getBooleanByPath(JsonPath("$.key")) }
    }

    @Test fun `get Int where data is String throws`() {
        assertThrows<JsonPathFoundWrongFormatException> { """{"item":"aaa"}""".toJsonObject().getIntByPath(JsonPath("$.item")) }
    }
    @Test fun `get Int where data is JsonObject throws`() {
        assertThrows<JsonPathFoundWrongFormatException> { """{"item":{"key2":"data2"}}""".toJsonObject().getIntByPath(JsonPath("$.item")) }
    }
    @Test fun `get Int where data is JsonArray throws`() {
        assertThrows<JsonPathFoundWrongFormatException> { """{"item":[{"key2":"data2"}]}""".toJsonObject().getIntByPath(JsonPath("$.item")) }
    }
    @Test fun `get Int where key is not found throws`() {
        assertThrows<JsonPathNotFoundException> { """{"item":"aaa"}""".toJsonObject().getIntByPath(JsonPath("$.key")) }
    }

    @Test fun `get Double where data is String throws`() {
        assertThrows<JsonPathFoundWrongFormatException> { """{"item":"aaa"}""".toJsonObject().getDoubleByPath(JsonPath("$.item")) }
    }
    @Test fun `get Double where data is JsonObject throws`() {
        assertThrows<JsonPathFoundWrongFormatException> { """{"item":{"key2":"data2"}}""".toJsonObject().getDoubleByPath(JsonPath("$.item")) }
    }
    @Test fun `get Double where data is JsonArray throws`() {
        assertThrows<JsonPathFoundWrongFormatException> { """{"item":[{"key2":"data2"}]}""".toJsonObject().getDoubleByPath(JsonPath("$.item")) }
    }
    @Test fun `get Double where key is not found throws`() {
        assertThrows<JsonPathNotFoundException> { """{"item":"aaa"}""".toJsonObject().getDoubleByPath(JsonPath("$.key")) }
    }

    @Test fun `get Float where data is String throws`() {
        assertThrows<JsonPathFoundWrongFormatException> { """{"item":"aaa"}""".toJsonObject().getFloatByPath(JsonPath("$.item")) }
    }
    @Test fun `get Float where data is JsonObject throws`() {
        assertThrows<JsonPathFoundWrongFormatException> { """{"item":{"key2":"data2"}}""".toJsonObject().getFloatByPath(JsonPath("$.item")) }
    }
    @Test fun `get Float where data is JsonArray throws`() {
        assertThrows<JsonPathFoundWrongFormatException> { """{"item":[{"key2":"data2"}]}""".toJsonObject().getFloatByPath(JsonPath("$.item")) }
    }
    @Test fun `get Float where key is not found throws`() {
        assertThrows<JsonPathNotFoundException> { """{"item":"aaa"}""".toJsonObject().getFloatByPath(JsonPath("$.key")) }
    }

    @Test fun `get Long where data is String throws`() {
        assertThrows<JsonPathFoundWrongFormatException> { """{"item":"aaa"}""".toJsonObject().getLongByPath(JsonPath("$.item")) }
    }
    @Test fun `get Long where data is JsonObject throws`() {
        assertThrows<JsonPathFoundWrongFormatException> { """{"item":{"key2":"data2"}}""".toJsonObject().getLongByPath(JsonPath("$.item")) }
    }
    @Test fun `get Long where data is JsonArray throws`() {
        assertThrows<JsonPathFoundWrongFormatException> { """{"item":[{"key2":"data2"}]}""".toJsonObject().getLongByPath(JsonPath("$.item")) }
    }
    @Test fun `get Long where key is not found throws`() {
        assertThrows<JsonPathNotFoundException> { """{"item":"aaa"}""".toJsonObject().getLongByPath(JsonPath("$.key")) }
    }

    @Test fun `get String where data is JsonObject throws`() {
        assertThrows<JsonPathFoundWrongFormatException> { """{"item":{"key2":"data2"}}""".toJsonObject().getStringByPath(JsonPath("$.item")) }
    }
    @Test fun `get String where data is JsonArray throws`() {
        assertThrows<JsonPathFoundWrongFormatException> { """{"item":[{"key2":"data2"}]}""".toJsonObject().getStringByPath(JsonPath("$.item")) }
    }
    @Test fun `get String where key is not found throws not found`() {
        assertThrows<JsonPathNotFoundException> { """{"item":"aaa"}""".toJsonObject().getStringByPath(JsonPath("$.items")) }
    }

    @Test fun `set JsonObject inside non existent array`() {
        assertEquals(
            """{"item":"El principito","items":[{"data":"Some data"}]}""".toJsonElement(),
            """{"item":"El principito"}""".toJsonObject().set(JsonPath("$.items[0].data"), "Some data", true)
        )
    }

    @Test fun `set JsonPrimitive inside non existent array`() {
        assertEquals(
            """{"item":"El principito","items":["Some data"]}""".toJsonElement(),
            """{"item":"El principito"}""".toJsonObject().set(JsonPath("$.items[0]"), "Some data", true)
        )
    }

    @Test fun `set JsonPrimitive by regex - simple`() {
        assertEquals(
            """{"item":"Some data"}""".toJsonElement(),
            """{"item":"El principito"}""".toJsonObject().set(JsonPath("$.R={${Regex("items?")}}"), "Some data")
        )
    }

    @Test fun `add JsonPrimitive by regex - simple`() {
        assertEquals(
            """{"item":"Some data"}""".toJsonElement(),
            """{"item":"El principito"}""".toJsonObject().add(JsonPath("$.R={${Regex("items?")}}"), "Some data")
        )
    }

    @Test fun `set JsonPrimitive by non existent regex - $ R={${books}`() {
        assertEquals(
            """{"item":"El principito"}""".toJsonElement(),
            """{"item":"El principito"}""".toJsonObject().set(JsonPath("$.R={${Regex("books?")}}"), "Some data")
        )
    }

    @Test fun `set JsonPrimitive by non existent regex - $ library R={${books}`() {
        assertEquals(
            """{"library":{"items":[{"title":"El principito"}]}}""".toJsonElement(),
            """{"library":{"items":[{"title":"El principito"}]}}""".toJsonObject().set(
                JsonPath("$.library.R={${Regex("books?")}}"),
                "Some data"
            )
        )
    }

    @Test fun `set JsonPrimitive by non existent regex - $ library R={${books} title - throws MissingNodeException`() {
        assertThrows<JsonPathMissingNodeException> {
            """{"library":{"items":[{"title":"El principito"}]}}""".toJsonObject().set(
                JsonPath("$.library.R={${Regex("books?")}}.title"),
                "Some data"
            )
        }
    }

    @Test fun `set with regex element0 - simple`() {
        assertEquals(
            """{"items":["Some data"]}""".toJsonElement(),
            """{"items":["El principito"]}""".toJsonObject().set(JsonPath("$.R={${Regex("items?")}}[0]"), "Some data")
        )
    }

    @Test fun `add with regex element0 - simple`() {
        assertEquals(
            """{"items":["Some data","El principito"]}""".toJsonElement(),
            """{"items":["El principito"]}""".toJsonObject().add(JsonPath("$.R={${Regex("items?")}}[0]"), "Some data")
        )
    }

    @Test fun `set with regexDOTitem - simple`() {
        assertEquals(
            """{"items":{"book":"Some data"}}""".toJsonElement(),
            """{"items":{"book":"El principito"}}""".toJsonObject().set(JsonPath("$.R={${Regex("items?")}}.book"), "Some data")
        )
    }

    /**
     * Regex to identify a UUID
     */
    val UUID_REGEX =
        "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"

    @Test fun `set JsonPrimitive by regex - complex`() {
        assertEquals(
            """{"8b24a949-682a-42b8-8897-fcfb545c1541":"Some data"}""".toJsonElement(),
            """{"8b24a949-682a-42b8-8897-fcfb545c1541":"El principito"}""".toJsonObject().set(JsonPath("$.R={${Regex(UUID_REGEX)}}"), "Some data")
        )
    }

    @Test fun `set JsonPrimitive by regex - complex 2`() {
        assertEquals(
            """{"8b24a949-682a-42b8-8897-fcfb545c1541": { "id": "Some data" }}""".toJsonElement(),
            """{"8b24a949-682a-42b8-8897-fcfb545c1541": { "id": "El principito" }}""".toJsonObject().set(JsonPath("$.R={${Regex(UUID_REGEX)}}.id"), "Some data")
        )
    }

    @Test fun `use regex containing dot`() {
        assertEquals(
            """{"com.package.example":"Some data"}""".toJsonElement(),
            """{"com.package.example":"El principito"}""".toJsonObject().set(JsonPath("$.R={\\w*\\.\\w*\\.\\w*}"), "Some data")
        )
    }

    @DisplayName("""Remove empty jsons: simplest case""")
    @Test fun removeEmptyJsonObjectFieldsNormal() {
        assertEquals(null, """{"center":{}}""".toJsonObject().removeEmptyJsonObjectFields())
    }

    @DisplayName("""Remove null jsons: simplest case""")
    @Test fun removeNullJsonObjectFieldsNormal() {
        assertEquals(null, """{"center":null}""".toJsonObject().removeEmptyJsonObjectFields())
    }

    @DisplayName("""Remove empty jsons: inner object: 1 step""")
    @Test fun removeEmptyJsonObjectFieldsInsideObject1Step() {
        assertEquals("""{"wrapper": {"a": 1}}""".toJsonObject(), """{"wrapper": { "center":{}, "a": 1} }""".toJsonObject().removeEmptyJsonObjectFields())
    }

    @DisplayName("""Remove null jsons: inner object: 1 step""")
    @Test fun removeNullJsonObjectFieldsInsideObject1Step() {
        assertEquals("""{"wrapper": {"a": 1}}""".toJsonObject(), """{"wrapper": { "center":null, "a": 1} }""".toJsonObject().removeEmptyJsonObjectFields())
    }

    @DisplayName("""Remove empty jsons: inner object: 2 steps""")
    @Test fun removeEmptyJsonObjectFieldsInsideObject2Steps() {
        assertEquals(null, """{"wrapper": { "center":{}} }""".toJsonObject().removeEmptyJsonObjectFields())
    }

    @DisplayName("""Remove empty jsons: inner array: 1 step""")
    @Test fun removeEmptyJsonObjectFieldsInsideArray1Step() {
        assertEquals("""{"wrapper": [{"a": 1}]}""".toJsonObject(), """{"wrapper": [ {"center":{}, "a": 1} ]}""".toJsonObject().removeEmptyJsonObjectFields())
    }

    @DisplayName("""Remove empty jsons: inner array: 2 steps""")
    @Test fun removeEmptyJsonObjectFieldsInsideArray2Step() {
        assertJsonEquals(null, """{"wrapper": [ {"center":{}} ]}""".toJsonObject().removeEmptyJsonObjectFields())
    }

    @DisplayName("""Remove empty jsons: real example""")
    @Test fun removeEmptyJsonObjectFieldsInsideRealExample() {
        assertJsonEquals(
            """{"added_objects":[{"uuid":"00000000-0000-0000-0000-000000000027","type":"photo"}],"layouts":[{"TODO": "TODO V2LayersLayoutConverter"}],"groups": [{"TODO": "TODO V2LayersLayoutConverter"}],"extra-field1": "extra1"}""".toJsonObject(),
            """{"added_objects":[{"uuid":"00000000-0000-0000-0000-000000000027","type":"photo","settings":{"transform":{"position":{},"flipped":{}},"brush":[],"shadows":[]}}],"layouts":[{"TODO":"TODO V2LayersLayoutConverter"}],"groups":[{"TODO":"TODO V2LayersLayoutConverter"}],"frame":[],"extra-field1":"extra1"}""".toJsonObject().removeEmptyJsonObjectFields()
        )
    }

    private fun assertJsonEquals(json1: JsonObject?, json2: JsonObject?) = assertEquals(json1, json2, json1?.compareTo(json2!!).toString())

    @DisplayName("""Remove empty jsons: real example4""")
    @Test fun removeEmptyJsonObjectFieldsInsideRealExample4() {
        assertJsonEquals(
            """{"added_objects":[{"uuid":"00000000-0000-0000-0000-000000000027","type":"photo"}],"extra-field1":"extra1"}""".toJsonObject(),
            """{"added_objects":[{"uuid":"00000000-0000-0000-0000-000000000027","type":"photo","settings":{"transform":{"position":{},"flipped":{}},"brush":[],"shadows":[]}}],"extra-field1":"extra1"}""".toJsonObject().removeEmptyJsonObjectFields()
        )
    }

    @DisplayName("""Remove empty jsons: real example3""")
    @Test fun removeEmptyJsonObjectFieldsInsideRealExample3() {
        assertJsonEquals(
            """{"added_objects":[{"uuid": "00000000-0000-0000-0000-000000000027","type": "photo"}]}""".toJsonObject(),
            """{"added_objects":[{"uuid":"00000000-0000-0000-0000-000000000027","type":"photo","settings":{"transform":{"position":{},"flipped":{}},"brush":[],"shadows":[]}}]}""".toJsonObject().removeEmptyJsonObjectFields()
        )
    }

    @DisplayName("""Remove empty jsons: real example2""")
    @Test fun removeEmptyJsonObjectFieldsInsideRealExample2() {
        assertJsonEquals(
            """{"added_objects":[{"uuid": "00000000-0000-0000-0000-000000000027","type": "photo"}]}""".toJsonObject(),
            """{"added_objects":[{"uuid":"00000000-0000-0000-0000-000000000027","type":"photo"}]}""".toJsonObject().removeEmptyJsonObjectFields()
        )
    }

    @DisplayName("""Remove empty jsons: real example5""")
    @Test fun removeEmptyJsonObjectFieldsInsideRealExample5() {
        assertJsonEquals(
            """{"added_objects":[{"uuid": "00000000-0000-0000-0000-000000000027","type": "photo"}]}""".toJsonObject(),
            """{"added_objects":[{"uuid":"00000000-0000-0000-0000-000000000027","type":"photo"}],"frame":[]}""".toJsonObject().removeEmptyJsonObjectFields()
        )
    }

    @DisplayName("""Transform JsonObject to final JSONPATHs""")
    @Test fun toJsonPaths() {
        assertEquals(
            listOf(JsonPath("$.wrapper.center.x"), JsonPath("$.wrapper.center.y")),
            """{ "wrapper": {"center": { "x": 1, "y": 2 } } }""".toJsonObject().toJsonPaths()
        )
        assertEquals(
            listOf(JsonPath("$.wrapper[0].center.x"), JsonPath("$.wrapper[0].center.y")),
            """{ "wrapper": [ {"center": { "x": 1, "y": 2 } } ] }""".toJsonObject().toJsonPaths()
        )
        assertEquals(
            listOf(JsonPath("$.wrapper[0].center.x"), JsonPath("$.wrapper[0].center.y"), JsonPath("$.wrapper[1].center.x"), JsonPath("$.wrapper[1].center.y")),
            """{ "wrapper": [ {"center": { "x": 1, "y": 2 } }, {"center": { "x": 3, "y": 4 } } ] }""".toJsonObject().toJsonPaths()
        )
    }

    @DisplayName("""Trying to access non existent index in array""")
    @Test fun getArray() {
        assertNull("""{"items":[]}""".toJsonObject().getNullableJsonObjectByPath(JsonPath("$.items[0]")))
    }

    @DisplayName("""Add new item to array of length 0 by set index 0""")
    @Test fun setArrayIndex0() {
        assertEquals(
            """{"items":[{"x":6}]}""".toJsonObject(),
            """{"items":[]}""".toJsonObject().set(JsonPath("$.items[0]"), """{"x":6}""".toJsonElement()),
        )
    }

    @DisplayName("""Add new item to array of length 1 by set index 1""")
    @Test fun setArrayIndex1() {
        assertEquals(
            """{"items":[{"x":5,"y":4,"z":3},{"x":6}]}""".toJsonObject(),
            """{"items":[{"x":5,"y":4,"z":3}]}""".toJsonObject()
                .set(JsonPath("$.items[1].x"), 6.toJsonElement(), addMiddlePathsIfNotExist = true),
        )
    }

    @Test fun modifyArrayIndex1() {
        assertEquals(
            """{"items":[{"x":5,"y":4,"z":3},{"x":6,"y":5}]}""".toJsonObject(),
            """{"items":[{"x":5,"y":4,"z":3},{"x":6}]}""".toJsonObject()
                .set(JsonPath("$.items[1].y"), 5.toJsonElement()),
        )
    }

    @DisplayName("""Merge non colliding simple items""")
    @Test fun merge1() {
        assertEquals(
            """{"a": 1, "b": 2}""".toJsonObject(),
            """{"a": 1}""".toJsonObject().merge("""{"b": 2}""".toJsonObject())
        )
    }

    @DisplayName("""Merge colliding simple items""")
    @Test fun merge2() {
        assertEquals(
            """{"a": 1, "b": 3}""".toJsonObject(),
            """{"a": 1, "b": 2}""".toJsonObject().merge("""{"b": 3}""".toJsonObject())
        )
    }

    @DisplayName("""Merge non colliding complex items""")
    @Test fun merge3() {
        assertEquals(
            """{"point":{"a":1,"b":3}}""".toJsonObject(),
            """{"point":{"a":1,"b":2}}""".toJsonObject().merge("""{"point":{"b":3}}""".toJsonObject())
        )
    }

    @DisplayName("""Merge colliding complex items""")
    @Test fun merge4() {
        assertEquals(
            """{"point":{"a":1,"b":3}}""".toJsonObject(),
            """{"point":{"a":1,"b":2}}""".toJsonObject().merge("""{"point":{"b":3}}""".toJsonObject())
        )
    }

    @DisplayName("""Merge colliding complex and simple items""")
    @Test fun merge5() {
        assertEquals(
            """{"point":{"a":1,"b":3},"c":1}""".toJsonObject(),
            """{"point":{"a":1,"b":3}}""".toJsonObject().merge("""{"c":1}""".toJsonObject())
        )
    }

    @Test fun mergeArray1() {
        assertEquals(
            """{"points":[{"x":1,"y":1},{"x":2,"y":2},{"x":1,"y":1,"z":3},{"x":2,"y":2,"z":3}]}""".toJsonObject(),
            """{"points":[{"x":1,"y":1},{"x":2,"y":2}]}""".toJsonObject().merge("""{"points":[{"x":1,"y":1,"z":3},{"x":2,"y":2,"z":3}]}""".toJsonObject())
        )
    }

    @Test fun mergeArray2() {
        assertEquals(
            """{"points":[{"x":1,"y":1},{"x":2,"y":1},{"x":5,"y":1},{"x":6,"y":1}]}""".toJsonObject(),
            """{"points":[{"x":1,"y":1},{"x":2,"y":1}]}""".toJsonObject().merge("""{"points":[{"x":5,"y":1},{"x":6,"y":1}]}""".toJsonObject())
        )
    }

    @Test fun arrayStar0() {
        assertEquals(
            listOf("""{"main":false}""".toJsonElement(), """{"main":true}""".toJsonElement()),
            """{"items":[{"main":false},{"main":true}]}""".toJsonObject()
                .getAllByPath(JsonPath("$.items[*]")),
        )
    }

    @Test fun arrayStar1() {
        assertEquals(
            listOf(false.toJsonElement(), true.toJsonElement()),
            """{"items":[{"main":false},{"main":true}]}""".toJsonObject()
                .getAllByPath(JsonPath("$.items[*].main")),
        )
    }

    @Test fun arrayStar2() {
        assertEquals(
            listOf(false.toJsonElement(), true.toJsonElement(), true.toJsonElement(), false.toJsonElement()),
            """{"items":[{"items2":[{"main":false},{"main":true}]},{"items2":[{"main":true},{"main":false}]}]}""".toJsonObject()
                .getAllByPath(JsonPath("$.items[*].items2[*].main")),
        )
    }
}
