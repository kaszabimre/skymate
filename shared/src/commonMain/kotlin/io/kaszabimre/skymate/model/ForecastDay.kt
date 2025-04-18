package io.kaszabimre.skymate.model

import io.kaszabimre.skymate.util.getWeatherCondition
import io.kaszabimre.skymate.util.getWeatherIconUrl

data class ForecastDay(
    val date: String,
    val minTemperature: Double,
    val maxTemperature: Double,
    val weatherCode: Int,
    val iconUrl: String = getWeatherIconUrl(weatherCode),
    val weatherCondition: String = getWeatherCondition(weatherCode),
)
