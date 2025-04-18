package io.kaszabimre.skymate.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    @SerialName("daily")
    val daily: DailyForecast
)

@Serializable
data class DailyForecast(
    @SerialName("time")
    val dates: List<String>,

    @SerialName("temperature_2m_max")
    val maxTemperatures: List<Double>,

    @SerialName("temperature_2m_min")
    val minTemperatures: List<Double>,

    @SerialName("weathercode")
    val weatherCodes: List<Int>
)
