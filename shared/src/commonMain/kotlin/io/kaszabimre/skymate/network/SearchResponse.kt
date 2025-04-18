package io.kaszabimre.skymate.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerialName("results")
    val results: List<CityResult>?
)

@Serializable
data class CityResult(
    @SerialName("name")
    val name: String,

    @SerialName("country")
    val country: String,

    @SerialName("latitude")
    val latitude: Double,

    @SerialName("longitude")
    val longitude: Double
)
