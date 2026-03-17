package se.mau.grupp7.happyplant2

import org.junit.Assert.*
import org.junit.Test
import se.mau.grupp7.happyplant2.model.PlantDetails
import se.mau.grupp7.happyplant2.model.WaterAmount
import se.mau.grupp7.happyplant2.network.PlantRepository

class PlantRepositoryLogicTest {

    private val repo = PlantRepository()

    /*
    @Test
    fun `parseWaterInterval returns correct range for valid interval string`() {
        val result = repo.run {
            parseWaterInterval("5-7", "average")
        }
        assertEquals(5 to 7, result)
    }

    @Test
    fun `parseWaterInterval expands single value using boundary spread`() {
        val result = repo.run {
            parseWaterInterval("10", "average")
        }
        assertEquals(7 to 13, result)
    }

    @Test
    fun `parseWaterInterval returns default for invalid string`() {
        val result = repo.run {
            parseWaterInterval("invalid", "average")
        }
        assertEquals(5 to 7, result)
    }

    @Test
    fun `parseWaterInterval uses watering mapping when benchmark is null`() {
        val result = repo.run {
            parseWaterInterval(null, "frequent")
        }
        assertEquals(2 to 3, result)
    }

    @Test
    fun `parseWaterInterval clamps lower boundary to at least one`() {
        val result = repo.run {
            parseWaterInterval("1", "average")
        }
        assertTrue(result.first >= 1)
    }

    @Test
    fun `mapWaterAmount returns correct enum for all boundaries and default`() {
        assertEquals(WaterAmount.OFTEN, repo.run { mapWaterAmount("frequent") })
        assertEquals(WaterAmount.RARELY, repo.run { mapWaterAmount("average") })
        assertEquals(WaterAmount.CACTUS, repo.run { mapWaterAmount("minimum") })
        assertEquals(WaterAmount.NEVER, repo.run { mapWaterAmount("none") })

        assertEquals(WaterAmount.RARELY, repo.run { mapWaterAmount("unknown") })
    }

     */

    @Test
    fun `rankPlants filters and sorts matching plants correctly`() {
        val plants = listOf(
            PlantDetails(1, "Rose", "Rosa", "Rosa", "", ""),
            PlantDetails(2, "Tulip", "Tulipa", "Tulipa", "", ""),
            PlantDetails(3, "Random", "Something", "Genus", "", "")
        )

        val result = repo.run {
            rankPlants(plants, "ro")
        }

        assertEquals(1, result.size)
        assertEquals("Rose", result[0].common_name)
    }
}
