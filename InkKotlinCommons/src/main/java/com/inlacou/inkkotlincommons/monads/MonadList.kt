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

fun doubler(li: List<Int>) = MonadList(li.map { 2 * it } )
fun letters(li: List<Int>) = MonadList(li.map { "${('@' + it)}".repeat(it) } )

fun main(args: Array<String>) {
    val iv = MonadList(listOf(2, 3, 4))
    val fv = iv.bind(::doubler).bind(::letters)
    println(fv.value)
}