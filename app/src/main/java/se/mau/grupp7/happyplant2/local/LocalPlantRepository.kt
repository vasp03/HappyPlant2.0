package se.mau.grupp7.happyplant2.local

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import se.mau.grupp7.happyplant2.model.UserPlant

class LocalPlantRepository(context: Context) {

    private val dao = PlantDatabase.getDatabase(context).plantDao()

    val plants: Flow<List<UserPlant>> =
        dao.getAllPlants().map { list ->
            list.map { it.toUserPlant() }
        }

    suspend fun insert(plant: UserPlant) {
        dao.insertPlant(plant.toEntity())
    }

    suspend fun update(plant: UserPlant) {
        dao.updatePlant(plant.toEntity())
    }

    suspend fun delete(plant: UserPlant) {
        dao.deletePlant(plant.toEntity())
    }
}
