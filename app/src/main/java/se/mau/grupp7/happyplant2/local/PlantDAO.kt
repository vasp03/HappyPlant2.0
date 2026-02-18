package se.mau.grupp7.happyplant2.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {

    @Query("SELECT * FROM plants")
    fun getAllPlants(): Flow<List<PlantEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: PlantEntity)

    @Update
    suspend fun updatePlant(plant: PlantEntity)

    @Delete
    suspend fun deletePlant(plant: PlantEntity)
}
