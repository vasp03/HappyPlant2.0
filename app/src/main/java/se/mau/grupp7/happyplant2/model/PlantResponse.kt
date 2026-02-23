package se.mau.grupp7.happyplant2.model

import com.google.gson.annotations.SerializedName

data class PlantResponse(
    val data: List<FlowerTypes>
)

data class FlowerTypes(
    val id: Int = 0,
    val common_name: String = "",
    val scientific_name: List<String> = emptyList(),
    val other_name: List<String> = emptyList(),
    val family: String? = null,
    val hybrid: String? = null,
    val authority: String? = null,
    val subspecies: String? = null,
    val cultivar: String? = null,
    val variety: String? = null,
    val species_epithet: String? = null,
    val genus: String? = null,
    val default_image: DefaultImage? = null
)

data class DefaultImage(
    val license: Int = 0,
    val license_name: String = "",
    val license_url: String = "",
    val original_url: String = "",
    val regular_url: String = "",
    val medium_url: String = "",
    val small_url: String = "",
    val thumbnail: String = ""
)

data class SpeciesDetails(
    val watering: String = "Average",
    @SerializedName("sunlight")
    val sunlight: List<String> = emptyList(),
    @SerializedName("watering_general_benchmark")
    val wateringGeneralBenchmark: Benchmark? = null,
    val growth: Growth? = null
)

data class Benchmark(
    val value: String? = null,
    val unit: String? = null
)

data class Growth(
    @SerializedName("precipitation_minimum")
    val precipitationMinimum: Measure? = null
)

data class Measure(
    val mm: Int? = null,
    val cm: Int? = null,
    val inches: Int? = null
)
