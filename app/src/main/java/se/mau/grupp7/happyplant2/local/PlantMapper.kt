package se.mau.grupp7.happyplant2.local

import se.mau.grupp7.happyplant2.model.UserPlant
import se.mau.grupp7.happyplant2.model.WaterAmount
import se.mau.grupp7.happyplant2.model.Defect
import java.util.Date

fun PlantEntity.toUserPlant(): UserPlant {
    return UserPlant(
        id = id,
        customName = customName,
        name = name,
        description = description,
        imageURL = imageURL,
        localImageUri = localImageUri,
        wateringInterval = wateringInterval,
        wateringAmount = WaterAmount.valueOf(wateringAmount),
        lastTimeWatered = Date(lastTimeWatered),
        dateAdded = Date(dateAdded),
        category = category,
        healthStatus = healthStatus,
        defect = Defect.valueOf(defect),
        family = family,
        sunlight = sunlight,
        wateringNeeds = wateringNeeds,
        potType = potType,
        heightCm = heightCm,
        notes = notes
    )
}

fun UserPlant.toEntity(): PlantEntity {
    return PlantEntity(
        id = id,
        customName = customName,
        name = name,
        description = description,
        imageURL = imageURL,
        localImageUri = localImageUri,
        wateringInterval = wateringInterval,
        wateringAmount = wateringAmount.name,
        lastTimeWatered = lastTimeWatered.time,
        dateAdded = dateAdded.time,
        category = category,
        healthStatus = healthStatus,
        defect = defect.name,
        family = family,
        sunlight = sunlight,
        wateringNeeds = wateringNeeds,
        potType = potType,
        heightCm = heightCm,
        notes = notes
    )
}
