package se.mau.grupp7.happyplant2.local

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import se.mau.grupp7.happyplant2.model.UserPlant
import se.mau.grupp7.happyplant2.model.WaterAmount
import se.mau.grupp7.happyplant2.model.Defect
import java.util.Date

class Converters {
    @TypeConverter fun fromWaterAmount(value: WaterAmount) = value.name
    @TypeConverter fun toWaterAmount(name: String) = WaterAmount.valueOf(name)

    @TypeConverter fun fromDefect(value: Defect) = value.name
    @TypeConverter fun toDefect(name: String) = Defect.valueOf(name)

    @TypeConverter fun fromDate(date: Date) = date.time
    @TypeConverter fun toDate(time: Long) = Date(time)
}

@Database(entities = [UserPlant::class], version = 1)
@TypeConverters(Converters::class)
abstract class PlantDatabase : RoomDatabase() {

    abstract fun plantDao(): PlantDao

    @Dao
    interface PlantDao {
        @Query("SELECT * FROM plants")
        fun getAllPlants(): Flow<List<UserPlant>>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertPlant(plant: UserPlant)

        @Update
        suspend fun updatePlant(plant: UserPlant)

        @Delete
        suspend fun deletePlant(plant: UserPlant)
    }

    companion object {
        @Volatile private var INSTANCE: PlantDatabase? = null

        fun getDatabase(context: Context): PlantDatabase =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlantDatabase::class.java,
                    "plant_database"
                ).build()
                INSTANCE = instance
                instance
            }
    }
}
