package se.mau.grupp7.happyplant2.viewmodel

import se.mau.grupp7.happyplant2.model.UserPlant

internal fun calculateCategories(plants: List<UserPlant>): List<String> {
    return plants
        .map { it.category.trim() }
        .filter { it.isNotBlank() }
        .distinctBy { it.lowercase() }
        .sortedBy { it.lowercase() }
}