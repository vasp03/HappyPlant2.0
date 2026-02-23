package se.mau.grupp7.happyplant2.local

import android.content.Context
import kotlinx.coroutines.flow.Flow
import se.mau.grupp7.happyplant2.model.UserPlant

class LocalPlantRepository(context: Context) {

    private val dao = PlantDatabase.getDatabase(context).plantDao()

    val plants: Flow<List<UserPlant>> = dao.getAllPlants()

    suspend fun insert(plant: UserPlant) = dao.insertPlant(plant)
    suspend fun update(plant: UserPlant) = dao.updatePlant(plant)
    suspend fun delete(plant: UserPlant) = dao.deletePlant(plant)

    suspend fun waterPlants(ids: List<String>) {
        dao.updateLastWateredForIds(ids, System.currentTimeMillis())
    }
}
