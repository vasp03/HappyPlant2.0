package se.mau.grupp7.happyplant2.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import se.mau.grupp7.happyplant2.model.PlantResponse
import se.mau.grupp7.happyplant2.model.DiseaseResponse
import se.mau.grupp7.happyplant2.model.SpeciesDetails

interface PerenualAPI {
    @GET("v2/species-list")
    suspend fun getSpeciesList(
        @Query("key") apiKey: String,
        @Query("q") query: String
    ): PlantResponse

    @GET("v2/species/details/{id}")
    suspend fun getSpeciesDetails(
        @Path("id") id: Int,
        @Query("key") apiKey: String
    ): SpeciesDetails

    @GET("pest-disease-list")
    suspend fun getPestDiseaseList(
        @Query("key") apiKey: String
    ): DiseaseResponse
}