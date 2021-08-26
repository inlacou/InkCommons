package com.inlacou.inkkotlincommons.lists

class Lifo<T>: BaseCustomList<T>() {

	override fun push(element: T) {
		//Last input
		this.items.add(element)
		max?.let {
			while(size>it){ discard() }
		}
	}

	override fun pop(): T {
		//First output
		return this.items.removeAt(0)
	}
	
	override fun discard() {
		this.items.removeAt(0)
	}
	
}