package io.kaszabimre.skymate.network

interface WeatherApi {
    suspend fun getCurrentWeather(latitude: Double, longitude: Double): CurrentWeatherResponse
    suspend fun getFiveDayForecast(latitude: Double, longitude: Double): ForecastResponse
    suspend fun searchCity(query: String): List<CityResult>
}
