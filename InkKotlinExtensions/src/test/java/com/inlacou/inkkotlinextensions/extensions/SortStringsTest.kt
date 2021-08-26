package com.inlacou.inkkotlinextensions.extensions

import com.inlacou.inkkotlinextensions.*
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SortStringsTest {
    
    @Test
    fun basic(){
        val list = mutableListOf<String>()
        list.add("Jon")
        list.add("Ana")
        list.add("Juan")
        list.add("Pedro")
        list.add("Pancho")
        list.add("Aitor")
        list.add("Hodei")
        list.add("Enara")
        list.add("Unai")
        list.add("Zac")
        list.add("Andrea")
        list.add("Adrian")
        list.add("Aaron")
        list.add("Eneko")
        list.add("Iker")
        list.add("Leire")
        list.sortBy { it }
        assertEquals("Aaron", list[0])
        assertEquals("Zac", list[list.size-1])
        var lastLetter: Char? = null
        val iterator = list.listIterator()
        iterator.forEach {
            val currentFirstLetter = it.trim().first()
            if(lastLetter==null){
                lastLetter = currentFirstLetter
                iterator.addBefore(currentFirstLetter.toString())
            }else if(lastLetter!=currentFirstLetter){
                lastLetter = currentFirstLetter
                iterator.addBefore(currentFirstLetter.toString())
            }
        }
        assertEquals("A", list[0])
        assertEquals("Z", list[list.size-2])
        assertEquals(true, list.contains("U"))
        assertEquals(true, list.contains("E"))
        assertEquals(true, list.contains("H"))
        assertEquals(true, list.contains("P"))
        assertEquals(false, list.contains("X"))
        assertEquals(false, list.contains("B"))
    }
    
    @Test
    fun medium(){
        val list = mutableListOf<String>()
        list.add("ççç1")
        list.add("??+ç2")
        list.add("ç3")
        list.add("?4")
        list.add("32645")
        list.add("89789")
        list.add("527679789")
        list.add("?????5")
        list.add("çç6")
        list.add("ç7")
        list.add("???8")
        list.add("Jon")
        list.add("Ana")
        list.add("Juan")
        list.add("Pedro")
        list.add("Pancho")
        list.add("Aitor")
        list.add("Hodei")
        list.add("Enara")
        list.add("Unai")
        list.add("Zac")
        list.add("Andrea")
        list.add("Adrian")
        list.add("Aaron")
        list.add("Eneko")
        list.add("Álvaro")
        list.add("Iker")
        list.add("Leire")
        list.add("çççç")
        list.sortBy { it.toSortableNumber() }
        var lastLetter: Char? = null
        val iterator = list.listIterator()
        iterator.forEach {
            val currentFirstLetter = it.trim().first()
            if((lastLetter==null || lastLetter!=currentFirstLetter)){
                if(currentFirstLetter.isValidLetter()) {
                    lastLetter = currentFirstLetter
                    iterator.addBefore(currentFirstLetter.toString())
                }else if(lastLetter==null){
                    lastLetter = '#'
                    iterator.addBefore('#'.toString())
                }
            }
        }
        assertEquals("#", list[0])
        assertEquals("Z", list[list.size-2])
        assertEquals(true, list.indexOf("ç3")<list.indexOf("ç7"))
        assertEquals(true, list.contains("U"))
        assertEquals(true, list.contains("E"))
        assertEquals(true, list.contains("H"))
        assertEquals(true, list.contains("P"))
        assertEquals(false, list.contains("X"))
        assertEquals(false, list.contains("B"))
    }
    
}
