package io.kaszabimre.skymate.domain

import io.kaszabimre.skymate.model.City
import io.kaszabimre.skymate.permission.CurrentLocation

interface WeatherAction {
    suspend fun fetchSelectedWeather(city: City)
    suspend fun fetchForecast(city: City)
    suspend fun searchCity(query: String)
    suspend fun fetchCurrentWeatherByCoordinates(currentLocation: CurrentLocation)
    suspend fun fetchForecastByCoordinates(latitude: Double, longitude: Double)
}
