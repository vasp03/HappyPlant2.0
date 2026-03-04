package se.mau.grupp7.happyplant2.viewmodel

import se.mau.grupp7.happyplant2.model.UserPlant
import java.util.Date

internal fun selectPlantsToWater(
    plants: List<UserPlant>,
    ids: List<String>,
    now: Date
): List<UserPlant> {
    if (ids.isEmpty()) return emptyList()

    val idSet = ids.toSet()
    return plants
        .asSequence()
        .filter { it.id in idSet }
        .map { it.copy(lastTimeWatered = now) }
        .toList()
}