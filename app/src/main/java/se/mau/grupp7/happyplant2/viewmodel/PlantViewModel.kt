package se.mau.grupp7.happyplant2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import se.mau.grupp7.happyplant2.model.Defect
import se.mau.grupp7.happyplant2.model.PlantDetails
import se.mau.grupp7.happyplant2.model.UserPlant
import se.mau.grupp7.happyplant2.model.WaterAmount
import se.mau.grupp7.happyplant2.network.PlantRepository
import java.util.Date

class PlantViewModel : ViewModel() {

    private val repository = PlantRepository()

    private val _flowerList = MutableStateFlow<List<PlantDetails>>(emptyList())
    val flowerList: StateFlow<List<PlantDetails>> = _flowerList

    private val _userPlants = MutableStateFlow<List<UserPlant>>(emptyList())
    val userPlants: StateFlow<List<UserPlant>> = _userPlants

    fun getFlowers(query: String) {
        viewModelScope.launch {
            try {
                val response = repository.getSpecies(query)
                _flowerList.value = response.data.map { ft ->
                    PlantDetails(
                        id = ft.id,
                        common_name = ft.common_name,
                        scientific_name = ft.scientific_name.joinToString(", "),
                        genus = ft.genus ?: "",
                        imageUrl = ft.default_image?.regular_url ?: ""
                    )
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun addPlantToUserCollection(plantDetails: PlantDetails, onError: () -> Unit) {
        viewModelScope.launch {
            try {
                val details = repository.getSpeciesDetails(plantDetails.id)
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
                    lastTimeWatered = Date()
                )
                _userPlants.value = _userPlants.value + newPlant
            } catch (e: Exception) {
                onError()
            }
        }
    }

    fun updatePlantDefect(plantToUpdate: UserPlant, defect: Defect) {
        val healthModifier = when (defect) {
            Defect.WILTING_LEAVES -> -1
            Defect.DEAD -> -5
            else -> 0
        }
        val newHealth = 5 + healthModifier

        _userPlants.value = _userPlants.value.map { plant ->
            if (plant.id == plantToUpdate.id) {
                plant.copy(healthStatus = newHealth, defect = defect)
            } else {
                plant
            }
        }
    }

    fun waterUserPlant(plant: UserPlant) {
        _userPlants.value = _userPlants.value.map { 
            if (it.id == plant.id) it.copy(lastTimeWatered = Date()) else it 
        }
    }

    fun deleteUserPlant(plant: UserPlant) {
        _userPlants.value = _userPlants.value.filter { it.id != plant.id }
    }

    fun updatePlantCategory(plant: UserPlant, newCategory: String) {
        val categoryToSet = if (newCategory == "Unassigned") "" else newCategory
        _userPlants.value = _userPlants.value.map {
            if (it.id == plant.id) it.copy(category = categoryToSet) else it
        }
    }

    val overallHealthPercentage: StateFlow<Int> =
        _userPlants.map { plants ->

            if (plants.isEmpty()) return@map 100

            val totalHealth = plants.sumOf { it.healthStatus }
            val maxHealth = plants.size * 5

            ((totalHealth.toFloat() / maxHealth) * 100).toInt()

        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            100
        )
}