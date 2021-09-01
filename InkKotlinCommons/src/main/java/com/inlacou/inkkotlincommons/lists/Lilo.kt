package com.inlacou.inkkotlincommons.lists

open class Lilo<T>(items: MutableList<T> = mutableListOf(), max: Int? = null): BaseCustomList<T>(items = items, max = max) {
	
	override fun push(element: T): Boolean {
		//Last input
		this.items.add(element)
		max?.let {
			while(size>it){ discard() }
		}
		return true
	}
	
	override fun pop(): T {
		//Last output
		return this.items.removeAt(this.items.lastIndex)
	}
	
	override fun discard() {
		this.items.removeAt(0)
	}
}