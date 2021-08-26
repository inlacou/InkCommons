package com.inlacou.inkkotlinextensions.extensions

import com.inlacou.inkkotlinextensions.*
import org.junit.Assert.*
import org.junit.jupiter.api.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class StringExtensionsTest2 {

	@Test fun `1dot2dot0 previous version to 1dot2dot1`() = assertTrue("1.2.0".previousVersion("1.2.1"))
	@Test fun `1dot2dot0 previous version to 1dot2dot1dot0`() = assertTrue("1.2.0".previousVersion("1.2.1.0"))
	@Test fun `1dot2dot0 previous version to 1dot2dot0dot1`() = assertTrue("1.2.0".previousVersion("1.2.0.1"))
	@Test fun `1dot2dot0dot0 previous version to 1dot2dot0dot1`() = assertTrue("1.2.0.0".previousVersion("1.2.0.1"))
	@Test fun `1dot2dot0dot0 previous version to 1dot2dot0dot1dot0`() = assertTrue("1.2.0.0".previousVersion("1.2.0.1.0"))
	@Test fun `1dot2dot0 not previous version to 1dot1dot6dot3`() = assertFalse("1.2.0".previousVersion("1.1.6.3"))
	@Test fun `1dot2dot0 not previous version to 1dot1dot9`() = assertFalse("1.2.0".previousVersion("1.1.9"))
	@Test fun `1dot2dot0 not previous version to 1dot1dot9dot0`() = assertFalse("1.2.0".previousVersion("1.1.9.0"))
	@Test fun `1dot2dot0dot1 not previous version to 1dot2dot0`() = assertFalse("1.2.0.1".previousVersion("1.2.0"))
	@Test fun `1dot2dot0dot1dot0 not previous version to 1dot2dot0`() = assertFalse("1.2.0.1.0".previousVersion("1.2.0"))
	@Test fun `1dot2dot0 same version as 1dot2dot0`() = assertFalse("1.2.0".previousVersion("1.2.0"))
	@Test fun `1dot2dot0dot0 same version as 1dot2dot0`() = assertFalse("1.2.0.0".previousVersion("1.2.0"))
	@Test fun `1dot2dot0dot0dot0 same version as 1dot2dot0dot0dot0`() = assertFalse("1.2.0.0.0".previousVersion("1.2.0.0.0"))
	
}