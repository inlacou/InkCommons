package com.inlacou.inkkotlincommons.lists

open class Lifo<T>(items: MutableList<T> = mutableListOf(), max: Int? = null): BaseCustomList<T>(items = items, max = max) {
	
	override fun push(element: T): Boolean {
		//Last input
		this.items.add(element)
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
		this.items.removeAt(0)
	}
}