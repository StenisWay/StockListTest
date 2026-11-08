package com.stenis.stocklisttest.core.extension

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class StringExtTest {

    @Test
    fun `toDoubleOrNullSafe parses api strings`() {
        assertEquals(2135.0, "2135.00".toDoubleOrNullSafe())
        assertEquals(1234.5, " 1,234.5 ".toDoubleOrNullSafe())
        assertEquals(-1.41, "-1.4100".toDoubleOrNullSafe())
        assertNull("".toDoubleOrNullSafe())
        assertNull("--".toDoubleOrNullSafe())
        assertNull((null as String?).toDoubleOrNullSafe())
    }

    @Test
    fun `toLongOrNullSafe parses api strings`() {
        assertEquals(68650665000L, "68650665000".toLongOrNullSafe())
        assertEquals(1234567L, "1,234,567".toLongOrNullSafe())
        assertNull("".toLongOrNullSafe())
        assertNull((null as String?).toLongOrNullSafe())
    }
}
