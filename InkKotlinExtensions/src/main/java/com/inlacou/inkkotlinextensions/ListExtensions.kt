package com.inlacou.inkkotlinextensions

import java.lang.IllegalArgumentException
import java.util.stream.Stream
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Returns a list containing all elements of the original collection and the elements contained in the given [other] collection without duplicates.
 *
 * In case of duplicate, takes the element from the first list.
 */
fun <T> Iterable<T>.mergeBy(other: Iterable<T>, equals: (T, T) -> Boolean): List<T> {
	val set = mutableListOf<T>()
	set.addAll(this)
	other.forEach { second ->
		if(set.find { equals(second, it) }==null) set.add(second)
	}
	return set
}

/**
 * Returns a list containing all elements of the original collection except the elements contained in the given [other] collection.
 */
fun <T> Iterable<T>.minusBy(other: Iterable<T>, equals: (T, T) -> Boolean): List<T> {
	val set = mutableListOf<T>()
	this.forEach { first ->
		val present = other.find { equals(first, it) }!=null
		if(!present) set.add(first)
	}
	return set
}

/**
 * Returns a set containing all elements that are contained by both this collection and the specified collection.
 *
 * The returned set preserves the element iteration order of the original collection.
 *
 * To get a set containing all elements that are contained at least in one of these collections use [union].
 */
fun <T> Iterable<T>.intersectBy(other: Iterable<T>, equals: (T, T) -> Boolean): Set<T> {
	val set = mutableSetOf<T>()
	this.forEach { first ->
		val present = other.find { equals(first, it) }!=null
		if(present) set.add(first)
	}
	return set
}

fun <T> Iterator<T>.toList(): List<T> {
	val mutableList = mutableListOf<T>()
	while (hasNext()) mutableList.add(next())
	return mutableList
}

fun <T> Stream<T>.toList(): List<T> = mutableListOf<T>().apply { this@toList.forEach { this.add(it) } }

fun <T> MutableList<T>.concat(otherList: List<T>): MutableList<T> {
	addAll(otherList)
	return this
}

fun <T> MutableListIterator<T>.addBefore(item: T){
	previous()
	add(item)
	next()
}

fun <T, V> List<Pair<T, V>>.toHashMap(): HashMap<T, V> {
	val values = hashMapOf<T, V>()
	forEach { values[it.first] = it.second }
	return values
}

fun <T> List<T>.safeGet(i: Int): T? {
	return try {
		get(i)
	}catch (ioobe: IndexOutOfBoundsException){
		null
	}
}

fun <T> List<T>.getItemByPercentage(f: Float): T? {
	var index = (size*f).toInt()
	if(index>=size) index = size-1
	if(index<0) index = 0
	return this[index]
}

/**
 * Gets nearest selectable item. Ignores non selectable items.
 * @param roughIndex approximate index to start search. Rounded up. On equal distance, higher index has priority.
 * @param selectable method to filter only to selectable items
 * @return T
 * @throws NoSuchElementException when there is no item to select (no selectable items).
 */
fun <T> List<T>.getNearestSelectable(roughIndex: Float, selectable: (T) -> Boolean): T {
	val index = roughIndex.roundToInt()
	val roundUp = index>=roughIndex
	return getNextNearestSelectable(lastIndex = index, goUp = !roundUp, selectable = selectable)
}

/**
 * Gets next selectable item on list. Ignores non selectable items.
 * @param lastIndex to know where we are
 * @param stepSize to know step size (it increases, as we go bouncing top/bottom because we check for the nearest one on any direction.
 * @param goUp to know current direction
 * @param topReached if top is reached, we no longer need to go in that direction
 * @param bottomReached if bottom reached, we no longer need to go in that direction
 * @param selectable method to filter only to selectable items
 * @return T
 * @throws NoSuchElementException when there is no item to select (no selectable items in any direction, or no items at all, for example).
 */
private fun <T> List<T>.getNextNearestSelectable(lastIndex: Int, stepSize: Int = 0, goUp: Boolean, topReached: Boolean = false, bottomReached: Boolean = false, selectable: (T) -> Boolean): T {
	var auxTopReached = topReached
	var auxBottomReached = bottomReached
	val newIndex = lastIndex+
			if(goUp) stepSize
			else -stepSize
	val currentItem = try {
		get(newIndex)
	}catch (ioobe: IndexOutOfBoundsException){
		if(goUp) auxTopReached = true
		if(!goUp) auxBottomReached = true
		null
	}
	return if(currentItem!=null && selectable.invoke(currentItem)){
		currentItem
	}else if(topReached && bottomReached) {
		throw NoSuchElementException()
	}else {
		getNextNearestSelectable(lastIndex = newIndex, stepSize = when {
			topReached -> 1
			bottomReached -> 1
			else -> stepSize+1
		}, goUp = when {
			topReached -> false
			bottomReached -> true
			else -> !goUp
		}, topReached = auxTopReached, bottomReached = auxBottomReached, selectable = selectable)
	}
}

/**
 * Remove last appearance of given item
 */
fun <T> List<T>.removeLastItem(item: T): List<T> {
	return toMutableList().apply { removeAt(lastIndexOf(item)) }
}

/**
 * Returns a list containing all elements of the original collection from the given element onward (not containing given element)
 *
 * If item is not found, it returns the whole list.
 */
fun <T> List<T>.sublistAfter(item: T): List<T> {
	return takeLast(this.size-this.indexOf(item)-1)
}

/**
 * Returns a list containing all elements of the original collection from the given element onward (containing given element)
 *
 * If item is not found, it returns the whole list.
 */
fun <T> List<T>.sublistFrom(item: T): List<T> {
	return takeLast(this.size-this.indexOf(item))
}

/*fun <T> List<T>.safeRemoveAt(index: Int): List<T> {
	return if(0<index && index<this.size) {
		val aux = mutableListOf<T>()
		aux.addAll(this.subList(0, index))
		aux.addAll(this.subList(index+1, this.size))
		aux
	}else{
		this
	}
}*/

fun <T> List<T>.sublistBy(by: (T) -> Any?): List<List<T>> {
	val map = hashMapOf<Any, MutableList<T>>()
	val unKeyed = mutableListOf<T>()
	this.forEach {
		val key = by.invoke(it)
		if(key!=null) {
			val list = map[key] ?: mutableListOf()
			list.add(it)
			map[key] = list
		} else unKeyed.add(it)
	}
	val result = map.toList().map { it.second }
	result.toMutableList().add(unKeyed)
	return result
}

fun <T, P> List<T>.pairSublistBy(by: (T) -> P?): List<Pair<P?, List<T>>> {
	val map = hashMapOf<P?, MutableList<T>>()
	val unKeyed = mutableListOf<T>()
	this.forEach {
		val key = by.invoke(it)
		if(key!=null) {
			val list = map[key] ?: mutableListOf()
			list.add(it)
			map[key] = list
		} else unKeyed.add(it)
	}
	val result = map.toList()
	result.toMutableList().add(Pair(null, unKeyed))
	return result
}

/**
 * Get next value (given index+1) safely (always return value) and looping (if index==maxIndex, index+1 is the first value, i.e. start again from the start)
 */
fun <T> List<T>.safeNextLooping(currentIndex: Int): T {
	return try {
		get(currentIndex+1)
	}catch (ioobe: IndexOutOfBoundsException) {
		get(0)
	}
}

/**
 * Get previous value (given index-1) safely (return null if no value) and looping (if index==0, index-1 is the last value, i.e. start again from the rear)
 */
fun <T> List<T>.safePreviousLooping(currentIndex: Int): T? {
	return try {
		get(currentIndex-1)
	}catch (ioobe: IndexOutOfBoundsException){
		try {
			get(size-1)
		}catch (ioobe: IndexOutOfBoundsException){
			null
		}
	}
}

fun <T> List<T>.getFastestPathFromTwoIndexInCircularList(first: Int, second: Int): Int {
	val curatedFirst = if(first>second) first else first+size
	val curatedSecond = if(second>first) second else second+size
	val firstValue = second-first
	val secondValue = curatedSecond-curatedFirst
	return if(abs(firstValue) < abs(secondValue)) firstValue
	else secondValue
}

fun <T> MutableList<T>.safeRemoveAt(i: Int): T? {
	return try {
		removeAt(i)
	}catch (ioobe: IndexOutOfBoundsException){
		null
	}
}

fun <T> MutableList<T>.safeAddBeforeLast(item: T): MutableList<T> {
	return try {
		add(lastIndex, item)
		this
	}catch (ioobe: IndexOutOfBoundsException){
		mutableListOf(item)
	}
}

fun <T> MutableList<T>.addBeforeLast(item: T): MutableList<T> {
	add(lastIndex, item)
	return this
}

fun <T> MutableList<T>.replaceAll(item: T, replaceCondition: (Int, T) -> Boolean): MutableList<T> {
	(0 until size).forEach { index ->
		val currentItem = get(index)
		if(replaceCondition(index, currentItem)) {
			remove(currentItem)
			add(index, item)
		}
	}
	return this
}

/**
 * @param indexToAddIfNotFound if -1, add at end
 */
fun <T> MutableList<T>.replaceAllOrAddAtIfNotFound(newItem: T, indexToAddIfNotFound: Int, replaceCondition: (T) -> Boolean): MutableList<T> {
	var found = false
	(0 until size).forEach { index ->
		val currentItem = get(index)
		if (replaceCondition(currentItem)) {
			remove(currentItem)
			add(index, newItem)
			found = true
		}
	}
	if (!found)
		if(indexToAddIfNotFound==-1) add(size, newItem)
		else add(indexToAddIfNotFound, newItem)
	return this
}

fun <T> List<T>.avoid(n: Int): List<T> {
	return if(n==0) this
	else subList(n, size)
}

fun <T> List<T>.avoidOrEmpty(n: Int): List<T> {
	return try{
		if(n==0) this
		else subList(n, size)
	}catch (ioobe: java.lang.IndexOutOfBoundsException){
		listOf()
	}catch (iae: IllegalArgumentException) {
		System.err.println("error arose on avoidOrEmpty method | n: $n | list: $this")
		listOf()
	}
}

val <T> List<T>.maxIndex: Int
	get() = size-1

inline fun <T> List<T>.reversedForEach(operation: (T) -> Unit) = reversed().forEach(operation)

fun <T> MutableList<T>.concurrentRemove(item: T) {
	val iterator = iterator()
	while(iterator.hasNext()) {
		val currentItem = iterator.next()
		if(currentItem==item) {
			synchronized(this) {
				iterator.remove()
			}
		}
	}
}

fun <T, V> List<T>.toPairs(second: V): List<Pair<T, V>> {
	return map { Pair(it, second) }
}

/**
 * Intended use of merging two lists with the same number of items
 */
fun <T, V> List<T>.toPairs(second: List<V>): List<Pair<T, V>> {
	return mapIndexed { index, it -> Pair(it, second[index]) }
}

fun <T> Iterable<T>.groupConsecutiveBy(groupIdentifier: (T, T) -> Boolean) =
	if (!this.any())
		emptyList()
	else
		this.drop(1)
			.fold(mutableListOf(mutableListOf(this.first()))) { groups, t ->
				groups.last().apply {
					if (groupIdentifier.invoke(last(), t)) {
						add(t)
					} else {
						groups.add(mutableListOf(t))
					}
				}
				groups
			}

fun <T> HashMap<T, Boolean>.merge(list: List<T>, default: Boolean): HashMap<T, Boolean> {
	list.forEach { this[it] = this[it] ?: default }
	return this
}

/**
 * Takes current list and @return a new one with the same elements but different order.
 * Take elements from given @param index to end, and then adds too the elements from the start to @param index.
 * List A,B,C,D,E,F changed start to 1 would be B,C,D,E,F,A
 */
fun <T> List<T>.changeStartToIndex(index: Int): List<T> {
	val result = mutableListOf<T>()
	result.addAll(this.subList(index, this.size))
	result.addAll(this.subList(0, index))
	return result
}

inline fun <T> Iterable<T>.tapIndexed(function: (index: Int, T) -> Unit): Iterable<T> = this.also { forEachIndexed(function) }
inline fun <T> Iterable<T>.tap(function: (T) -> Unit): Iterable<T> = this.also { forEach(function) }