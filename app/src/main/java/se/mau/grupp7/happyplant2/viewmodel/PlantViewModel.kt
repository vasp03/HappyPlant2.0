package se.mau.grupp7.happyplant2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import se.mau.grupp7.happyplant2.local.LocalPlantRepository
import se.mau.grupp7.happyplant2.model.*
import se.mau.grupp7.happyplant2.network.PlantRepository
import java.util.Date

class PlantViewModel(application: Application) : AndroidViewModel(application) {

    private val remoteRepository = PlantRepository()
    private val localRepository = LocalPlantRepository(application)
    private val _flowerList = MutableStateFlow<List<PlantDetails>>(emptyList())
    val flowerList: StateFlow<List<PlantDetails>> = _flowerList

    val userPlants: StateFlow<List<UserPlant>> =
        localRepository.plants
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories

    fun getFlowers(query: String) {
        viewModelScope.launch {
            try {
                val response = remoteRepository.getSpecies(query)

                _flowerList.value = response.data.map { ft ->
                    PlantDetails(
                        id = ft.id,
                        common_name = ft.common_name,
                        scientific_name = ft.scientific_name.joinToString(", "),
                        genus = ft.genus ?: "",
                        family = ft.family ?: "",
                        imageUrl = ft.default_image?.regular_url ?: ""
                    )
                }
            } catch (_: Exception) {
                // :/
            }
        }
    }

    fun addPlantToUserCollection(
        plantDetails: PlantDetails,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val details =
                    remoteRepository.getSpeciesDetails(plantDetails.id)

                val intervalDays = when (details.watering.lowercase()) {
                    "frequent" -> 3
                    "average" -> 7
                    "minimum" -> 14
                    "none" -> 30
                    else -> 7
                }

                val waterAmount = when (details.watering.lowercase()) {
                    "frequent" -> WaterAmount.OFTEN
                    "average" -> WaterAmount.RARELY
                    "minimum" -> WaterAmount.CACTUS
                    "none" -> WaterAmount.NEVER
                    else -> WaterAmount.RARELY
                }

                val newPlant = UserPlant(
                    name = plantDetails.common_name,
                    description = plantDetails.scientific_name,
                    imageURL = plantDetails.imageUrl,
                    wateringInterval = intervalDays,
                    wateringAmount = waterAmount,
                    lastTimeWatered = Date(),
                    family = plantDetails.family,
                    sunlight = details.sunlight.joinToString(", "),
                    wateringNeeds = details.watering,
                    healthStatus = 5,
                    defect = Defect.NONE
                )

                localRepository.insert(newPlant)

            } catch (_: Exception) {
                onError()
            }
        }
    }

    fun updatePlantDefect(plant: UserPlant, defect: Defect) {
        viewModelScope.launch {

            val healthModifier = when (defect) {
                Defect.WILTING_LEAVES -> -1
                Defect.DEAD -> -5
                else -> 0
            }

            val newHealth =
                (plant.healthStatus + healthModifier).coerceIn(0, 5)

            localRepository.update(
                plant.copy(
                    healthStatus = newHealth,
                    defect = defect
                )
            )
        }
    }

    fun waterUserPlant(plant: UserPlant) {
        viewModelScope.launch {
            localRepository.update(
                plant.copy(lastTimeWatered = Date())
            )
        }
    }

    fun deleteUserPlant(plant: UserPlant) {
        viewModelScope.launch {
            localRepository.delete(plant)
        }
    }

    fun updatePlantCategory(
        plant: UserPlant,
        newCategory: String
    ) {
        viewModelScope.launch {
            localRepository.update(
                plant.copy(category = newCategory.trim())
            )
        }
    }

    fun updatePlantDetails(
        plant: UserPlant,
        customName: String,
        potType: String,
        heightCm: String,
        notes: String
    ) {
        viewModelScope.launch {
            localRepository.update(
                plant.copy(
                    customName = customName,
                    potType = potType,
                    heightCm = heightCm,
                    notes = notes
                )
            )
        }
    }

    fun updatePlantImage(plant: UserPlant, newUri: String) {
        viewModelScope.launch {
            localRepository.update(
                plant.copy(localImageUri = newUri)
            )
        }
    }

    val overallHealthPercentage: StateFlow<Int> =
        userPlants
            .map { plants ->

                if (plants.isEmpty()) return@map 100

                val totalHealth =
                    plants.sumOf { it.healthStatus }

                val maxHealth = plants.size * 5

                ((totalHealth.toFloat() / maxHealth) * 100)
                    .toInt()
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                100
            )

    fun addCategoryIfNotExists(category: String) {
        val trimmed = category.trim()

        if (trimmed.isNotEmpty()
            && trimmed !in _categories.value
        ) {
            _categories.value += trimmed
        }
    }
}
