package se.mau.grupp7.happyplant2.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plants")
data class PlantEntity(

    @PrimaryKey
    val id: String,

    val customName: String,
    val name: String,
    val description: String,
    val imageURL: String,
    val localImageUri: String?,
    val wateringInterval: Int,
    val wateringAmount: String,
    val lastTimeWatered: Long,
    val dateAdded: Long,
    val category: String,
    val healthStatus: Int,
    val defect: String,
    val family: String,
    val sunlight: String,
    val wateringNeeds: String,
    val potType: String,
    val heightCm: String,
    val notes: String
)
