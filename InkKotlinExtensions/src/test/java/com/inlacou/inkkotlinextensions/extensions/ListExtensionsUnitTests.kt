package com.inlacou.inkkotlinextensions.extensions

import com.inlacou.inkkotlinextensions.*
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import java.lang.IndexOutOfBoundsException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ListExtensionsUnitTests {
    
    data class DumbItem(val value: Int, val otherStringValue: String)
    
    private val itemsBase = listOf(
            DumbItem(1, "A"), DumbItem(2, "B"), DumbItem(3, "C"), DumbItem(4, "D")
            , DumbItem(5, "E"), DumbItem(6, "F"), DumbItem(7, "G"), DumbItem(8, "H")
    )
    
    private val itemsToMerge = listOf(
            DumbItem(1, "H"), DumbItem(2, "I"), DumbItem(3, "J"), DumbItem(4, "K")
    )
    
    private val itemsToMinus = listOf(
            DumbItem(1, "A"), DumbItem(2, "A"), DumbItem(3, "H"), DumbItem(4, "K")
    )
    
    private val itemsToIntersect = listOf(
            DumbItem(1, "A"), DumbItem(2, "A"), DumbItem(3, "H"), DumbItem(4, "K")
    )

    private val array = arrayOf(
            DumbItem(1, "A"), DumbItem(2, "A"), DumbItem(3, "H"), DumbItem(4, "K")
    )

    @Test
    fun mergeBy() {
        //itemsToMerge has 4 items, but only 3 will be added
        val merged = itemsBase.mergeBy(itemsToMerge) { first, second -> first.otherStringValue==second.otherStringValue }
        Assert.assertEquals(merged.toString(), 11, merged.size)
    }
    
    @Test
    fun minusBy() {
        val merged = itemsBase.minusBy(itemsToMinus) { first, second -> first.otherStringValue==second.otherStringValue }
        Assert.assertEquals(merged.toString(), 6, merged.size)
    }
    
    @Test
    fun intersectBy() {
        val merged = itemsBase.intersectBy(itemsToIntersect) { first, second -> first.otherStringValue==second.otherStringValue }
        Assert.assertEquals(merged.toString(), 2, merged.size)
    }

    @Test fun `iterator to list`() = Assert.assertTrue(array.iterator().toList() is List<DumbItem>)
    @Test fun `stream to list`() {}

    @Test
    fun concat() {
        val list = mutableListOf<String>()
        list.add("Jon")
        list.add("Ana")
        val list2 = mutableListOf<String>()
        list2.add("Juan")
    
        Assert.assertEquals("Jon", list[0])
        Assert.assertEquals("Ana", list[1])
        
        list.concat(list2)
        
        Assert.assertEquals("Juan", list[2])
        
        list.concat(list)
    
        Assert.assertEquals("Jon", list[3])
        Assert.assertEquals("Ana", list[4])
    }

    @Test fun addBefore() {}

    @Test fun toHashMap() {}

    @Test
    fun safeGet() {
        val list = mutableListOf<String>()
        list.add("Jon")
        list.add("Ana")
        Assert.assertNull(list.safeGet(2))
        Assert.assertNull(list.safeGet(3))
        Assert.assertNull(list.safeGet(4))
    }

    @Test
    fun `get nearest selectable`() {
        var items = listOf(
                SelectableItem(0, true)
                , SelectableItem(1, true)
                , SelectableItem(2, true)
                , SelectableItem(3, false)
                , SelectableItem(4, false)
                , SelectableItem(5, true)
        )
        Assert.assertEquals(2, items.getNearestSelectable(roughIndex = 3F, selectable = { it.selectable }).position)
        items = listOf(
                SelectableItem(0, true)
                , SelectableItem(1, false)
                , SelectableItem(2, false)
                , SelectableItem(3, false)
                , SelectableItem(4, false)
                , SelectableItem(5, false)
        )
        Assert.assertEquals(0, items.getNearestSelectable(roughIndex = 3F, selectable = { it.selectable }).position)
        items = listOf(
                SelectableItem(0, false)
                , SelectableItem(1, false)
                , SelectableItem(2, false)
                , SelectableItem(3, false)
                , SelectableItem(4, false)
                , SelectableItem(5, true)
        )
        Assert.assertEquals(5, items.getNearestSelectable(roughIndex = 3F, selectable = { it.selectable }).position)
        items = listOf(
                SelectableItem(0, false)
                , SelectableItem(1, false)
                , SelectableItem(2, false)
                , SelectableItem(3, true)
                , SelectableItem(4, false)
                , SelectableItem(5, false)
        )
        Assert.assertEquals(3, items.getNearestSelectable(roughIndex = 3F, selectable = { it.selectable }).position)
        items = listOf(
                SelectableItem(0, true)
                , SelectableItem(1, true)
                , SelectableItem(2, true)
                , SelectableItem(3, true)
                , SelectableItem(4, true)
                , SelectableItem(5, true)
        )
        Assert.assertEquals(3, items.getNearestSelectable(roughIndex = 3F, selectable = { it.selectable }).position)
        Assert.assertEquals(3, items.getNearestSelectable(roughIndex = 3.1F, selectable = { it.selectable }).position)
        Assert.assertEquals(3, items.getNearestSelectable(roughIndex = 3.2F, selectable = { it.selectable }).position)
        Assert.assertEquals(3, items.getNearestSelectable(roughIndex = 3.3F, selectable = { it.selectable }).position)
        Assert.assertEquals(3, items.getNearestSelectable(roughIndex = 3.4F, selectable = { it.selectable }).position)
        //Rounds up on decimals
        Assert.assertEquals(4, items.getNearestSelectable(roughIndex = 3.5F, selectable = { it.selectable }).position)
        items = listOf(
                SelectableItem(0, true)
                , SelectableItem(1, true)
                , SelectableItem(2, true)
                , SelectableItem(3, false)
                , SelectableItem(4, true)
                , SelectableItem(5, true)
        )
        //and on same distance higher has priority
        Assert.assertEquals(4, items.getNearestSelectable(roughIndex = 3F, selectable = { it.selectable }).position)
    }

    @Test
    fun getItemByPercentage() {
        val list = mutableListOf<Int>()
        list.add(0)
        list.add(1)
        list.add(2)
        list.add(3)
        list.add(4)
        list.add(5)
        list.add(6)
        list.add(7)
        list.add(8)
        list.add(9)
        Assert.assertEquals(0, list.getItemByPercentage(0f))
        Assert.assertEquals(9, list.getItemByPercentage(1f))
        Assert.assertEquals(2, list.getItemByPercentage(0.25f))
        Assert.assertEquals(5, list.getItemByPercentage(0.5f))
        Assert.assertEquals(7, list.getItemByPercentage(0.75f))
        list.add(10)
        list.add(11)
        list.add(12)
        list.add(13)
        list.add(14)
        list.add(15)
        list.add(16)
        list.add(17)
        list.add(18)
        list.add(19)
        Assert.assertEquals(0, list.getItemByPercentage(0f))
        Assert.assertEquals(19, list.getItemByPercentage(1f))
        Assert.assertEquals(5, list.getItemByPercentage(0.25f))
        Assert.assertEquals(10, list.getItemByPercentage(0.5f))
        Assert.assertEquals(15, list.getItemByPercentage(0.75f))
    }

    @Test fun `removeLast 5 on 12345 is 12345`() = Assert.assertEquals(listOf(1,2,3,4), listOf(1,2,3,4,5).removeLastItem(5))
    @Test fun `removeLast 4 on 12345 is 12345`() = Assert.assertEquals(listOf(1,2,3,5), listOf(1,2,3,4,5).removeLastItem(4))
    @Test fun `removeLast 3 on 12345 is 12345`() = Assert.assertEquals(listOf(1,2,4,5), listOf(1,2,3,4,5).removeLastItem(3))
    @Test fun `removeLast 5 on 123455 is 12345`() = Assert.assertEquals(listOf(1,2,3,4,5), listOf(1,2,3,4,5,5).removeLastItem(5))
    @Test fun `removeLast 4 on 123445 is 12345`() = Assert.assertEquals(listOf(1,2,3,4,5), listOf(1,2,3,4,4,5).removeLastItem(4))
    @Test fun `removeLast 3 on 132345 is 13245`() = Assert.assertEquals(listOf(1,3,2,4,5), listOf(1,3,2,3,4,5).removeLastItem(3))

    @Test fun `sublistAfter 4 on 12345 is 5`() = Assert.assertEquals(listOf(5), listOf(1,2,3,4,5).sublistAfter(4))
    @Test fun `sublistAfter 3 on 12345 is 45`() = Assert.assertEquals(listOf(4,5), listOf(1,2,3,4,5).sublistAfter(3))
    @Test fun `sublistAfter 6 on 12345 is 12345`() = Assert.assertEquals(listOf(1,2,3,4,5), listOf(1,2,3,4,5).sublistAfter(6))

    @Test fun `sublistFrom 4 on 12345 is 5`() = Assert.assertEquals(listOf(4,5), listOf(1,2,3,4,5).sublistFrom(4))
    @Test fun `sublistFrom 3 on 12345 is 45`() = Assert.assertEquals(listOf(3,4,5), listOf(1,2,3,4,5).sublistFrom(3))
    @Test fun `sublistFrom 6 on 12345 is 12345`() = Assert.assertEquals(listOf(1,2,3,4,5), listOf(1,2,3,4,5).sublistFrom(6))

    @Test fun `safeNextLooping 1 in 12345 is 3`() = Assert.assertEquals(3, listOf(1,2,3,4,5).safeNextLooping(1))
    @Test fun `safeNextLooping 3 in 12345 is 4`() = Assert.assertEquals(4, listOf(1,2,3,4,5).safeNextLooping(2))
    @Test fun `safeNextLooping 4 in 12345 is 1`() = Assert.assertEquals(1, listOf(1,2,3,4,5).safeNextLooping(4))
    @Test fun `safeNextLooping 5 in 12345 is 1`() = Assert.assertEquals(1, listOf(1,2,3,4,5).safeNextLooping(5))
    @Test fun `safeNextLooping 50 in 12345 is 1`() = Assert.assertEquals(1, listOf(1,2,3,4,5).safeNextLooping(50))

    @Test fun `safePreviousLooping 1 in 12345 is 5`() = Assert.assertEquals(5, listOf(1,2,3,4,5).safePreviousLooping(0))
    @Test fun `safePreviousLooping 5 in 12345 is 4`() = Assert.assertEquals(4, listOf(1,2,3,4,5).safePreviousLooping(4))
    @Test fun `safePreviousLooping 2 in 12345 is 1`() = Assert.assertEquals(1, listOf(1,2,3,4,5).safePreviousLooping(1))
    @Test fun `safePreviousLooping 0 in 12345 is null`() = Assert.assertEquals(5, listOf(1,2,3,4,5).safePreviousLooping(0))
    @Test fun `safePreviousLooping 20 in 12345 is null`() = Assert.assertEquals(5, listOf(1,2,3,4,5).safePreviousLooping(20))

    @Test fun `getFastestPathFromTwoIndexInCircularList`() {}

    @Test fun `safeRemoveAt 20 on 12345 is 12345`() = Assert.assertEquals(null, mutableListOf(1,2,3,4,5).safeRemoveAt(20))
    @Test fun `safeRemoveAt 2 on 12345 is 1245`() = Assert.assertEquals(3, mutableListOf(1,2,3,4,5).safeRemoveAt(2))
    @Test fun `safeRemoveAt 0 on 12345 is 2345`() = Assert.assertEquals(1, mutableListOf(1,2,3,4,5).safeRemoveAt(0))
    @Test fun `safeRemoveAt 4 on 12345 is 1234`() = Assert.assertEquals(5, mutableListOf(1,2,3,4,5).safeRemoveAt(4))

    @Test fun `safeAddBeforeLast 6 to 12345 is 123456`() = Assert.assertEquals(mutableListOf(1,2,3,4,6,5), mutableListOf(1,2,3,4,5).safeAddBeforeLast(6))
    @Test fun `safeAddBeforeLast 6 to empty is 6`() = Assert.assertEquals(mutableListOf(6), mutableListOf<Int>().safeAddBeforeLast(6))

    @Test fun `addBeforeLast 6 to 12345 is 123465`() = Assert.assertEquals(mutableListOf(1,2,3,4,6,5), mutableListOf(1,2,3,4,5).addBeforeLast(6))
    @Test fun `addBeforeLast 6 to empty is 6`() {
        try{
            mutableListOf<Int>().addBeforeLast(6)
            Assert.fail("Should throw IndexOutOfBoundsException")
        }catch (ioobe: IndexOutOfBoundsException){}
    }

    @Test fun `replaceAll pairs with 1 in 12345 is 11315`() = Assert.assertEquals(mutableListOf(1,1,3,1,5), mutableListOf(1,2,3,4,5).replaceAll(1) { index, item ->
        item%2==0
    })

    @Test fun `replaceFirst pairs with 1 in 12345 is 11345`() = Assert.assertEquals(mutableListOf(1,1,3,4,5), mutableListOf(1,2,3,4,5).replaceFirst(1) { item ->
        item%2==0
    })

    @Test fun `replaceLast pairs with 1 in 12345 is 12315`() = Assert.assertEquals(mutableListOf(1,2,3,1,5), mutableListOf(1,2,3,4,5).replaceLast(1) { item ->
        item%2==0
    })

    @Test fun `replaceAllOrAddAtIfNotFound 12345 replace 4 with 8 is 12385`() = Assert.assertEquals(mutableListOf(1,2,3,8,5), mutableListOf(1,2,3,4,5).replaceAllOrAddAtIfNotFound(8, -1, { it==4 }))
    @Test fun `replaceAllOrAddAtIfNotFound 12345 replace 6 with 8 is 123458`() = Assert.assertEquals(mutableListOf(1,2,3,4,5,8), mutableListOf(1,2,3,4,5).replaceAllOrAddAtIfNotFound(8, -1, { it==6 }))
    @Test fun `replaceAllOrAddAtIfNotFound 12345 replace 6 with 8 and add at start is 812345`() = Assert.assertEquals(mutableListOf(8,1,2,3,4,5), mutableListOf(1,2,3,4,5).replaceAllOrAddAtIfNotFound(8, 0, { it==6 }))

    @Test fun `avoid 0 on 12345 is 12345`() = Assert.assertEquals(mutableListOf(1,2,3,4,5), mutableListOf(1,2,3,4,5).avoid(0))
    @Test fun `avoid 1 on 12345 is 2345`() = Assert.assertEquals(mutableListOf(2,3,4,5), mutableListOf(1,2,3,4,5).avoid(1))
    @Test fun `avoid 3 on 12345 is 45`() = Assert.assertEquals(mutableListOf(4,5), mutableListOf(1,2,3,4,5).avoid(3))
    @Test fun `avoid 30 on 12345 is error`() {
        try {
            Assert.assertEquals(mutableListOf(4,5), mutableListOf(1,2,3,4,5).avoid(30))
            Assert.fail("Should give IllegalArgumentException")
        }catch (iae: IllegalArgumentException) {}
    }

    @Test fun `avoidOrEmpty 0 on 12345 is 12345`() = Assert.assertEquals(mutableListOf(1,2,3,4,5), mutableListOf(1,2,3,4,5).avoidOrEmpty(0))
    @Test fun `avoidOrEmpty 1 on 12345 is 2345`() = Assert.assertEquals(mutableListOf(2,3,4,5), mutableListOf(1,2,3,4,5).avoidOrEmpty(1))
    @Test fun `avoidOrEmpty 3 on 12345 is 45`() = Assert.assertEquals(mutableListOf(4,5), mutableListOf(1,2,3,4,5).avoidOrEmpty(3))
    @Test fun `avoidOrEmpty 30 on 12345 is empty`() = Assert.assertEquals(mutableListOf<Int>(), mutableListOf(1,2,3,4,5).avoidOrEmpty(30))

    @Test fun `maxIndex on 12345 is 4`() = Assert.assertEquals(4, mutableListOf(1,2,3,4,5).maxIndex)
    @Test fun `maxIndex on 1 is 0`() = Assert.assertEquals(0, mutableListOf(1).maxIndex)

    @Test fun `reversedForEach`() {
        var counter = 0
        listOf(1,2,3,4,5).reversedForEach {
            when(counter) {
                0 -> Assert.assertEquals(5,it)
                1 -> Assert.assertEquals(4,it)
                2 -> Assert.assertEquals(3,it)
                3 -> Assert.assertEquals(2,it)
                else -> Assert.assertEquals(1,it)
            }
            counter++
        }
    }

    @Test fun `concurrentRemove`() {
        //TODO
    }

    @Test fun `toPairs`() { listOf(1,2,3,4,5).toPairs(true).tapIndexed { index, pair -> Assert.assertEquals(Pair(index+1, true), pair) } }

    @Test fun `groupConsecutiveBy`() {
        listOf(1,2,3,4,5)
    }

    @Test fun `merge 12345 with 6789 is 123456789`() = Assert.assertEquals(listOf(1,2,3,4,5,6,7,8,9).toTypedArray(), listOf(1,2,3,4,5,).toPairs(true).toHashMap().merge(listOf(6,7,8,9), false).keys.toTypedArray())
    @Test fun `merge 12345 with 4567 is 1234567`() = Assert.assertEquals(listOf(1,2,3,4,5,6,7).toTypedArray(), listOf(1,2,3,4,5,).toPairs(true).toHashMap().merge(listOf(4,5,6,7), false).keys.toTypedArray())

    @Test fun `tap`() { listOf(1,2,3,4,5,).tap { assert(it==1||it==2||it==3||it==4||it==5) } }

    @Test fun `tapIndexed`() { listOf(1,2,3,4,5,).tapIndexed { index, item -> Assert.assertEquals(index+1, item) } }

    @Test fun `swap 1 with 1 in 1234 gives 1234`() = assert(listOf(1,2,3,4,).swap(0, 0)==listOf(1,2,3,4))
    @Test fun `swap 1 with 2 in 1234 gives 2134`() = assert(listOf(1,2,3,4,).swap(0,1)==listOf(2,1,3,4))
    @Test fun `swap 1 with 3 in 1234 gives 3214`() = assert(listOf(1,2,3,4,).swap(0,2)==listOf(3,2,1,4))
    @Test fun `swap 1 with 4 in 1234 gives 4231`() = assert(listOf(1,2,3,4,).swap(0,3)==listOf(4,2,3,1))
    @Test fun `swap 1 with 5 in 1234 gives error`() { assertThrows<IndexOutOfBoundsException> { listOf(1,2,3,4,).swap(0,10)==listOf(2,1,3,4) } }

    val compareStrings = { a: String, b: String ->
        when {
            a==b -> 0
            a.isBlank() -> -1
            b.isBlank() -> 1
            else -> {
                var index = 0
                var aux1: Char
                var aux2: Char
                do {
                    aux1 = a[index]
                    aux2 = b[index]
                    index++
                }while (aux1==aux2 && index<a.length && index<b.length)

                val num1 = aux1.toSortableNumber()
                val num2 = aux2.toSortableNumber()
                when {
                    num1==num2   ->  0
                    num1<num2    -> -1
                    num1>num2    -> +1
                    else /*wtf*/ ->  0
                }
            }
        }
    }

    @Test fun `(a,h,j,z,b,0,c,w,i,l,j,j,jh,ja) sorted is (0,a,b,c,h,i,j,j,j,ja,jh,l,w,z)`() = assert(listOf("a", "h", "j", "z", "b", "0", "c", "w", "i", "l", "j", "j", "jh", "ja").bubbleSort(compareStrings)==listOf("0", "a", "b", "c", "h", "i", "j", "j", "j", "ja", "jh", "l", "w", "z"))

    data class SelectableItem(val position: Int, val selectable: Boolean)
}
