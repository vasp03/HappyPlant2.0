package se.mau.grupp7.happyplant2

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import se.mau.grupp7.happyplant2.model.PlantDetails
import se.mau.grupp7.happyplant2.oldfiles.PlantType
import se.mau.grupp7.happyplant2.viewmodel.PlantViewModel

class UnitTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
    }

    @Test
    fun plantTypeTest() {
        val name = "Rose"
        val description = "Rose is a plant."
        val url = "http://url.url"

        val plantData = PlantType(name, description, url)

        Assert.assertEquals("Name: ", name, plantData.name)
        Assert.assertEquals("Description: ", description, plantData.description)
        Assert.assertEquals("Image URL: ", url, plantData.imageURL)
    }

    @Test
    fun plantDetailsTest() {
        val id = 1
        val name = "Rose"
        val scientific_name = "Rose but fancy"
        val genus = "Genus 1"
        val family = "Family 1"
        val url = "http://url.url"

        val plantDetails = PlantDetails(id, name, scientific_name, genus, family, url)


        Assert.assertEquals("ID: ", id.toLong(), plantDetails.id.toLong())
        Assert.assertEquals("Name: ", name, plantDetails.common_name)
        Assert.assertEquals("Scientific Name: ", scientific_name, plantDetails.scientific_name)
        Assert.assertEquals("Genus: ", genus, plantDetails.genus)
        Assert.assertEquals("Family: ", family, plantDetails.family)
        Assert.assertEquals("Image URL: ", url, plantDetails.imageUrl)
    }

    @Test
    fun testAdditionToUserPlantCollection() {
        val viewModel = PlantViewModel()

        val plant1Id = 1
        val plant1Name = "Rose"
        val plant1SciName = "Rose but fancy"
        val plant1Genus = "Genus 1"
        val plant1Family = "Family 1"
        val plant1Image = "image 1"

        val plant2Id = 2
        val plant2Name = "Rose 2"
        val plant2SciName = "Rose but fancy 2"
        val plant2Genus = "Genus 2"
        val plant2Family = "Family 2"
        val plant2Image = "image 2"

        val plant1 = PlantDetails(plant1Id, plant1Name, plant1SciName, plant1Genus, plant1Family, plant1Image)
        val plant2 = PlantDetails(plant2Id, plant2Name, plant2SciName, plant2Genus, plant2Family, plant2Image)

        viewModel.addPlantToUserCollection(plant1) {
        }

        viewModel.addPlantToUserCollection(plant2) {
        }
    }

    @Test
    fun testHealthPercentage() {
        val viewModel = PlantViewModel()

        val plant1Id = 1
        val plant1Name = "Rose"
        val plant1SciName = "Rose but fancy"
        val plant1Genus = "Genus 1"
        val plant1Family = "Family 1"
        val plant1Image = "image 1"

        val plant2Id = 2
        val plant2Name = "Rose 2"
        val plant2SciName = "Rose but fancy 2"
        val plant2Genus = "Genus 2"
        val plant2Family = "Family 2"
        val plant2Image = "image 2"

        val plant1 = PlantDetails(plant1Id, plant1Name, plant1SciName, plant1Genus, plant1Family, plant1Image)
        val plant2 = PlantDetails(plant2Id, plant2Name, plant2SciName, plant2Genus, plant2Family, plant2Image)

        viewModel.addPlantToUserCollection(plant1) {
        }

        viewModel.addPlantToUserCollection(plant2) {
        }

        val healthPercentage = viewModel.overallHealthPercentage.value
        Assert.assertTrue(healthPercentage == 100)
    }

    @Test
    fun testHealthPercentageBelow100() {
        val viewModel = PlantViewModel()

        val plant1Id = 1
        val plant1Name = "Rose"
        val plant1SciName = "Rose but fancy"
        val plant1Genus = "Genus 1"
        val plant1Family = "Family 1"
        val plant1Image = "image 1"

        val plant2Id = 2
        val plant2Name = "Rose 2"
        val plant2SciName = "Rose but fancy 2"
        val plant2Genus = "Genus 2"
        val plant2Family = "Family 2"
        val plant2Image = "image 2"

        val plant1 = PlantDetails(plant1Id, plant1Name, plant1SciName, plant1Genus, plant1Family, plant1Image)
        val plant2 = PlantDetails(plant2Id, plant2Name, plant2SciName, plant2Genus, plant2Family, plant2Image)

        viewModel.addPlantToUserCollection(plant1) {
        }

        viewModel.addPlantToUserCollection(plant2) {
        }

        val healthPercentage = viewModel.overallHealthPercentage.value
        Assert.assertTrue(healthPercentage == 100)
    }
}