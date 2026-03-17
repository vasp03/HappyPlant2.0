package se.mau.grupp7.happyplant2

import org.junit.Assert.*
import org.junit.Test
import se.mau.grupp7.happyplant2.model.PlantDetails
import se.mau.grupp7.happyplant2.network.PlantRepository

class PlantRepositoryLogicTest {

    private val repo = PlantRepository()

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
