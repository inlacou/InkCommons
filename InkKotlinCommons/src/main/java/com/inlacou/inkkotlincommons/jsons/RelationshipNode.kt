package com.inlacou.inkkotlincommons.jsons

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

internal operator fun String.times(times: Int): String {
    var sum = ""
    var index = 0
    while (index < times) {
        sum += this
        index++
    }
    return sum
}

data class InitialRelationshipNode<UUID>(
    val oneself: UUID,
    val self: JsonElement?,
    val parent: UUID?,
    val children: List<UUID>
) {
    override fun toString(): String = "Item <$oneself>($self) has ${if(parent==null) "no parent" else "parent <$parent>"} and children [${children.joinToString()}]"
}

data class RelationshipNode<UUID>(
    val oneself: UUID,
    val self: JsonElement?,
    val children: List<RelationshipNode<UUID>>
) {
    fun addChild(uuid: UUID) = copy(children = children + RelationshipNode(uuid, self, emptyList()))
    fun addChild(uuid: RelationshipNode<UUID>) = copy(children = children + uuid)

    override fun toString(): String = "<$oneself>($self)${children.map { it.toString(1) }}"
    fun toString(nestLevel: Int): String {
        val lead = "\n"+("\t"*nestLevel)
        return lead+"<$oneself>($self)${children.mapIndexed { index, it -> 
            it.toString(nestLevel+1)+if(index==children.lastIndex) lead else ""
        }}"
    }
}

fun <UUID> JsonObject.toInitialRelationshipNodes(
    parentUuidRetriever: (JsonObject) -> UUID?,
    selfUuidRetriever: (JsonObject) -> UUID?,
    selfRetriever: (JsonObject) -> JsonElement?,
    childUuidRetriever: (JsonObject) -> List<UUID>,
): List<InitialRelationshipNode<UUID>> {
    val selfKey = selfUuidRetriever(this)
    return (keys.map { key -> get(key) }
        .filterIsInstance<JsonObject>()
        .map { it.toInitialRelationshipNodes(parentUuidRetriever, selfUuidRetriever, selfRetriever, childUuidRetriever) }
        .flatten() +
            if(selfKey != null) {
                listOf(
                    InitialRelationshipNode(
                        oneself = selfKey,
                        self = selfRetriever(this),
                        parent = parentUuidRetriever(this),
                        children = childUuidRetriever(this),
                    )
                )
            } else emptyList())
}

fun <UUID> JsonArray.toInitialRelationshipNodes(
    parentUuidRetriever: (JsonObject) -> UUID?,
    selfUuidRetriever: (JsonObject) -> UUID?,
    selfRetriever: (JsonObject) -> JsonElement?,
    childUuidRetriever: (JsonObject) -> List<UUID>,
): List<InitialRelationshipNode<UUID>> {
    return mapNotNull {
        when(it) {
            is JsonObject -> it.toInitialRelationshipNodes(parentUuidRetriever, selfUuidRetriever, selfRetriever, childUuidRetriever)
            is JsonArray -> it.toInitialRelationshipNodes(parentUuidRetriever, selfUuidRetriever, selfRetriever, childUuidRetriever)
            else -> null
        }
    }.flatten()
}

/**
 * If all are related (correctly), it should be a [List] with a single element.
 * If we have two distinct "families", we will have a [List] with two elements, and so on.
 */
fun <UUID> List<InitialRelationshipNode<UUID>>.toFinal(): List<RelationshipNode<UUID>> {
    /* Alias for readability*/
    val initialRelationships = this

    val rootNodes: MutableList<InitialRelationshipNode<UUID>> = initialRelationships
        .filter { possibleParent -> possibleParent.parent == null && initialRelationships.any { it.children.contains(possibleParent.oneself) }.not() }
        .toMutableList()

    fun getNode(uuid: UUID): RelationshipNode<UUID> {
        val item = try {
            initialRelationships.first { it.oneself == uuid }
        }catch (nsee: NoSuchElementException) {
            println("No element found with $uuid on ${initialRelationships.map { it.oneself }}")
            throw nsee
        }
        return RelationshipNode(
            oneself = item.oneself,
            self = item.self,
            children = item.children.map { getNode(it) }
        )
    }

    return rootNodes.map { getNode(it.oneself) }.toMutableList()
}
