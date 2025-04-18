package io.kaszabimre.skymate.domain

import co.touchlab.kermit.Logger
import io.kaszabimre.skymate.model.City
import io.kaszabimre.skymate.model.Forecast
import io.kaszabimre.skymate.model.Weather
import io.kaszabimre.skymate.network.WeatherApi
import io.kaszabimre.skymate.network.toDomain
import io.kaszabimre.skymate.permission.CurrentLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class WeatherService(
    private val api: WeatherApi,
    private val logger: Logger
) : WeatherAction, WeatherStore {

    private val _currentWeather = MutableStateFlow<Weather?>(null)
    private val _selectedWeather = MutableStateFlow<Weather?>(null)
    private val _forecast = MutableStateFlow<Forecast?>(null)
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)
    private val _searchResults = MutableStateFlow<List<City>>(emptyList())

    override suspend fun fetchSelectedWeather(city: City) = withContext(Dispatchers.Default) {
        logger.d { "Fetching current weather for: ${city.name}, ${city.country}" }
        _isLoading.value = true
        _error.value = null
        try {
            val response = api.getCurrentWeather(city.latitude, city.longitude)
            val weather = response.toDomain(city)
            _selectedWeather.value = weather
            logger.i { "Current weather fetched successfully: $weather" }
        } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
            _error.value = e.message
            logger.e(e) { "Failed to fetch current weather" }
        } finally {
            _isLoading.value = false
        }
    }

    override suspend fun fetchForecast(city: City) = withContext(Dispatchers.Default) {
        logger.d { "Fetching 5-day forecast for: ${city.name}, ${city.country}" }
        _isLoading.value = true
        _error.value = null
        try {
            val response = api.getFiveDayForecast(city.latitude, city.longitude)
            val forecast = response.toDomain()
            _forecast.value = forecast
            logger.i { "Forecast fetched successfully: $forecast" }
        } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
            _error.value = e.message
            logger.e(e) { "Failed to fetch forecast" }
        } finally {
            _isLoading.value = false
        }
    }

    override suspend fun searchCity(query: String) = withContext(Dispatchers.Default) {
        logger.d { "Searching city for query: \"$query\"" }
        _isLoading.value = true
        _error.value = null
        try {
            val results = api.searchCity(query).map { it.toDomain() }
            _searchResults.value = results
            logger.i { "City search successful: ${results.joinToString()}" }
        } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
            _error.value = e.message
            logger.e(e) { "City search failed" }
        } finally {
            _isLoading.value = false
        }
    }

    override suspend fun fetchCurrentWeatherByCoordinates(currentLocation: CurrentLocation) =
        withContext(Dispatchers.Default) {
            logger.d { "Fetching current weather for device location: $currentLocation" }
            _isLoading.value = true
            _error.value = null
            try {
                val response = api.getCurrentWeather(currentLocation.latitude, currentLocation.longitude)
                val city = City(
                    name = currentLocation.cityName.takeIf { it.isNotBlank() } ?: "My Location",
                    latitude = currentLocation.latitude,
                    longitude = currentLocation.longitude,
                    country = currentLocation.countryName
                )
                val weather = response.toDomain(city)
                _currentWeather.value = weather
                logger.i { "Device weather fetched: $weather" }
            } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
                _error.value = e.message
                logger.e(e) { "Failed device‐weather" }
            } finally {
                _isLoading.value = false
            }
        }

    override suspend fun fetchForecastByCoordinates(latitude: Double, longitude: Double) =
        withContext(Dispatchers.Default) {
            logger.d { "Fetching forecast for device location: $latitude, $longitude" }
            _isLoading.value = true
            _error.value = null
            try {
                val response = api.getFiveDayForecast(latitude, longitude)
                val forecast = response.toDomain()
                _forecast.value = forecast
                logger.i { "Device forecast fetched: $forecast" }
            } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
                _error.value = e.message
                logger.e(e) { "Failed device‐forecast" }
            } finally {
                _isLoading.value = false
            }
        }

    override fun currentWeather(): Flow<Weather?> = _currentWeather.asStateFlow()
    override fun selectedWeather(): Flow<Weather?> = _selectedWeather.asStateFlow()
    override fun forecast(): Flow<Forecast?> = _forecast.asStateFlow()
    override fun isLoading(): Flow<Boolean> = _isLoading.asStateFlow()
    override fun error(): Flow<String?> = _error.asStateFlow()
    override fun searchResults(): Flow<List<City>> = _searchResults.asStateFlow()
}
