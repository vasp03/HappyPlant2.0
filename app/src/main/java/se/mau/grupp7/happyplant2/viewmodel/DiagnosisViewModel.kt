package se.mau.grupp7.happyplant2.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import se.mau.grupp7.happyplant2.model.UserPlant
import se.mau.grupp7.happyplant2.model.diagnosis.DiagnosisEngine
import se.mau.grupp7.happyplant2.model.diagnosis.DiagnosisResult
import se.mau.grupp7.happyplant2.model.diagnosis.DiagnosisRule
import se.mau.grupp7.happyplant2.model.diagnosis.DiagnosisRulesLoader
import se.mau.grupp7.happyplant2.model.WaterAmount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import se.mau.grupp7.happyplant2.BuildConfig
import se.mau.grupp7.happyplant2.network.PerenualAPI
import se.mau.grupp7.happyplant2.network.ApiClient

data class DiagnosisUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,

    val allSymptoms: List<String> = emptyList(),
    val selectedSymptoms: Set<String> = emptySet(),

    val symptomQuery: String = "",
    val suggestedSymptoms: List<String> = emptyList(),

    val selectedPlantId: String? = null,
    val selectedPlantName: String = "No specific plant",
    val selectedPlantWateringInterval: Int? = null,
    val selectedPlantWaterAmount: WaterAmount? = null,

    val careInfoLoading: Boolean = false,
    val careInfoText: String? = null,
    val apiWateringLevel: String? = null,


    val results: List<DiagnosisResult> = emptyList()
)

class DiagnosisViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DiagnosisUiState())
    val uiState: StateFlow<DiagnosisUiState> = _uiState.asStateFlow()

    private var rules: List<DiagnosisRule> = emptyList()
    private val engine = DiagnosisEngine()


    private val perenualAPI: PerenualAPI = ApiClient.api

    private fun fetchCareInfoForPlant(plantName: String) {
        val key = BuildConfig.PERENUAL_API_KEY
        if (key.isBlank()) {
            _uiState.value = _uiState.value.copy(
                careInfoLoading = false,
                careInfoText = "Missing Perenual API key"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(careInfoLoading = true, careInfoText = null)
            try {
                val search = withContext(Dispatchers.IO) {
                    perenualAPI.getSpeciesList(apiKey = key, query = plantName)
                }
                val firstId = search.data.firstOrNull()?.id

                if (firstId == null) {
                    _uiState.value = _uiState.value.copy(
                        careInfoLoading = false,
                        careInfoText = "No care info found for \"$plantName\"."
                    )
                    return@launch
                }

                val details = withContext(Dispatchers.IO) {
                    perenualAPI.getSpeciesDetails(id = firstId, apiKey = key)
                }

                val watering = details.watering.ifBlank { "Unknown" }

                val precipitationMm = details.growth
                    ?.precipitationMinimum
                    ?.mm

                val extra = if (precipitationMm != null) {
                    " · Min precipitation: ${precipitationMm}mm"
                } else {
                    ""
                }

                _uiState.value = _uiState.value.copy(
                    careInfoLoading = false,
                    careInfoText = "Watering: $watering$extra",
                    apiWateringLevel = watering
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    careInfoLoading = false,
                    careInfoText = "Could not load care info right now..."
                )
            }
        }
    }
    fun loadRules(context: Context) {
        //Avoid reloading if already loaded
        if (rules.isNotEmpty()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val file = DiagnosisRulesLoader(context).load()
                rules = file.rules

                val symptoms = rules
                    .flatMap { it.symptoms }
                    .map { it.trim() }
                    .distinctBy { it.lowercase() }
                    .sorted()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    allSymptoms = symptoms
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load diagnosis rules."
                )
            }
        }
    }

    fun selectPlant(plant: UserPlant?) {
        _uiState.value = _uiState.value.copy(
            selectedPlantId = plant?.id,
            selectedPlantName = plant?.name ?: "No specific plant",
            selectedPlantWateringInterval = plant?.wateringInterval,
            selectedPlantWaterAmount = plant?.wateringAmount,
            careInfoLoading = false,
            careInfoText = null,
            results = emptyList())
        if (plant != null) {
            fetchCareInfoForPlant(plant.name)
        }
    }

    fun toggleSymptom(symptom: String) {
        val current = _uiState.value
        val newSelected = current.selectedSymptoms.toMutableSet().apply {
            if (contains(symptom)) remove(symptom) else add(symptom)
        }
        _uiState.value = current.copy(
            selectedSymptoms = newSelected,
            results = emptyList()
        )
    }

    fun runDiagnosis() {
        val current = _uiState.value
        val selected = current.selectedSymptoms.toList()

        val baseResults = engine.diagnose(
            rules = rules,
            selectedSymptoms = selected
        )

        val weighted = applyPlantWeighting(
            results = baseResults,
            wateringInterval = current.selectedPlantWateringInterval,
            waterAmount = current.selectedPlantWaterAmount,
            apiWateringLevel = current.apiWateringLevel
        )

        _uiState.value = current.copy(results = weighted)
    }

    fun selectPlantNameOnly(name: String) {
        _uiState.value = _uiState.value.copy(
            selectedPlantId = null,
            selectedPlantName = name,
            selectedPlantWateringInterval = null,
            selectedPlantWaterAmount = null,
            careInfoText = null,
            results = emptyList()
        )
        fetchCareInfoForPlant(name)
    }

    private fun applyPlantWeighting(
        results: List<DiagnosisResult>,
        wateringInterval: Int?,
        waterAmount: WaterAmount?,
        apiWateringLevel: String?
    ): List<DiagnosisResult> {
        if (wateringInterval == null && waterAmount == null && apiWateringLevel == null) return results

        fun bonusForRule(ruleId: String): Int {
            var bonus = 0

            if (waterAmount != null) {
                when (waterAmount) {
                    WaterAmount.CACTUS, WaterAmount.RARELY, WaterAmount.NEVER -> {
                        if (ruleId == "overwatering") bonus += 2
                        if (ruleId == "underwatering") bonus -= 1
                    }

                    WaterAmount.OFTEN -> {
                        if (ruleId == "underwatering") bonus += 2
                        if (ruleId == "overwatering") bonus -= 1
                    }
                }
            }

            if (wateringInterval != null) {
                if (wateringInterval >= 10) { //rarly needs watering
                    if (ruleId == "overwatering") bonus += 1
                }
                if (wateringInterval <= 3) { //needs watering often
                    if (ruleId == "underwatering") bonus += 1
                }
            }

            apiWateringLevel?.lowercase()?.let { level ->
                when {
                    level.contains("minimum") || level.contains("low") -> {
                        if (ruleId == "overwatering") bonus += 2
                    }
                    level.contains("frequent") || level.contains("high") -> {
                        if (ruleId == "underwatering") bonus += 2
                    }
                }
            }
            return bonus
        }

        return results
            .map { r ->
            val adjusted = (r.score + bonusForRule(r.rule.id)).coerceAtLeast(0)
            r.copy(score = adjusted)
        }
            .sortedByDescending { it.score }
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(
            selectedSymptoms = emptySet(),
            results = emptyList()
        )
    }

    fun updateSymptomQuery(query: String) {
        val current = _uiState.value
        val trimmed = query.trimStart()

        val suggestions = if (trimmed.isBlank()) {
            emptyList()
        } else {
            val q = trimmed.trim().lowercase()
            current.allSymptoms
                .filter { it.lowercase().contains(q) }
                .take(8)
        }

        _uiState.value = current.copy(
            symptomQuery = query,
            suggestedSymptoms = suggestions
        )
    }

    fun addSymptom(symptom: String) {
        val current = _uiState.value
        val newSelected = current.selectedSymptoms.toMutableSet().apply { add(symptom) }

        _uiState.value = current.copy(
            selectedSymptoms = newSelected,
            symptomQuery = "",
            suggestedSymptoms = emptyList(),
            results = emptyList()
        )
    }

}
