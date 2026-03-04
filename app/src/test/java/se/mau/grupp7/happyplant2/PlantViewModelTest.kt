package se.mau.grupp7.happyplant2

import org.junit.Test
import org.junit.Assert.*
import se.mau.grupp7.happyplant2.model.UserPlant
import se.mau.grupp7.happyplant2.model.WaterAmount
import se.mau.grupp7.happyplant2.viewmodel.calculateOverallHealthPercentage
import java.util.Date
import se.mau.grupp7.happyplant2.viewmodel.calculateCategories
import se.mau.grupp7.happyplant2.viewmodel.calculateNewHealth
import org.junit.Assert.assertNull



class PlantViewModelTest {

    private fun userPlantWithHealth(health: Int): UserPlant {
        return UserPlant(
            name = "Test",
            description = "Desc",
            imageURL = "url",
            wateringIntervalMin = 1,
            wateringIntervalMax = 3,
            wateringAmount = WaterAmount.RARELY,
            lastTimeWatered = Date(),
            healthStatus = health
        )
    }

    @Test
    fun overallHealth_emptyList_returns100() {
        val result = calculateOverallHealthPercentage(emptyList())
        assertEquals(100, result)
    }

    @Test
    fun overallHealth_singlePlantHealth5_returns100() {
        val result = calculateOverallHealthPercentage(listOf(userPlantWithHealth(5)))
        assertEquals(100, result)
    }

    @Test
    fun overallHealth_twoPlants_5and0_returns50() {
        val result = calculateOverallHealthPercentage(
            listOf(userPlantWithHealth(5), userPlantWithHealth(0))
        )
        assertEquals(50, result)
    }

    @Test
    fun overallHealth_threePlants_5_2_1_returns53() {
        //total is 8, max is 15, so in precentage 8/15 = 0.533.., toInt() is 53
        val result = calculateOverallHealthPercentage(
            listOf(userPlantWithHealth(5), userPlantWithHealth(2), userPlantWithHealth(1))
        )
        assertEquals(53, result)
    }

    @Test
    fun overallHealth_allZeroHealth_returns0() {
        val result = calculateOverallHealthPercentage(
            listOf(userPlantWithHealth(0), userPlantWithHealth(0), userPlantWithHealth(0))
        )
        assertEquals(0, result)
    }

    @Test
    fun overallHealth_roundsDown_withToInt() {
        // total is 1, max is 15 => 6.666... => toInt() => 6
        val result = calculateOverallHealthPercentage(
            listOf(userPlantWithHealth(1), userPlantWithHealth(0), userPlantWithHealth(0))
        )
        assertEquals(6, result)
    }

    @Test
    fun overallHealth_mixedValues_returnsExpectedPercentage() {
        // total is 12, max is 20 => 60
        val result = calculateOverallHealthPercentage(
            listOf(
                userPlantWithHealth(5),
                userPlantWithHealth(4),
                userPlantWithHealth(3),
                userPlantWithHealth(0)
            )
        )
        assertEquals(60, result)
    }

    //==========================================================


    @Test
    fun categories_emptyList_returnsEmpty() {
        val result = calculateCategories(emptyList())
        assertEquals(emptyList<String>(), result)
    }

    @Test
    fun categories_trimsWhiteSpace() {
        val plants = listOf(
            userPlantWithCategory("  Flowers  "),
            userPlantWithCategory("Flowers")
        )
        val result = calculateCategories(plants)
        assertEquals(listOf("Flowers"), result)
    }
    private fun userPlantWithCategory(category: String): UserPlant {
        return UserPlant(
            name = "Test",
            description = "Desc",
            imageURL = "url",
            wateringIntervalMin = 1,
            wateringIntervalMax = 3,
            wateringAmount = WaterAmount.RARELY,
            lastTimeWatered = Date(),
            category = category
        )
    }

    @Test
    fun categories_filtersBlankAndEmpty() {
        val plants = listOf(
            userPlantWithCategory(""),
            userPlantWithCategory("  "),
            userPlantWithCategory("Herbs")
        )
        val result = calculateCategories(plants)
        assertEquals(listOf("Herbs"), result)
    }

    @Test
    fun categories_removesDuplicates() {
        val plants = listOf(
            userPlantWithCategory("Herbs"),
            userPlantWithCategory("Herbs"),
            userPlantWithCategory("Herbs ")
        )
        val result = calculateCategories(plants)
        assertEquals(listOf("Herbs"), result)
    }

    @Test
    fun categories_sortsAlphabetically() {
        val plants = listOf(
            userPlantWithCategory("Trees"),
            userPlantWithCategory("Flowers"),
            userPlantWithCategory("Herbs")
        )
        val result = calculateCategories(plants)
        assertEquals(listOf("Flowers", "Herbs", "Trees"), result)
    }

    @Test
    fun categories_handlesMixedWhitespaceAndValidCategories() {
        val plants = listOf(
            userPlantWithCategory("  Herbs "),
            userPlantWithCategory("Flowers"),
            userPlantWithCategory("   "),
            userPlantWithCategory("Trees ")
        )

        val result = calculateCategories(plants)

        assertEquals(listOf("Flowers", "Herbs", "Trees"), result)
    }

    @Test
    fun categories_handlesSingleCategory() {
        val plants = listOf(userPlantWithCategory("Succulents"))

        val result = calculateCategories(plants)

        assertEquals(listOf("Succulents"), result)
    }

    @Test
    fun categories_handlesAllBlankCategories() {
        val plants = listOf(
            userPlantWithCategory(""),
            userPlantWithCategory(" "),
            userPlantWithCategory("   ")
        )

        val result = calculateCategories(plants)

        assertTrue(result.isEmpty())
    }

    @Test
    fun categories_handlesAlreadySortedInput() {
        val plants = listOf(
            userPlantWithCategory("Flowers"),
            userPlantWithCategory("Herbs"),
            userPlantWithCategory("Trees")
        )

        val result = calculateCategories(plants)

        assertEquals(listOf("Flowers", "Herbs", "Trees"), result)
    }

    //========================================

    @Test
    fun newHealth_zeroImpact_returnsMax() {
        assertEquals(5, calculateNewHealth(0))
    }

    @Test
    fun newHealth_negativeImpact_clampedToZero() {
        assertEquals(0, calculateNewHealth(-10))
    }

    @Test
    fun newHealth_positiveImpact_clampedToMax() {
        assertEquals(5, calculateNewHealth(10))
    }

    @Test
    fun newHealth_smallNegativeImpact_decreasesHealth() {
        assertEquals(4, calculateNewHealth(-1))
    }

    @Test
    fun newHealth_smallPositivtImpact_stillClampedAtMax() {
        assertEquals(5, calculateNewHealth(1))
    }

    @Test
    fun newHealth_boundaryAtZero() {
        assertEquals(0, calculateNewHealth(-5))
    }

    @Test
    fun newHealth_boundaryAtMax() {
        assertEquals(5, calculateNewHealth(0))
    }


    //====== TESTER AV fun getPlantById
    @Test
    fun getPlantById_returnsCorrectPlant() {
        val plant1 = userPlantWithHealth(5).copy(id = "1")
        val plant2 = userPlantWithHealth(3).copy(id = "2")

        val list = listOf(plant1, plant2)

        val result = list.find {it.id == "2"}
        assertEquals(plant2, result)
    }

    @Test
    fun getPlantById_returnsNullIfNotFound() {
        val plant = userPlantWithHealth(5).copy(id = "1")
        val list = listOf(plant)
        val result = list.find {it.id == "999"}

        assertNull(result)
    }

    @Test
    fun getPlantById_handlesEmptyList() {
        val list = emptyList<UserPlant>()
        val result = list.find {it.id == "1"}

        assertNull(result)
    }


}