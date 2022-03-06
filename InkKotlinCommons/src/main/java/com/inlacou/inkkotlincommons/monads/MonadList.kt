package com.inlacou.inkkotlincommons.monads

/**
 * It is just a normal list
 */
class MonadList<T>(val value: List<T>) {
    /**
     * Akin to the usual list map function.
     */
    fun <U : Any> bind(f: (List<T>) -> MonadList<U>) = f(this.value)
}