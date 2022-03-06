package com.inlacou.commons.monads

import com.inlacou.inkkotlincommons.monads.MonadList

fun doubler(li: List<Int>) = MonadList(li.map { 2 * it } )
fun letters(li: List<Int>) = MonadList(li.map { "${('@' + it)}".repeat(it) } )

fun main(args: Array<String>) {
    val iv = MonadList(listOf(2, 3, 4))
    val fv = iv.bind(::doubler).bind(::letters)
    println(fv.value)
}