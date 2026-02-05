package se.mau.grupp7.happyplant2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
                    plantDetails.common_name,
                    plantDetails.scientific_name,
                    plantDetails.imageUrl,
                    intervalDays,
                    waterAmount,
                    Date()
                )
                _userPlants.value = _userPlants.value + newPlant
            } catch (e: Exception) {
                onError()
            }
        }
    }

    fun waterUserPlant(plant: UserPlant) {
        val updatedPlant = plant.copy(lastTimeWatered = Date())
        _userPlants.value = _userPlants.value.map { if (it.name == plant.name) updatedPlant else it }
    }

    fun deleteUserPlant(plant: UserPlant) {
        _userPlants.value = _userPlants.value - plant
    }
}
