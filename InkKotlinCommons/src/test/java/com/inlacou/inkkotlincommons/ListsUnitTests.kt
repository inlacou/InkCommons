package com.inlacou.inkkotlincommons

import com.inlacou.inkkotlincommons.lists.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ListsUnitTests {
	
	@Test
	fun base() {
		val list = Fifo<String>()
		assertTrue(list.isEmpty())
		list.push("uno") //uno
		list.push("dos") //dos, uno
		list.push("tres") //tres, dos, uno
		list.push("cuatro") //cuatro, tres, dos, uno
		assertTrue(list.toString(), list.contains("uno"))
		assertTrue(list.toString(), list.contains("dos"))
		assertTrue(list.toString(), list.contains("tres"))
		assertTrue(list.toString(), list.contains("cuatro"))
		assertEquals(list.toString(), "[cuatro, tres, dos, uno]", list.toString())
		list.max = 4
		assertEquals(4, list.size)
		list.push("uno") //uno, cuatro, tres, dos //oldest value (first "uno") is discarded
		assertEquals(list.toString(), "uno", list.get(0))
		assertTrue(list.toString(), list.contains("uno"))
		assertTrue(list.toString(), list.contains("dos"))
		assertTrue(list.toString(), list.contains("tres"))
		assertTrue(list.toString(), list.contains("cuatro"))
		assertEquals(list.toString(), "[uno, cuatro, tres, dos]", list.toString())
		assertEquals(4, list.size)
		assertTrue(list.toString(), list.isNotEmpty())
		list.clear()
		assertTrue(list.toString(), list.isEmpty())
		
		list.push("uno") //uno
		list.push("dos") //dos, uno
		list.push("tres") //tres, dos, uno
		list.push("cuatro") //cuatro, tres, dos, uno
		assertTrue(list.safePop()!=null)
		assertEquals(3, list.size)
		assertTrue(list.safePop()!=null)
		assertEquals(2, list.size)
		assertTrue(list.safePop()!=null)
		assertEquals(1, list.size)
		assertTrue(list.safePop()!=null)
		assertEquals(0, list.size)
		assertTrue(list.safePop()==null)
		assertEquals(0, list.size)
		assertTrue(list.safePop()==null)
		assertEquals(0, list.size)
	}
	
	@Test
	fun fifo() {
		val list = Fifo<String>()
		list.push("uno") //uno
		list.push("dos") //dos, uno
		list.push("tres") //tres, dos, uno
		list.push("cuatro") //cuatro, tres, dos, uno
		assertEquals("cuatro", list.pop()) //tres, dos, uno
		assertEquals("tres", list.pop()) //dos, uno
		assertEquals("dos", list.pop()) //uno
		assertEquals("uno", list.pop()) //-
	}

	@Test
	fun fifo_max_size() {
		val list = fifoOf("uno") //uno
		list.max = 3
		list.push("dos") //dos, uno
		list.push("tres") //tres, dos, uno
		list.push("cuatro") //cuatro, tres, dos
		assertEquals("cuatro", list.pop()) //tres, dos
		assertEquals("tres", list.pop()) //dos
		assertEquals("dos", list.pop()) //-
	}

	@Test
	fun lilo() {
		val list = Lilo<String>()
		list.push("uno") //uno
		list.push("dos") //uno, dos
		list.push("tres") //uno, dos, tres
		list.push("cuatro") //uno, dos, tres, cuatro
		assertEquals("cuatro", list.pop()) //uno, dos, tres
		assertEquals("tres", list.pop()) //uno, dos
		assertEquals("dos", list.pop()) //uno
		assertEquals("uno", list.pop()) //-
	}

	@Test
	fun lilo_max_size() {
		val list = liloOf<String>()
		list.max = 3
		list.push("uno") //uno
		list.push("dos") //uno, dos
		list.push("tres") //uno, dos, tres
		list.push("cuatro") //dos, tres, cuatro
		assertEquals("cuatro", list.pop()) //dos, tres
		assertEquals("tres", list.pop()) //dos
		assertEquals("dos", list.pop()) //-
	}

	@Test
	fun lifo() {
		val list = Lifo<String>()
		list.push("uno") //uno
		list.push("dos") //uno, dos
		list.push("tres") //uno, dos, tres
		list.push("cuatro") //uno, dos, tres, cuatro
		assertEquals("uno", list.pop()) //dos, tres, cuatro
		assertEquals("dos", list.pop()) //tres, cuatro
		assertEquals("tres", list.pop()) //cuatro
		assertEquals("cuatro", list.pop()) //-
	}
	
	@Test
	fun lifo_max_size() {
		val list = lifoOf<String>()
		list.max = 3
		list.push("uno") //uno
		list.push("dos") //uno, dos
		list.push("tres") //uno, dos, tres
		list.push("cuatro") //dos, tres, cuatro
		assertEquals("dos", list.pop()) //tres, cuatro
		assertEquals("tres", list.pop()) //cuatro
		assertEquals("cuatro", list.pop()) //-
	}
	
	@Test
	fun filo() {
		val list = Filo<String>()
		list.push("uno") //uno
		list.push("dos") //dos, uno
		list.push("tres") //tres, dos, uno
		list.push("cuatro") //cuatro, tres, dos, uno
		assertEquals("uno", list.pop()) //cuatro, tres, dos
		assertEquals("dos", list.pop()) //cuatro, tres
		assertEquals("tres", list.pop()) //cuatro
		assertEquals("cuatro", list.pop()) //-
	}
	
	@Test
	fun filo_max_size() {
		val list = filoOf<String>()
		list.max = 3
		list.push("uno") //uno
		list.push("dos") //dos, uno
		list.push("tres") //tres, dos, uno
		list.push("cuatro") //cuatro, tres, dos
		assertEquals("dos", list.pop()) //cuatro, tres
		assertEquals("tres", list.pop()) //cuatro
		assertEquals("cuatro", list.pop()) //-
	}

	@Test
	fun no_dupe_mutable_list() {
		val noDupe = noDupeMutableListOf<Int>()
		noDupe.push(1)
		noDupe.push(2)
		noDupe.push(2)
		assertEquals(2, noDupe.size)
		noDupe.push(3)
		assertEquals(3, noDupe.size)
		noDupe.push(3)
		assertEquals(3, noDupe.size)
		noDupe.push(3)
		assertEquals(3, noDupe.size)
		noDupe.remove(2)
		assertEquals(2, noDupe.size)
		noDupe.remove(2)
		assertEquals(2, noDupe.size)

		assertEquals(1, noDupe.items.first())
		noDupe.removeAt(0)
		assertEquals(2, noDupe.items.first())
		assertEquals(1, noDupe.size)
		noDupe.push(3)
		noDupe.removeAll(listOf(2, 3))
		assertEquals(0, noDupe.size)
	}
	
}
