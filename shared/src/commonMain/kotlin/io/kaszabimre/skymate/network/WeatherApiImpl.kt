package io.kaszabimre.skymate.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

private const val COUNT_LIMIT = 5
class WeatherApiImpl(private val httpClient: HttpClient) : WeatherApi {

    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): CurrentWeatherResponse =
        withContext(Dispatchers.IO) {
            httpClient.get(OPEN_METEO_BASE_URL) {
                parameter("latitude", latitude)
                parameter("longitude", longitude)
                parameter("current", "temperature_2m,weathercode,wind_speed_10m,relative_humidity_2m")
                parameter("timezone", "auto")
            }.body()
        }

    override suspend fun getFiveDayForecast(latitude: Double, longitude: Double): ForecastResponse =
        withContext(Dispatchers.IO) {
            httpClient.get(OPEN_METEO_BASE_URL) {
                parameter("latitude", latitude)
                parameter("longitude", longitude)
                parameter("daily", "temperature_2m_max,temperature_2m_min,weathercode")
                parameter("timezone", "auto")
                parameter("forecast_days", COUNT_LIMIT)
            }.body()
        }

    override suspend fun searchCity(query: String): List<CityResult> =
        withContext(Dispatchers.IO) {
            httpClient.get(GEOCODING_BASE_URL) {
                parameter("name", query)
                parameter("count", COUNT_LIMIT)
                parameter("language", "en")
                parameter("format", "json")
            }.body<SearchResponse>().results ?: emptyList()
        }

    companion object {
        private const val OPEN_METEO_BASE_URL = "https://api.open-meteo.com/v1/forecast"
        private const val GEOCODING_BASE_URL = "https://geocoding-api.open-meteo.com/v1/search"
    }
}
