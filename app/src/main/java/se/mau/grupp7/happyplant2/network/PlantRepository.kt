package se.mau.grupp7.happyplant2.network

import se.mau.grupp7.happyplant2.BuildConfig
import se.mau.grupp7.happyplant2.model.ApiResponse
import se.mau.grupp7.happyplant2.model.SpeciesDetails

class PlantRepository {

    private val api = ApiClient.api

    suspend fun getSpecies(query: String): ApiResponse {
        return api.getSpeciesList(apiKey = BuildConfig.API_KEY, query = query)
    }

    suspend fun getSpeciesDetails(id: Int): SpeciesDetails {
        return api.getSpeciesDetails(id = id, apiKey = BuildConfig.API_KEY)
    }
}
