package se.mau.grupp7.happyplant2.local

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LocalPlantRepositoryTest {

    private lateinit var dao: PlantDatabase.PlantDao
    private lateinit var repo: LocalPlantRepository

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        repo = LocalPlantRepository(dao)
    }

    @Test
    fun `waterPlants with empty list calls dao with empty ids`() = runTest {
        repo.waterPlants(emptyList())

        coVerify { dao.updateLastWateredForIds(emptyList(), any()) }
    }

    @Test
    fun `waterPlants with multiple ids calls dao with correct ids`() = runTest {
        val ids = listOf("a", "b", "c")

        repo.waterPlants(ids)

        coVerify { dao.updateLastWateredForIds(ids, any()) }
    }
}