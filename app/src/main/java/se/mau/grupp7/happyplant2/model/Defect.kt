package se.mau.grupp7.happyplant2.model

import kotlinx.serialization.Serializable

@Serializable
data class Defect(
    val id: String,
    val displayName: String,
    val category: String,
    val subCategory: String,
    val healthImpact: Int
)