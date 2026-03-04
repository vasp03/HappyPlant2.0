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
    private val _diseaseList = MutableStateFlow<List<PestDisease>>(emptyList())
    val diseaseList: StateFlow<List<PestDisease>> = _diseaseList
    private var diseasesLoaded = false
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val userPlants: StateFlow<List<UserPlant>> =
        localRepository.plants
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )


    val categories: StateFlow<List<String>> =
        userPlants
            .map { plants -> calculateCategories(plants) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )
    private val popularPlants = listOf("rosa", "rose", "lavender", "monstera")
    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions: StateFlow<List<String>> = _suggestions

    fun getFlowers(query: String) {
        val q = query.trim()
        if (q.isBlank()) {
            _flowerList.value = emptyList()
            _suggestions.value = emptyList()
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val plants = remoteRepository.getRankedPlants(q)
                _flowerList.value = plants

                _suggestions.value =
                    if (plants.isEmpty()) {
                        suggestQuery(q, popularPlants, 2)
                    } else emptyList()

            } catch (_: Exception) {
                _flowerList.value = emptyList()
                _suggestions.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getDiseases() {
        if (diseasesLoaded) return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val diseases = remoteRepository.getPestDiseases()
                _diseaseList.value = diseases
                diseasesLoaded = true
            } catch (_: Exception) {
                _diseaseList.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addPlantToUserCollection(
        plantDetails: PlantDetails,
        daysAgo: Int,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val newPlant =
                    remoteRepository.createUserPlant(plantDetails, daysAgo)

                localRepository.insert(newPlant)

            } catch (_: Exception) {
                onError()
            }
        }
    }

    fun updatePlantDefect(plant: UserPlant, defect: Defect) {
        viewModelScope.launch {

            val newHealth = calculateNewHealth(defect.healthImpact)

            localRepository.update(
                plant.copy(
                    defectId = defect.id,
                    healthStatus = newHealth
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


    fun waterSelectedPlants(ids: List<String>) {
        if (ids.isEmpty()) return

        viewModelScope.launch {
            val now = Date()
            val updated = selectPlantsToWater(userPlants.value, ids, now)
            updated.forEach { localRepository.update(it) }
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

                calculateOverallHealthPercentage(plants)

            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                100
            )

    fun getPlantById(id: String?): StateFlow<UserPlant?> {
        return userPlants
            .map { list -> list.find { it.id == id } }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                null
            )
    }
}
