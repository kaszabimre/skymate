package io.kaszabimre.skymate.model

import io.kaszabimre.skymate.util.getWeatherCondition
import io.kaszabimre.skymate.util.getWeatherIconUrl

data class Weather(
    val city: String,
    val country: String,
    val temperature: Double,
    val weatherCode: Int,
    val windSpeed: Double,
    val humidity: Double,
    val iconUrl: String = getWeatherIconUrl(weatherCode),
    val weatherCondition: String = getWeatherCondition(weatherCode)
)
