package io.kaszabimre.skymate.domain

import io.kaszabimre.skymate.model.City

interface WeatherAction {
    suspend fun fetchCurrentWeather(city: City)
    suspend fun fetchForecast(city: City)
    suspend fun searchCity(query: String)
}
