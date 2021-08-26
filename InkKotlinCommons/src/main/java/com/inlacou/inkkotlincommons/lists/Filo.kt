package com.inlacou.inkkotlincommons.lists

class Filo<T>: BaseCustomList<T>() {

	override fun push(element: T) {
		//First input
		this.items.add(0, element)
		max?.let {
			while(size>it){ discard() }
		}
	}

	override fun pop(): T {
		//Last output
		return this.items.removeAt(this.items.lastIndex)
	}
	
	override fun discard() {
		this.items.removeAt(this.items.lastIndex)
	}
	
}