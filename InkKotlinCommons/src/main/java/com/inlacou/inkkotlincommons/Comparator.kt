package com.inlacou.inkkotlincommons

interface Comparator<T> {
	operator fun invoke(one: T, other: T): Boolean
}