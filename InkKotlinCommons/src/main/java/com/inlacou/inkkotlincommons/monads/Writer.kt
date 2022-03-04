package com.inlacou.inkkotlincommons.monads

import kotlin.math.sqrt
 
class Writer<T>(val value: T, s: String) {
    var log = "  ${s.padEnd(17)}: $value\n"
        private set
    fun bind(f: (T) -> Writer<T>): Writer<T> = f(this.value).apply { log = this@Writer.log + this.log }
}

fun root(d: Double) = Writer(sqrt(d), "Took square root")
fun addOne(d: Double) = Writer(d + 1.0, "Added one")
fun half(d: Double) = Writer(d / 2.0, "Divided by two")

fun main(args: Array<String>) {
    val iv = Writer(5.0, "Initial value")
    val fv = iv.bind { root(it) }.bind { addOne(it) }.bind { half(it) }
    println("The Golden Ratio is ${fv.value}")
    println("\nThis was derived as follows:-\n${fv.log}")
}