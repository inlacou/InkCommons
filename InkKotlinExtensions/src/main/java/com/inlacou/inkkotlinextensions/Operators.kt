package com.inlacou.inkkotlinextensions

/**
 * Scope function that the specified function [block] with `this` value as its argument and returns its result.
 * Only called if the value of [bool] is true.
 * Unlike usual [let], restricted to return the same type as the type it's called on.
 */
internal inline fun <T> T.letIf(bool: Boolean, block: (T) -> T): T = if (bool) block.invoke(this) else this
