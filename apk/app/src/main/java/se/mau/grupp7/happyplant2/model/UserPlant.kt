package se.mau.grupp7.happyplant2.model

import java.util.Date

data class UserPlant(
    val name: String,
    val description: String,
    val imageURL: String,
    val wateringInterval: Int,
    val wateringAmount: WaterAmount,
    val lastTimeWatered: Date
) {
    fun daysUntilWatering(): Int {
        val millisSince = System.currentTimeMillis() - lastTimeWatered.time
        val millisUntil = wateringInterval * 86400000L - millisSince
        if (millisUntil <= 0) return 0
        var days = (millisUntil / 86400000L).toInt()
        if (millisUntil % 86400000L > 0) days++
        return days
    }
}

enum class WaterAmount {
    OFTEN,
    RARELY,
    CACTUS,
    NEVER
}
