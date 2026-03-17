package se.mau.grupp7.happyplant2.network

import se.mau.grupp7.happyplant2.BuildConfig
import se.mau.grupp7.happyplant2.model.DefectList
import se.mau.grupp7.happyplant2.model.PestDisease
import se.mau.grupp7.happyplant2.model.PlantDetails
import se.mau.grupp7.happyplant2.model.UserPlant
import se.mau.grupp7.happyplant2.model.WaterAmount
import java.util.Date

const val MILLISECOND_CONVERSION = 86400000L
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
        return UserPlant(
            name = plantDetails.common_name,
            description = plantDetails.scientific_name,
            imageURL = plantDetails.imageUrl,
            wateringIntervalMin = 5,
            wateringIntervalMax = 7,
            wateringAmount = WaterAmount.RARELY,
            lastTimeWatered = Date(
                System.currentTimeMillis() -
                        (daysAgo * MILLISECOND_CONVERSION)
            ),
            family = plantDetails.family,
            sunlight = "Unknown",
            wateringNeeds = "average",
            healthStatus = 5,
            defectId = DefectList.NONE.id
        )
    }

    fun rankPlants(
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
