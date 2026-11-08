package com.stenis.stocklisttest.core.extension

import org.junit.Assert.assertEquals
import org.junit.Test

class DoubleExtTest {

    @Test
    fun `toDecimalString trims trailing zeros and adds thousands separator`() {
        assertEquals("2,135", 2135.0.toDecimalString())
        assertEquals("14.5", 14.50.toDecimalString())
        assertEquals("33.65", 33.65.toDecimalString())
        assertEquals("0", 0.0.toDecimalString())
        assertEquals(EMPTY_PLACEHOLDER, (null as Double?).toDecimalString())
    }

    @Test
    fun `toSignedString adds sign except zero`() {
        assertEquals("+35", 35.0.toSignedString())
        assertEquals("-0.05", (-0.05).toSignedString())
        assertEquals("+1,234.5", 1234.5.toSignedString())
        assertEquals("0", 0.0.toSignedString())
        assertEquals(EMPTY_PLACEHOLDER, (null as Double?).toSignedString())
    }

    @Test
    fun `toPercentString appends percent sign`() {
        assertEquals("2.99%", 2.99.toPercentString())
        assertEquals("3.9%", 3.90.toPercentString())
        assertEquals(EMPTY_PLACEHOLDER, (null as Double?).toPercentString())
    }
}
