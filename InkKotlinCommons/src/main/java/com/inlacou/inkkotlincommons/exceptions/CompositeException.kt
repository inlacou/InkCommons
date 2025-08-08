package com.inlacou.inkkotlincommons.exceptions

class CompositeException(val s: String?, private val other: Exception): Exception() {
    override fun toString(): String {
        return "${this::class.simpleName}: $s | other: $other"
    }
}