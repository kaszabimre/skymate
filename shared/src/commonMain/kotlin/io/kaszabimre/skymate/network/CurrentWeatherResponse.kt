package io.kaszabimre.skymate.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherResponse(
    @SerialName("current")
    val currentWeather: CurrentWeather
)

@Serializable
data class CurrentWeather(
    @SerialName("temperature_2m")
    val temperature: Double,

    @SerialName("wind_speed_10m")
    val windSpeed: Double,

    @SerialName("weathercode")
    val weatherCode: Int,

    @SerialName("relative_humidity_2m")
    val humidity: Double? = null
)
