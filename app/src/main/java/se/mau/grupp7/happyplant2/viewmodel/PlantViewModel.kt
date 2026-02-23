package se.mau.grupp7.happyplant2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import se.mau.grupp7.happyplant2.local.LocalPlantRepository
import se.mau.grupp7.happyplant2.model.*
import se.mau.grupp7.happyplant2.network.PlantRepository
import java.io.IOException
import java.util.Date


private const val MILLISECOND_CONVERSION = 86400000L
private const val MAX_HEALTH = 5
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
            .map { plants ->
                plants
                    .map { it.category.trim() }
                    .filter { it.isNotBlank() }
                    .distinct()
                    .sorted()
            }
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
            } catch (e: Exception) {
                _diseaseList.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun rankPlants(
        plants: List<PlantDetails>,
        query: String
    ): List<PlantDetails> {

        val q = query.trim().lowercase()
        if (q.isBlank()) return plants

        fun score(p: PlantDetails): Int {
            val cn = p.common_name.lowercase()
            val sn = p.scientific_name.lowercase()
            val genus = p.genus.lowercase()

            return when {
                cn.startsWith(q) -> 0
                sn.startsWith(q) -> 0
                cn.contains(q) -> 1
                sn.contains(q) -> 1
                genus.startsWith(q) -> 2
                else -> 10
            }
        }

        return plants
            .map { it to score(it) }
            .filter { it.second < 10 }
            .sortedBy { it.second }
            .map { it.first }    }

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

            val newHealth =
                (MAX_HEALTH + defect.healthImpact)
                    .coerceIn(0, MAX_HEALTH)

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
            val idSet = ids.toSet()

            userPlants.value
                .asSequence()
                .filter { it.id in idSet }
                .forEach { plant ->
                    localRepository.update(
                        plant.copy(lastTimeWatered = now)
                    )
                }
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

    private fun boundedLevenshtein(a: String, b: String, max: Int): Int {
        val s = a.lowercase()
        val t = b.lowercase()

        val n = s.length
        val m = t.length

        if (kotlin.math.abs(n - m) > max) return max + 1
        if (n == 0) return m
        if (m == 0) return n

        var prev = IntArray(m + 1) { it }
        var curr = IntArray(m + 1)

        for (i in 1..n) {
            curr[0] = i
            var rowMin = curr[0]
            val sc = s[i - 1]

            for (j in 1..m) {
                val cost = if (sc == t[j - 1]) 0 else 1
                val del = prev[j] + 1
                val ins = curr[j - 1] + 1
                val sub = prev[j - 1] + cost
                val v = minOf(del, ins, sub)
                curr[j] = v
                if (v < rowMin) rowMin = v
            }

            if (rowMin > max) return max + 1

            val tmp = prev
            prev = curr
            curr = tmp
        }

        return prev[m]
    }

    private fun suggestQuery(
        query: String,
        candidates: List<String>,
        maxDistance: Int = 2
    ): List<String> {

        val q = query.trim().lowercase()
        if (q.length < 3) return emptyList()

        return candidates
            .asSequence()
            .map { it to boundedLevenshtein(q, it, maxDistance) }
            .filter { (_, distance) -> distance <= maxDistance }
            .sortedBy { it.second }
            .take(5)
            .map { it.first }
            .toList()
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
