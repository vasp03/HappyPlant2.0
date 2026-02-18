package se.mau.grupp7.happyplant2.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [PlantEntity::class],
    version = 1
)
abstract class PlantDatabase : RoomDatabase() {

    abstract fun plantDao(): PlantDao

    companion object {

        @Volatile
        private var INSTANCE: PlantDatabase? = null

        fun getDatabase(context: Context): PlantDatabase {
            return INSTANCE ?: synchronized(this) {
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
}
