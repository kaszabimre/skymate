package io.kaszabimre.skymate.domain

import io.kaszabimre.skymate.model.City
import io.kaszabimre.skymate.model.Forecast
import io.kaszabimre.skymate.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherStore {
    fun currentWeather(): Flow<Weather?>
    fun forecast(): Flow<Forecast?>
    fun isLoading(): Flow<Boolean>
    fun error(): Flow<String?>
    fun searchResults(): Flow<List<City>>
}
