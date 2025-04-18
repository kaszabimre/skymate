package io.kaszabimre.skymate.network

import kotlinx.serialization.Serializable

@Serializable
data class ReverseGeocodingResponse(
    val results: List<GeocodingResult>?
)

@Serializable
data class GeocodingResult(
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double
)
