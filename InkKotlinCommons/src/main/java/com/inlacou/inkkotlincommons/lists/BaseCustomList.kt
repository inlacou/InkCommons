package com.inlacou.inkkotlincommons.lists

abstract class BaseCustomList<T>(
		val items: MutableList<T> = mutableListOf(),
		var max: Int? = null
){
	
	val size: Int get() = items.size
	override fun toString() = this.items.toString()
	fun contains(element: T?): Boolean = items.contains(element)
	fun clear() = items.clear()
	fun isEmpty(): Boolean = this.items.isEmpty()
	fun isNotEmpty(): Boolean = this.items.isNotEmpty()
	fun get(index: Int) = items[index]
	
	/**
	 * @return boolean indicating true if element was added, and false if not
	 */
	abstract fun push(element: T): Boolean
	abstract fun pop(): T
	protected abstract fun discard()
	
	/**
	 * Discards the oldest value to make room for a new value
	 */
	fun safePop(): T? {
		return if (this.isEmpty()){
			null
		} else {
			pop()
		}
	}
	
}

inline fun <T> fifoOf(): Fifo<T> = Fifo()
inline fun <T> fifoOf(vararg elements: T): Fifo<T> = Fifo((if (elements.isNotEmpty()) elements.asList() else emptyList()).toMutableList())
inline fun <T> lifoOf(): Lifo<T> = Lifo()
inline fun <T> lifoOf(vararg elements: T): Lifo<T> = Lifo((if (elements.isNotEmpty()) elements.asList() else emptyList()).toMutableList())
inline fun <T> liloOf(): Lilo<T> = Lilo()
inline fun <T> liloOf(vararg elements: T): Lilo<T> = Lilo((if (elements.isNotEmpty()) elements.asList() else emptyList()).toMutableList())
inline fun <T> filoOf(): Filo<T> = Filo()
inline fun <T> filoOf(vararg elements: T): Filo<T> = Filo((if (elements.isNotEmpty()) elements.asList() else emptyList()).toMutableList())
inline fun <T> noDupeMutableListOf(): NoDupeMutableList<T> = NoDupeMutableList()
inline fun <T> noDupeMutableListOf(vararg elements: T): NoDupeMutableList<T> = NoDupeMutableList((if (elements.isNotEmpty()) elements.asList() else emptyList()).toMutableList())