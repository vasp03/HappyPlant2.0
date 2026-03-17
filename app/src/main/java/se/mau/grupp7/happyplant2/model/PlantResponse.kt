package se.mau.grupp7.happyplant2.model


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

