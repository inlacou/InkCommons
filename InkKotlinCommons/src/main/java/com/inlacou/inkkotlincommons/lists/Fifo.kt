package com.inlacou.inkkotlincommons.lists

open class Fifo<T>(items: MutableList<T> = mutableListOf(), max: Int? = null): BaseCustomList<T>(items = items, max = max) {
	
	override fun push(element: T): Boolean {
		//First input
		this.items.add(0, element)
		max?.let {
			while(size>it){ discard() }
		}
		return true
	}
	
	override fun pop(): T {
		//First output
		return this.items.removeAt(0)
	}
	
	override fun discard() {
		this.items.removeAt(items.size-1)
	}
}