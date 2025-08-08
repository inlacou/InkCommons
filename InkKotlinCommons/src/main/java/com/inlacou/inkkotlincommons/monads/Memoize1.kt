package com.inlacou.inkkotlincommons.monads

/**
 * Used to store or cache results of a function call so as to not calculate those values every time.
 *
 * https://jorgecastillo.dev/kotlin-purity-and-function-memoization
 * Alt: https://github.com/MarioAriasC/funKTionale
 */
class Memoize1<in T, out R>(val f: (T) -> R) : (T) -> R {
    private val values = mutableMapOf<T, R>()
    override fun invoke(x: T): R {
        return values.getOrPut(x) { f(x) }
    }
}

fun <T, R> ((T) -> R).memoize(): (T) -> R = Memoize1(this)
