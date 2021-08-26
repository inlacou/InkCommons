package com.inlacou.inkkotlinextensions.extensions

import com.inlacou.inkkotlinextensions.*
import org.junit.Assert
import org.junit.jupiter.api.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MapExtensionsUnitTests {
    
    @Test
    fun mergeList() {
        val map: HashMap<Int, Boolean> = hashMapOf()
        
        //All should be null, because we have no values on the map
        Assert.assertEquals(null, map[0])
        Assert.assertEquals(null, map[1])
        Assert.assertEquals(null, map[2])
        Assert.assertEquals(null, map[3])
        Assert.assertEquals(null, map[4])
        Assert.assertEquals(null, map[5])
        Assert.assertEquals(null, map[6])
        Assert.assertEquals(null, map[7])
        
        //Add new items with default false
        val list1 = listOf(0, 1, 2, 3)
        map.merge(list1, false)
        Assert.assertEquals(false, map[0])
        Assert.assertEquals(false, map[1])
        Assert.assertEquals(false, map[2])
        Assert.assertEquals(false, map[3])
        
        map[0] = true
        Assert.assertEquals(true, map[0])
    
        //Add new items with default true
        val list2 = listOf(4, 5, 6, 7)
        map.merge(list2, true)
        Assert.assertEquals(true, map[4])
        Assert.assertEquals(true, map[5])
        Assert.assertEquals(true, map[6])
        Assert.assertEquals(true, map[7])
    
        //Try to add already there items with default true false, it should ignore and still be true
        val list3 = listOf(4, 5, 6, 7)
        map.merge(list3, false)
        Assert.assertEquals(true, map[4])
        Assert.assertEquals(true, map[5])
        Assert.assertEquals(true, map[6])
        Assert.assertEquals(true, map[7])
    }
    
}
