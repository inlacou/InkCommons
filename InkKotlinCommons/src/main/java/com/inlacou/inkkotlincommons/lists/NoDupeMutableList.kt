package com.inlacou.inkkotlincommons.lists

class NoDupeMutableList<T>(items: MutableList<T> = mutableListOf(), max: Int? = null): Lilo<T>(items = items, max = max) {
	
	override fun push(element: T): Boolean
		= if(contains(element)) false
	else super.push(element)
	
}