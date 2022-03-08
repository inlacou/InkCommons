package com.inlacou.commons.monads

import com.inlacou.inkkotlincommons.monads.Writer
import kotlin.math.sqrt

private fun root(d: Double) = Writer(sqrt(d), "Took square root")
private fun addOne(d: Double) = Writer(d + 1.0, "Added one")
private fun half(d: Double) = Writer(d / 2.0, "Divided by two")

fun main(args: Array<String>) {
    val iv = Writer(5.0, "Initial value")
    val fv = iv
        .bind { root(it) }
        .bind { addOne(it) }
        .bind { half(it) }
    println("The Golden Ratio is ${fv.value}")
    println("\nThis was derived as follows:" +
            "\n${fv.log}")
}
