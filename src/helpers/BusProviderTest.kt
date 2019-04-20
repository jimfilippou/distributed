package helpers

import org.junit.Assert.*
import org.junit.Test

class BusProviderTest {

    @Test
    fun readBusPositions() {
        assertTrue(BusProvider.readBusPositions(intArrayOf(821, 804)).entries.size > 0)
    }

}