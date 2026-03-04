package se.mau.grupp7.happyplant2.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import se.mau.grupp7.happyplant2.model.UserPlant
import se.mau.grupp7.happyplant2.model.WaterAmount
import java.util.Date
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class PlantDaoTest {

    private lateinit var db: PlantDatabase
    private lateinit var dao: PlantDatabase.PlantDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, PlantDatabase::class.java)
            .allowMainThreadQueries() //i test är detta ok!!
            .build()

        dao = db.plantDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    private fun plant(id:String, wateredAt: Date = Date(1), name: String = "Test"): UserPlant =
        UserPlant(
            id = id,
            name = name,
            description = "Desc",
            imageURL = "url",
            wateringIntervalMin = 1,
            wateringIntervalMax = 3,
            wateringAmount = WaterAmount.RARELY,
            lastTimeWatered = wateredAt
        )

    @Test
    fun updatePlant_updatesFields() = runTest {
        val p = plant("a", name = "Old")
        dao.insertPlant(p)

        dao.updatePlant(p.copy(customName = "NewName"))

        val all = dao.getAllPlants().first()
        assertEquals("NewName", all.first().customName)
    }

    @Test
    fun deletePlant_removesPlant() = runTest {
        val p = plant("a")
        dao.insertPlant(p)

        dao.deletePlant(p)

        val all = dao.getAllPlants().first()
        assertEquals(0, all.size)
    }

    @Test
    fun updateLastWateredForIds_updatesOnlySelectedIds() = runTest {
        val p1 = plant("a", wateredAt = Date(1))
        val p2 = plant("b", wateredAt = Date(1))
        val p3 = plant("c", wateredAt = Date(1))
        dao.insertPlant(p1)
        dao.insertPlant(p2)
        dao.insertPlant(p3)

        val newTime = 999L
        dao.updateLastWateredForIds(listOf("a", "c"), newTime)

        val all = dao.getAllPlants().first().associateBy { it.id }
        assertEquals(999L, all.getValue("a").lastTimeWatered.time)
        assertEquals(1L, all.getValue("b").lastTimeWatered.time)   // oförändrad
        assertEquals(999L, all.getValue("c").lastTimeWatered.time)
    }
}