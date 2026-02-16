package se.mau.grupp7.happyplant2.model

import java.io.Serializable

data class PlantDetails(
    val id: Int,
    val common_name: String,
    val scientific_name: String,
    val genus: String,
    val family: String,
    val imageUrl: String
) : Serializable
