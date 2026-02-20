package se.mau.grupp7.happyplant2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import se.mau.grupp7.happyplant2.local.LocalPlantRepository
import se.mau.grupp7.happyplant2.model.*
import se.mau.grupp7.happyplant2.network.PlantRepository
import java.util.Date


private const val MILLISECOND_CONVERSION = 86400000L
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
        if(q.isBlank()) {
            _flowerList.value = emptyList()
            _suggestions .value = emptyList()
            return
        }
        Log.d("HP_SEARCH", "getFlowers('$q')")

        viewModelScope.launch {
            try {
                val response = remoteRepository.getSpecies(query)
                val mapped = response.data.map { ft ->
                    PlantDetails(
                        id = ft.id,
                        common_name = ft.common_name,
                        scientific_name = ft.scientific_name.joinToString(", "),
                        genus = ft.genus ?: "",
                        family = ft.family ?: "",
                        imageUrl = ft.default_image?.regular_url ?: ""
                    )
                }
                Log.d("HP_SEARCH", "API returned ${response.data.size} items for '$q'")


                val ranked = rankPlants(mapped, q)
                _flowerList.value = ranked
                Log.d("HP_SEARCH", "ranked size = ${ranked.size}")


                _suggestions.value = if(ranked.isEmpty()) {
                    suggestQuery(q, popularPlants, maxDistance = 2)
                } else{
                    emptyList()
                }
            } catch (e: Exception) {
                Log.e("HP_SEARCH", "Search failed for query='$q'", e)
                _flowerList.value = emptyList()
                _suggestions.value = emptyList()
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
            .sortedBy { score(it) }
            .filter { score(it) < 10 }
    }

    fun addPlantToUserCollection(
        plantDetails: PlantDetails,
        daysAgo: Int,
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
                    lastTimeWatered = Date(System.currentTimeMillis() - (daysAgo * MILLISECOND_CONVERSION)),
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
            .map { it to boundedLevenshtein(q, it, maxDistance) }
            .filter { it.second <= maxDistance }
            .sortedBy { it.second }
            .take(5)
            .map { it.first }
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
}
