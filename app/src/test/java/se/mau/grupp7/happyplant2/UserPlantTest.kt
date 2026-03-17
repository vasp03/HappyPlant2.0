package se.mau.grupp7.happyplant2

import org.junit.Assert.assertEquals
import org.junit.Test
import se.mau.grupp7.happyplant2.model.PlantDetails
import se.mau.grupp7.happyplant2.model.UserPlant
import se.mau.grupp7.happyplant2.model.WaterAmount
import java.util.Date

class UserPlantTest {

    @Test
    fun plantDetails_initialization_isCorrect() {
        val id = 1
        val name = "Rose"
        val scientificName = "Rosa"
        val genus = "Rosa"
        val family = "Rosaceae"
        val url = "http://example.com/rose.jpg"

        val plantDetails = PlantDetails(id, name, scientificName, genus, family, url)

        assertEquals(id, plantDetails.id)
        assertEquals(name, plantDetails.common_name)
        assertEquals(scientificName, plantDetails.scientific_name)
        assertEquals(genus, plantDetails.genus)
        assertEquals(family, plantDetails.family)
        assertEquals(url, plantDetails.imageUrl)
    }

    @Test
    fun userPlant_initialization_isCorrect() {
        val id = "test-uuid"
        val name = "My Rose"
        val commonName = "Rose"
        val description = "A beautiful flower"
        val imageUrl = "http://example.com/rose.jpg"
        val now = Date()
        
        val userPlant = UserPlant(
            id = id,
            customName = name,
            name = commonName,
            description = description,
            imageURL = imageUrl,
            wateringIntervalMin = 2,
            wateringIntervalMax = 5,
            wateringAmount = WaterAmount.OFTEN,
            lastTimeWatered = now,
            category = "Flowers",
            healthStatus = 5,
            family = "Rosaceae"
        )

        assertEquals(id, userPlant.id)
        assertEquals(name, userPlant.customName)
        assertEquals(commonName, userPlant.name)
        assertEquals(description, userPlant.description)
        assertEquals(imageUrl, userPlant.imageURL)
        assertEquals(2, userPlant.wateringIntervalMin)
        assertEquals(5, userPlant.wateringIntervalMax)
        assertEquals(WaterAmount.OFTEN, userPlant.wateringAmount)
        assertEquals(now, userPlant.lastTimeWatered)
        assertEquals("Flowers", userPlant.category)
        assertEquals(5, userPlant.healthStatus)
        assertEquals("Rosaceae", userPlant.family)
    }

    @Test
    fun userPlant_defaultValues_isCorrect() {
        val now = Date()
        val userPlant = UserPlant(
            name = "Rose",
            description = "Desc",
            imageURL = "url",
            wateringIntervalMin = 1,
            wateringIntervalMax = 3,
            wateringAmount = WaterAmount.RARELY,
            lastTimeWatered = now
        )

        assertEquals("", userPlant.customName)
        assertEquals(5, userPlant.healthStatus)
        assertEquals("none", userPlant.defectId)
        assertEquals("", userPlant.category)
    }
}
