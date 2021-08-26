package com.inlacou.inkkotlincommons.lists

class Lilo<T>: BaseCustomList<T>() {

	override fun push(element: T) {
		//Last input
		this.items.add(element)
		max?.let {
			while(size>it){ discard() }
		}
	}

	override fun pop(): T {
		//Last output
		return this.items.removeAt(this.items.lastIndex)
	}
	
	override fun discard() {
		this.items.removeAt(0)
	}
	
}