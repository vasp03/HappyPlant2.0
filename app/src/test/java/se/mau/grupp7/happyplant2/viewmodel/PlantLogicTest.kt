package se.mau.grupp7.happyplant2.viewmodel

import org.junit.Assert
import org.junit.Test
import se.mau.grupp7.happyplant2.model.UserPlant
import se.mau.grupp7.happyplant2.model.WaterAmount
import java.util.Date

class PlantLogicTest {

    private fun plant(health: Int = 5, category: String = "") = UserPlant(
        name = "Test",
        description = "Desc",
        imageURL = "url",
        wateringIntervalMin = 1,
        wateringIntervalMax = 3,
        wateringAmount = WaterAmount.RARELY,
        lastTimeWatered = Date(),
        healthStatus = health,
        category = category
    )

    @Test
    fun overallHealth_empty_returns100() {
        Assert.assertEquals(100, calculateOverallHealthPercentage(emptyList()))
    }

    @Test
    fun overallHealth_twoPlants_halfHealth_returns50() {
        val plants = listOf(plant(5), plant(0))
        Assert.assertEquals(50, calculateOverallHealthPercentage(plants))
    }

    @Test
    fun overallHealth_roundsDown() {
        val plants = listOf(plant(1), plant(0), plant(0))
        Assert.assertEquals(6, calculateOverallHealthPercentage(plants))
    }

    @Test
    fun categories_trimsFiltersAndSorts() {
        val plants = listOf(
            plant(category = "  Herbs "),
            plant(category = "Flowers"),
            plant(category = " "),
            plant(category = "Trees")
        )

        val result = calculateCategories(plants)

        Assert.assertEquals(listOf("Flowers", "Herbs", "Trees"), result)
    }

    @Test
    fun categories_removesDuplicates() {
        val plants = listOf(
            plant(category = "Herbs"),
            plant(category = "Herbs "),
            plant(category = "Herbs")
        )

        val result = calculateCategories(plants)

        Assert.assertEquals(listOf("Herbs"), result)
    }

    @Test
    fun newHealth_clampsToRange() {
        Assert.assertEquals(0, calculateNewHealth(-10))
        Assert.assertEquals(4, calculateNewHealth(-1))
        Assert.assertEquals(5, calculateNewHealth(0))
        Assert.assertEquals(5, calculateNewHealth(10))
    }
}