package com.inlacou.commons.monads

import com.inlacou.inkkotlincommons.monads.IWriter
import com.inlacou.inkkotlincommons.monads.Writer
import com.inlacou.inkkotlincommons.monads.append
import com.inlacou.inkkotlincommons.monads.bind
import kotlin.math.sqrt

data class MyClass(var name: String, var surname: String, var height: Double): IWriter {
    override var log: String = ""
    override val iWriterValue: Any get() = height
}

private fun root(d: MyClass) = d.apply { height = sqrt(height) }.append("Took square root")
private fun addOne(d: MyClass) = d.apply { height += 1.0 }.append("Added one")
private fun half(d: MyClass) = d.apply { height /= 2.0 }.append("Divided by two")

fun main(args: Array<String>) {
    val iv = MyClass("John", "Doe", 5.0).append("initial value")
    val fv = iv
        .bind { root(it) }
        .bind { addOne(it) }
        .bind { half(it) }
    println("The new height is ${fv.height}")
    println("\nThis was derived as follows:" +
            "\n${fv.log}")
    if(fv.height==1.618033988749895) println("result is OK")
}
