package se.mau.grupp7.happyplant2.network

import se.mau.grupp7.happyplant2.BuildConfig
import se.mau.grupp7.happyplant2.model.DefectList
import se.mau.grupp7.happyplant2.model.PestDisease
import se.mau.grupp7.happyplant2.model.PlantDetails
import se.mau.grupp7.happyplant2.model.UserPlant
import se.mau.grupp7.happyplant2.model.WaterAmount
import java.util.Date

class PlantRepository {

    private val api = ApiClient.api

    suspend fun searchPlants(query: String): List<PlantDetails> {
        val response = api.getSpeciesList(
            apiKey = BuildConfig.API_KEY,
            query = query
        )

        return response.data.map { ft ->
            PlantDetails(
                id = ft.id,
                common_name = ft.common_name,
                scientific_name = ft.scientific_name.joinToString(", "),
                genus = ft.genus ?: "",
                family = ft.family ?: "",
                imageUrl = ft.default_image?.regular_url ?: ""
            )
        }
    }

    suspend fun getRankedPlants(query: String): List<PlantDetails> {
        val plants = searchPlants(query)
        return rankPlants(plants, query)
    }

    suspend fun getPestDiseases(): List<PestDisease> {
        return api.getPestDiseaseList(BuildConfig.API_KEY).data
    }

    suspend fun createUserPlant(
        plantDetails: PlantDetails,
        daysAgo: Int
    ): UserPlant {

        val details = api.getSpeciesDetails(
            id = plantDetails.id,
            apiKey = BuildConfig.API_KEY
        )

        val (minInterval, maxInterval) =
            parseWaterInterval(details.wateringGeneralBenchmark?.value, details.watering)

        val waterAmount = mapWaterAmount(details.watering)

        return UserPlant(
            name = plantDetails.common_name,
            description = plantDetails.scientific_name,
            imageURL = plantDetails.imageUrl,
            wateringIntervalMin = minInterval,
            wateringIntervalMax = maxInterval,
            wateringAmount = waterAmount,
            lastTimeWatered = Date(
                System.currentTimeMillis() -
                        (daysAgo * 86400000L)
            ),
            family = plantDetails.family,
            sunlight = details.sunlight.joinToString(", "),
            wateringNeeds = details.watering,
            healthStatus = 5,
            defectId = DefectList.NONE.id
        )
    }

    private fun parseWaterInterval(
        benchmark: String?,
        watering: String
    ): Pair<Int, Int> {

        val value = benchmark ?: when (watering.lowercase()) {
            "frequent" -> "2-3"
            "average" -> "5-7"
            "minimum" -> "10-14"
            "none" -> "24-30"
            else -> "5-7"
        }

        val parts = value
            .split("-")
            .mapNotNull { it.trim().toIntOrNull() }

        return when {
            parts.size >= 2 -> parts[0] to parts[1]
            parts.size == 1 -> {
                val v = parts[0]
                val spread = (v * 0.3).toInt().coerceAtLeast(1)
                (v - spread).coerceAtLeast(1) to (v + spread)
            }
            else -> 5 to 7
        }
    }

    private fun mapWaterAmount(watering: String): WaterAmount {
        return when (watering.lowercase()) {
            "frequent" -> WaterAmount.OFTEN
            "average" -> WaterAmount.RARELY
            "minimum" -> WaterAmount.CACTUS
            "none" -> WaterAmount.NEVER
            else -> WaterAmount.RARELY
        }
    }

    private fun rankPlants(
        plants: List<PlantDetails>,
        query: String
    ): List<PlantDetails> {

        val q = query.trim().lowercase()

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
            .map { it.first }
    }
}
