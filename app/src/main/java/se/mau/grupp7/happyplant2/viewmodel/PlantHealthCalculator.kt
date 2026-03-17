package se.mau.grupp7.happyplant2.viewmodel

import se.mau.grupp7.happyplant2.model.UserPlant

//this file was created so i could run tests on plantviewmodel and test its logic

internal fun calculateOverallHealthPercentage(plants : List<UserPlant>): Int {
    if (plants.isEmpty()) return 100

    val totalHealth = plants.sumOf { it.healthStatus }
    val maxHealth = plants.size * 5

    return ((totalHealth.toFloat() / maxHealth) * 100).toInt()
}