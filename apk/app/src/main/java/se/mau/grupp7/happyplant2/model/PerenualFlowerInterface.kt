package se.mau.grupp7.happyplant2.model

import retrofit2.http.GET
import retrofit2.http.Query

interface PerenualFlowerInterface {
    @GET("getFlowerTypes")
    suspend fun getFlowerTypes(@Query("q") q: String): Array<FlowerTypes>
}

data class FlowerTypes(
    val id: Int = 0,
    val common_name: String,
    val scientific_name: Array<String>,
    val other_name: Array<String>,
    val family: String,
    val hybrid: String,
    val authority: String,
    val subspecies: String,
    val cultivar: String,
    val variety: String,
    val species_epithet: String,
    val genus: String,
    val license: Int,
    val license_name: String,
    val license_url: String,
    val original_url: String,
    val regular_url: String,
    val medium_url: String,
    val small_url: String,
    val thumbnail: String
)
