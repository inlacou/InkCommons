package com.inlacou.inkkotlinextensions

import java.lang.AssertionError
import java.lang.IllegalArgumentException
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
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

fun <T> Iterable<T>.containsBy(item: T, equals: (T, T) -> Boolean): Boolean = this.find { equals(item, it) }!=null

fun <T> Iterator<T>.toList(): List<T> {
	val mutableList = mutableListOf<T>()
	while (hasNext()) mutableList.add(next())
	return mutableList
}

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
	return try { get(i) }
	catch (ioobe: IndexOutOfBoundsException) { null }
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
fun <T> List<T>.safePreviousLooping(currentIndex: Int): T {
	return try {
		get(currentIndex-1)
	}catch (ioobe: IndexOutOfBoundsException){
		get(size-1)
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

fun <T> MutableList<T>.replaceFirst(item: T, replaceCondition: (T) -> Boolean): MutableList<T> {
	(0 until size).forEach { index ->
		val currentItem = get(index)
		if(replaceCondition(currentItem)) {
			remove(currentItem)
			add(index, item)
			return this
		}
	}
	return this
}

fun <T> MutableList<T>.replaceLast(item: T, replaceCondition: (T) -> Boolean): MutableList<T> {
	(0 until size).reversed().forEach { index ->
		val currentItem = get(index)
		if(replaceCondition(currentItem)) {
			remove(currentItem)
			add(index, item)
			return this
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
 * List A,B,C,D,E,F changed start to 1 would be B,C,D,E,F,A, starting now in B, the old 1 index item.
 */
fun <T> List<T>.changeStartToIndex(index: Int): List<T> {
	val result = mutableListOf<T>()
	result.addAll(this.subList(index, this.size))
	result.addAll(this.subList(0, index))
	return result
}

inline fun <T> Iterable<T>.tapIndexed(function: (index: Int, T) -> Unit): Iterable<T> = this.also { forEachIndexed(function) }
inline fun <T> Iterable<T>.tap(function: (T) -> Unit): Iterable<T> = this.also { forEach(function) }

fun <T> List<T>.swap(index: Int, index2: Int): List<T> {
	return if(index==index2) this
	else toMutableList().apply {
		val firstIndex = min(index, index2)
		val secondIndex = max(index, index2)
		val firstAux = removeAt(index)
		val secondAux = removeAt(index2-1 /*after the first removeAt, we have now one item less on the list*/)
		add(firstIndex, secondAux)
		add(secondIndex, firstAux)
	}
}

/**
 * -1 or less means A before B
 * +1 or more means A after  B
 *  0         means A equals B
 *
 * Idea: https://medium.com/dare-to-be-better/6-algorithms-every-developer-should-know-f78b609c7e7c
 *
 * Bubble Sort is the most basic sorting algorithm, and it works by repeatedly swapping adjacent elements if they are out of order.
 */
fun <T> List<T>.bubbleSort(compare: (first: T, second: T) -> Int): List<T> = bubbleSort(compare, 0)
private fun <T> List<T>.bubbleSort(compare: (first: T, second: T) -> Int, startIndex: Int = 0): List<T> {
	if(this.size<2) return this
	var currentIndex = startIndex-1 /*Starting at -1 because we will add 1 before using*/
	var currentComparation: Int
	do {
		currentIndex++
		currentComparation = compare.invoke(this[currentIndex], this[currentIndex+1])
	}while(currentComparation<+1 && currentIndex+2<this.size)
	return if(currentComparation>=1) {
		/*recursive*/ swap(currentIndex, currentIndex+1).bubbleSort(compare, startIndex = max(currentIndex-1, 0) /*For optimization purposes*/)
	} else /*recursive end*/ this
}

private fun myAssertEquals(item1: List<String>, i: Int, i1: Int, item2: List<String>) {
	println("assert: $item1 swapping ${item1[i]} and ${item1[i1]} is $item2")
	val swapped = item1.swap(i, i1)
	if (swapped != item2) throw AssertionError("$item1 swapping ${item1[i]} and ${item1[i1]} is NOT $item2, is $swapped")
}

fun main() {
	myAssertEquals(listOf("a", "h", "j", "z"), 1, 2, listOf("a", "j", "h", "z"))
	myAssertEquals(listOf("a", "h", "j", "z"), 0, 1, listOf("h", "a", "j", "z"))
	myAssertEquals(listOf("a", "h", "j", "z"), 0, 2, listOf("j", "h", "a", "z"))
	myAssertEquals(listOf("a", "h", "j", "z"), 0, 3, listOf("z", "h", "j", "a"))
	val start = listOf("a", "h", "j", "z", "b", "c", "0", "w", "i", "l", "j", "7", "j", "jh", "ja")
	val aux = start.bubbleSort { a, b ->
		when {
			a==b -> 0
			a.isBlank() -> -1
			b.isBlank() -> 1
			else -> {
				var index = 0
				var aux1: Char
				var aux2: Char
				do {
					aux1 = a[index]
					aux2 = b[index]
					index++
				}while (aux1==aux2 && index<a.length && index<b.length)

				val num1 = aux1.toSortableNumber()
				val num2 = aux2.toSortableNumber()
				when {
					num1==num2   ->  0
					num1<num2    -> -1
					num1>num2    -> +1
					else /*wtf*/ ->  0
				}
			}
		}
	}
	println("before: $start")
	println("after:  $aux")
}