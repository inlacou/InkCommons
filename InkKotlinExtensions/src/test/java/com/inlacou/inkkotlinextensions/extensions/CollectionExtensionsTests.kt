package com.inlacou.inkkotlinextensions.extensions

import com.inlacou.inkkotlinextensions.combineWith
import com.inlacou.inkkotlinextensions.forEachIndexedReversed
import com.inlacou.inkkotlinextensions.mapInstance
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.ClassCastException

class CollectionExtensionsTests {
    @Test fun `combinatory AB with 12`() {
        assertEquals(
            listOf(
                Pair("A", 1),
                Pair("A", 2),
                Pair("B", 1),
                Pair("B", 2),
            ),
            listOf("A", "B").combineWith(listOf(1, 2))
        )
    }

    @Test fun `combinatory AB with 12 with TrueFalse`() {
        assertEquals(
            listOf(
                Triple("A", 1, true),
                Triple("A", 1, false),
                Triple("A", 2, true),
                Triple("A", 2, false),
                Triple("B", 1, true),
                Triple("B", 1, false),
                Triple("B", 2, true),
                Triple("B", 2, false),
            ),
            listOf("A", "B").combineWith(listOf(1, 2), listOf(true, false))
        )
    }

    @Test fun `combinatory ABC with 123`() {
        assertEquals(
            listOf(
                Pair("A", 1),
                Pair("A", 2),
                Pair("A", 3),
                Pair("B", 1),
                Pair("B", 2),
                Pair("B", 3),
                Pair("C", 1),
                Pair("C", 2),
                Pair("C", 3),
            ),
            listOf("A", "B", "C").combineWith(listOf(1, 2, 3))
        )
    }

    /**
     * Test for [forEachIndexedReversed]: basic intended usage
     */
    @Test fun `forEachIndexedReversed basic`() {
        // For reference
        var additionalIndex = 0
        listOf(0, 1, 2, 3).forEachIndexed { index, it -> assertEquals(index, it) }
        listOf(0, 1, 2, 3).forEachIndexed { index, it ->
            assertEquals(0, index - additionalIndex)
            additionalIndex++
        }
        // Actual test of forEachIndexedReversed
        additionalIndex = 0
        listOf(0, 1, 2, 3).forEachIndexedReversed { index, it -> assertEquals(index, it) }
        listOf(0, 1, 2, 3).forEachIndexedReversed { index, it ->
            assertEquals(3, index + additionalIndex)
            additionalIndex++
        }
    }

    /**
     * Edge case test for [forEachIndexedReversed]: empty list
     */
    @Test fun `forEachIndexedReversed on an empty list`() {
        listOf<String>().forEachIndexedReversed { index, s -> throw Exception("wtf") }
    }

    abstract class Parent
    data class Children(val name: String) : Parent()
    data class Cat(val name: String) : Parent()

    @Test fun `map instance children`() {
        val input = listOf<Parent>(Children("Lucian"), Children("Luis"))
        input.filterIsInstance<Children>().let {
            assertEquals(input.size, it.size)
            it.forEachIndexed { index, children -> assertEquals(input[index], children) }
        }
        input.mapInstance<Children>().let {
            assertEquals(input.size, it.size)
            it.forEachIndexed { index, children -> assertEquals(input[index], children) }
        }
    }
    @Test fun `map instance children and other`() {
        val input = listOf(Children("Lucian"), Children("Luis"), Cat("Breton"))
        input.filterIsInstance<Children>().let {
            assertEquals(input.size - 1 /* we filter the [Cat] out */, it.size)
            it.forEachIndexed { index, children -> assertEquals(input[index], children) }
        }
        assertThrows<ClassCastException> { input.mapInstance<Children>() }
    }
}
