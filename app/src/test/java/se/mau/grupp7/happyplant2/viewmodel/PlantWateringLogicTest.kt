package se.mau.grupp7.happyplant2.viewmodel

import org.junit.Assert
import org.junit.Test
import se.mau.grupp7.happyplant2.model.UserPlant
import se.mau.grupp7.happyplant2.model.WaterAmount
import java.util.Date

class PlantWateringLogicTest {

    private fun plant(id: String, last: Date): UserPlant =
        UserPlant(
            id = id,
            name = "Test",
            description = "Desc",
            imageURL = "url",
            wateringIntervalMin = 1,
            wateringIntervalMax = 3,
            wateringAmount = WaterAmount.RARELY,
            lastTimeWatered = last
        )

    @Test
    fun selectPlantsToWater_emptyIds_returnsEmpty() {
        val now = Date(123)
        val result = selectPlantsToWater(listOf(plant("a", Date(1))), emptyList(), now)
        Assert.assertTrue(result.isEmpty())
    }

    @Test
    fun selectPlantsToWater_selectOnlyMatchingIds() {
        val old = Date(1)
        val now = Date(999)
        val plants = listOf(plant("a", old), plant("b", old), plant("c", old))

        val result = selectPlantsToWater(plants, listOf("a", "c"), now)

        Assert.assertEquals(2, result.size)
        Assert.assertEquals(setOf("a", "c"), result.map { it.id }.toSet())
    }

    @Test
    fun selectPlantsToWater_updatesLastTimeWateredToNow() {
        val old = Date(1)
        val now = Date(999)
        val plants = listOf(plant("a", old), plant("b", old))

        val result = selectPlantsToWater(plants, listOf("b"), now)

        Assert.assertEquals(1, result.size)
        Assert.assertEquals("b", result[0].id)
        Assert.assertEquals(now, result[0].lastTimeWatered)
    }

    @Test
    fun selectPlantsToWater_doesNotReturnNonSelectedPlants() {
        val old = Date(1)
        val now = Date(999)
        val plants = listOf(plant("a", old), plant("b", old))

        val result = selectPlantsToWater(plants, listOf("a"), now)

        Assert.assertEquals(listOf("a"), result.map { it.id })
    }

    @Test
    fun selectPlantsToWater_allowsDuplicateIds_withoutDuplicatingPlants() {
        val old = Date(1)
        val now = Date(999)
        val plants = listOf(plant("a", old), plant("b", old))

        val result = selectPlantsToWater(plants, listOf("a", "a", "a"), now)

        Assert.assertEquals(1, result.size)
        Assert.assertEquals("a", result.first().id)
    }
}