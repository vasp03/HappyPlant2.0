package se.mau.grupp7.happyplant2.local

import org.junit.Assert
import org.junit.Test
import se.mau.grupp7.happyplant2.model.WaterAmount
import java.util.Date

class ConvertersTest {

    private val converters = Converters()

    @Test
    fun waterAmount_roundTrip_returnsSameEnum() {
        val original = WaterAmount.RARELY
        val stored = converters.fromWaterAmount(original)
        val restored = converters.toWaterAmount(stored)

        Assert.assertEquals(original, restored)
    }

    @Test
    fun date_roundTrip_returnsSameTime() {
        val original = Date(1700000000000L)
        val stored = converters.fromDate(original)
        val restored = converters.toDate(stored)

        Assert.assertEquals(original.time, restored.time)
    }

    @Test
    fun waterAmount_roundTrip_worksForAllValues() {
        for (value in WaterAmount.entries) {
            val restored = converters.toWaterAmount(converters.fromWaterAmount(value))
            Assert.assertEquals(value, restored)
        }
    }

    @Test
    fun date_roundTrip_handelsEpoch() {
        val d = Date(0L)
        val restored = converters.toDate(converters.fromDate(d))
        Assert.assertEquals(0L, restored.time)
    }
}