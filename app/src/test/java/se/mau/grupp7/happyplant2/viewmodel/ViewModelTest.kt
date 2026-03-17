package se.mau.grupp7.happyplant2.viewmodel

import android.app.Application
import app.cash.turbine.test
import io.mockk.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.*
import se.mau.grupp7.happyplant2.MainDispatcherRule
import se.mau.grupp7.happyplant2.local.LocalPlantRepository
import se.mau.grupp7.happyplant2.model.*
import se.mau.grupp7.happyplant2.network.PlantRepository
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class PlantViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var vm: PlantViewModel
    private lateinit var mockApp: Application
    private lateinit var mockRemote: PlantRepository
    private lateinit var mockLocal: LocalPlantRepository
    private lateinit var plantsFlow: MutableStateFlow<List<UserPlant>>

    private fun plantDetails(name: String = "Rose") = PlantDetails(
        id = 1,
        common_name = name,
        scientific_name = "Rosa spp.",
        genus = "Rosa",
        family = "Rosaceae",
        imageUrl = "https://example.com/rose.jpg"
    )

    private fun userPlant(
        id: String = "plant-1",
        health: Int = 5,
        category: String = "Flower"
    ) = UserPlant(
        id = id,
        name = "Rose",
        description = "Rosa spp.",
        imageURL = "https://example.com/rose.jpg",
        wateringIntervalMin = 5,
        wateringIntervalMax = 7,
        wateringAmount = WaterAmount.RARELY,
        lastTimeWatered = Date(),
        category = category,
        healthStatus = health
    )

    private fun defect(impact: Int = -1) = Defect(
        id = "def-1",
        displayName = "Aphids",
        category = "Pest",
        subCategory = "Insect",
        healthImpact = impact
    )

    private fun disease() = PestDisease(
        id = 1,
        common_name = "Aphids",
        scientific_name = null,
        other_name = null,
        family = null,
        description = null,
        solution = null,
        host = null,
        images = null,
        symptoms = null
    )

    @Before
    fun setUp() {
        mockApp = mockk(relaxed = true)
        mockRemote = mockk(relaxed = false)
        mockLocal = mockk(relaxed = false)

        plantsFlow = MutableStateFlow(emptyList())
        every { mockLocal.plants } returns plantsFlow

        coEvery { mockRemote.getRankedPlants(any()) } returns emptyList()
        coEvery { mockRemote.getPestDiseases() } returns emptyList()
        coEvery { mockRemote.createUserPlant(any(), any()) } returns userPlant()
        coEvery { mockLocal.insert(any()) } just Runs
        coEvery { mockLocal.update(any()) } just Runs
        coEvery { mockLocal.delete(any()) } just Runs

        vm = PlantViewModel(mockApp, mockRemote, mockLocal)
    }

    private fun TestScope.updatePlants(plants: List<UserPlant>) {
        plantsFlow.value = plants
        advanceUntilIdle()
    }

    @Test
    fun `getFlowers blank query`() = runTest {
        vm.getFlowers("   ")
        advanceUntilIdle()
        vm.flowerList.test {
            assertEquals(emptyList<PlantDetails>(), awaitItem())
        }
        vm.suggestions.test {
            assertEquals(emptyList<String>(), awaitItem())
        }
    }

    @Test
    fun `getFlowers short query ignored`() = runTest {
        vm.getFlowers("r")
        advanceUntilIdle()
        vm.getFlowers("ro")
        advanceUntilIdle()
        vm.suggestions.test {
            assertEquals(emptyList<String>(), awaitItem())
        }
    }

    @Test
    fun `getFlowers returns suggestions on match`() = runTest {
        coEvery { mockRemote.getRankedPlants("rose") } returns emptyList()

        vm.getFlowers("rose")
        advanceUntilIdle()

        vm.suggestions.test {
            assertEquals(listOf("rose", "rosa"), awaitItem())
        }
    }

    @Test
    fun `getDiseases first call`() = runTest {
        coEvery { mockRemote.getPestDiseases() } returns listOf(disease())
        vm.getDiseases()
        advanceUntilIdle()
        vm.diseaseList.test {
            assertEquals(listOf(disease()), awaitItem())
        }
        coVerify(exactly = 1) { mockRemote.getPestDiseases() }
    }

    @Test
    fun `addPlantToUserCollection inserts plant`() = runTest {
        var errorCalled = false
        vm.addPlantToUserCollection(
            plantDetails(),
            0,
            {},
            { errorCalled = true }
        )
        advanceUntilIdle()
        assertTrue(!errorCalled)
        coVerify { mockLocal.insert(any()) }
    }

    @Test
    fun `updatePlantDefect modifies health correctly`() = runTest {
        val original = userPlant(health = 5)
        updatePlants(listOf(original))

        listOf(-6, -5, 0, 1, 5, 6).forEach { impact ->
            vm.updatePlantDefect(original, defect(impact))
            advanceUntilIdle()
        }

        coVerify(exactly = 2) { mockLocal.update(match { it.healthStatus == 0 }) }
        coVerify(exactly = 4) { mockLocal.update(match { it.healthStatus == 5 }) }
    }

    @Test
    fun `waterSelectedPlants with empty list does nothing`() = runTest {
        vm.waterSelectedPlants(emptyList())
        advanceUntilIdle()
        coVerify(exactly = 0) { mockLocal.update(any()) }
    }

    @Test
    fun `overallHealthPercentage updates`() = runTest {
        vm.overallHealthPercentage.test {
            assertEquals(100, awaitItem())
            updatePlants(listOf(userPlant(health = 0)))
            assertEquals(0, awaitItem())
            updatePlants(listOf(userPlant(health = 5)))
            assertEquals(100, awaitItem())
            updatePlants(listOf(userPlant(health = 0), userPlant(health = 5)))
            assertEquals(50, awaitItem())
        }
    }

    @Test
    fun `categories flow trims duplicates`() = runTest {
        vm.categories.test {
            assertEquals(emptyList<String>(), awaitItem())
            updatePlants(
                listOf(
                    userPlant(category = "   "),
                    userPlant(category = "Flower"),
                    userPlant(category = "flower "),
                    userPlant(category = "Rose")
                )
            )
            assertEquals(listOf("Flower", "Rose"), awaitItem())
        }
    }

    @Test
    fun `getPlantById returns correct plant or null`() = runTest {
        val p1 = userPlant(id = "123")

        vm.getPlantById("123").test {
            updatePlants(listOf(p1))
            assertEquals(null, awaitItem())
            assertEquals(p1, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updatePlantCategory trims whitespace`() = runTest {
        val plant = userPlant()
        vm.updatePlantCategory(plant, "  New  ")
        advanceUntilIdle()
        coVerify { mockLocal.update(match { it.category == "New" }) }
    }

    @Test
    fun `updatePlantDetails accepts all values`() = runTest {
        val plant = userPlant()
        vm.updatePlantDetails(plant, "MyRose", "Ceramic", "-10", "notes")
        advanceUntilIdle()
        coVerify { mockLocal.update(match { it.heightCm == "-10" }) }
    }

    @Test
    fun `deleteUserPlant calls local delete`() = runTest {
        val plant = userPlant()
        vm.deleteUserPlant(plant)
        advanceUntilIdle()
        coVerify { mockLocal.delete(any()) }
    }
}