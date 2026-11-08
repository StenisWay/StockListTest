package com.stenis.stocklisttest.core.extension

import org.junit.Assert.assertEquals
import org.junit.Test

class LongExtTest {

    @Test
    fun `toThousandsString adds thousands separator`() {
        assertEquals("68,650,665,000", 68650665000L.toThousandsString())
        assertEquals("296", 296L.toThousandsString())
        assertEquals("0", 0L.toThousandsString())
        assertEquals(EMPTY_PLACEHOLDER, (null as Long?).toThousandsString())
    }
}
