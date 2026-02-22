package se.mau.grupp7.happyplant2.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "plants")
data class UserPlant(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val customName: String = "",
    val name: String,
    val description: String,
    val imageURL: String,
    val localImageUri: String? = null,
    val wateringIntervalMin: Int,
    val wateringIntervalMax: Int,
    val wateringAmount: WaterAmount,
    var lastTimeWatered: Date,
    val dateAdded: Date = Date(),
    var category: String = "",
    val healthStatus: Int = 5,
    val defectId: String = DefectList.NONE.id,
    val family: String = "",
    val sunlight: String = "",
    val wateringNeeds: String = "",
    val potType: String = "",
    val heightCm: String = "",
    val notes: String = ""
)

enum class WaterAmount {
    OFTEN,
    RARELY,
    CACTUS,
    NEVER
}
