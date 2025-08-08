package com.inlacou.inkkotlinextensions

/**
 * Does combination of two lists:
 *  listOf("A", "B").combineWith(listOf(1, 2))
 * and returns:
 *  listOf(
 *     Pair("A", 1),
 *     Pair("B", 2),
 *  )
 *
 * @receiver [Collection]<[T]>
 *
 * @param [other] [Collection]<[S]>
 *
 * @return [Collection]<[Pair]<[T], [S]>>
 *
 * @author Iñigo Lacoume
 */
internal fun <T, S> Collection<T>.pairWith(other: Collection<S>): Collection<Pair<T, S>> {
    val other = other.toList()
    return mapIndexed { index, first -> Pair(first, other[index]) }
}

/**
 * Does combination of two lists:
 *  listOf("A", "B").combineWith(listOf(1, 2))
 * and returns:
 *  listOf(
 *     Pair("A", 1),
 *     Pair("A", 2),
 *     Pair("B", 1),
 *     Pair("B", 2),
 *  )
 *
 * @receiver [Collection]<[T]>
 *
 * @param [other] [Collection]<[S]>
 *
 * @return [Collection]<[Pair]<[T], [S]>>
 *
 * @author Iñigo Lacoume
 */
internal fun <T, S> Collection<T>.combineWith(other: Collection<S>): Collection<Pair<T, S>> {
    return map { first -> other.map { second -> Pair(first, second) } }.flatten()
}

/**
 * Does combination of three lists:
 *  listOf("A", "B").combineWith(listOf(1, 2), listOf(true, false))
 * Returns:
 *  listOf(
 *      Triple("A", 1, true),
 *      Triple("A", 1, false),
 *      Triple("A", 2, true),
 *      Triple("A", 2, false),
 *      Triple("B", 1, true),
 *      Triple("B", 1, false),
 *      Triple("B", 2, true),
 *      Triple("B", 2, false),
 *  )
 *
 * @receiver [Collection]<[T]>
 *
 * @param [other1] [Collection]<[S]>
 * @param [other2] [Collection]<[P]>
 *
 * @return [Collection]<[Triple]<[T], [S], <[P]>>
 *
 * @author Iñigo Lacoume
 */
internal fun <T, S, P> Collection<T>.combineWith(other1: Collection<S>, other2: Collection<P>): Collection<Triple<T, S, P>> {
    return combineWith(other1).combineWith(other2).map { Triple(it.first.first, it.first.second, it.second) }
}

/**
 * Alternative version of the [MutableList.addAll] method that instead of returning a Boolean, it returns the same list the items were added to.
 *
 * @receiver [MutableList]<[T]>
 *
 * @param [other] [Collection]<[T]>
 *
 * @return [MutableList]<[T]>
 *
 * @author Iñigo Lacoume
 */
internal fun <T> MutableList<T>.addAll(vararg other: Collection<T>): MutableList<T> = apply {
    other.forEach { addAll(it) }
}

/**
 * Maps all items of the list to the specified type [R].
 *
 * @receiver [Iterable]<[Any]>
 *
 * @sample samples.collections.Collections.Filtering.filterIsInstanceTo
 *
 * @return [List]<[R]>
 *
 * @author Iñigo Lacoume
 */
internal inline fun <reified R> Iterable<*>.mapInstance(): List<R> {
    return mapInstanceTo(ArrayList())
}

/**
 * Appends all elements that are instances of specified type parameter [R] to the given [destination].
 *
 * @see [filterIsInstanceTo]
 *
 * @author Iñigo Lacoume
 */
internal inline fun <reified R, C : MutableCollection<in R>> Iterable<*>.mapInstanceTo(destination: C): C {
    for (element in this) destination.add(element as R)
    return destination
}

/**
 * Same as [Iterable.forEachIndexed] but on [Iterable.reversed] direction.
 *
 * The indexes are reversed too, so it will start with the index being the max index and go until 0.
 *
 * @author Iñigo Lacoume
 */
inline fun <T> Iterable<T>.forEachIndexedReversed(action: (index: Int, T) -> Unit) {
    var index = toList().size - 1
    this.reversed().forEach { action(index--, it) }
}

/**
 * If [index] is 5 on a 3 element list, it will take the second element.
 */
fun <T> List<T>.getRepeating(index: Int): T =
    if(index>=size) this[index%size]
    else this[index]
