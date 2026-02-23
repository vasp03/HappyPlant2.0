package se.mau.grupp7.happyplant2.model

data class DiseaseResponse(
    val data: List<PestDisease>
)

data class Section(
    val subtitle: String?,
    val description: String?
)

data class DiseaseImage(
    val license: Int,
    val license_name: String,
    val license_url: String,
    val original_url: String,
    val regular_url: String,
    val medium_url: String,
    val small_url: String,
    val thumbnail: String
)

data class PestDisease(
    val id: Int,
    val common_name: String?,
    val scientific_name: String?,
    val other_name: List<String>?,
    val family: String?,
    val description: List<Section>?,
    val solution: List<Section>?,
    val host: List<String>?,
    val images: List<DiseaseImage>?,
    val symptoms: List<String>?
)