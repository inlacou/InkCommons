package com.inlacou.commons

import com.inlacou.inkkotlincommons.monads.IWriter
import com.inlacou.inkkotlincommons.monads.append
import com.inlacou.inkkotlincommons.monads.bind
import org.junit.Test
import kotlin.math.sqrt

class WritterInterfaceUnitTests {

    private data class PersonWithHeight(var name: String, var surname: String, var height: Double):
        IWriter {
        override var log: String = ""
        override val iWriterValue: Any get() = height
    }

    private fun PersonWithHeight.root() = apply { height = sqrt(height) }.append("Took square root")
    private fun PersonWithHeight.addOne() = apply { height += 1.0 }.append("Added one")
    private fun PersonWithHeight.half() = apply { height /= 2.0 }.append("Divided by two")

    @Test fun aux() {
        val person = PersonWithHeight("John", "Doe", 5.0).append("initial value")
            .bind { it.root() }
            .bind { it.addOne() }
            .bind { it.half() }
        println("The new height is ${person.height}")
        println("\nThis was derived as follows:" +
                "\n\n${person.log}")
        if(person.height==1.618033988749895) println("result is OK")
    }
}