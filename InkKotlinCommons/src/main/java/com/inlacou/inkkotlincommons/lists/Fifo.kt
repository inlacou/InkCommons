package com.inlacou.inkkotlincommons.lists

class Fifo<T>: BaseCustomList<T>() {
	
	override fun push(element: T) {
		//First input
		this.items.add(0, element)
		max?.let {
			while(size>it){ discard() }
		}
	}

	override fun pop(): T {
		//First output
		return this.items.removeAt(0)
	}
	
	override fun discard() {
		this.items.removeAt(items.size-1)
	}
	
}