package se.mau.grupp7.happyplant2.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import se.mau.grupp7.happyplant2.model.ApiResponse
import se.mau.grupp7.happyplant2.model.PestDiseaseResponse
import se.mau.grupp7.happyplant2.model.SpeciesDetails

interface PerenualAPI {
    @GET("species-list")
    suspend fun getSpeciesList(
        @Query("key") apiKey: String,
        @Query("q") query: String
    ): ApiResponse

    @GET("species/details/{id}")
    suspend fun getSpeciesDetails(
        @Path("id") id: Int,
        @Query("key") apiKey: String
    ): SpeciesDetails

    @GET("https://perenual.com/api/pest-disease-list")
    suspend fun getPestDiseaseList(
        @Query("key") apiKey: String
    ): PestDiseaseResponse
}