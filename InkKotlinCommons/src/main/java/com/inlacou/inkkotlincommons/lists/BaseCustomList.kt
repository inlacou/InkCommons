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
	
	abstract fun push(element: T)
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