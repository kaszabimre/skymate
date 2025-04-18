package io.kaszabimre.skymate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.kaszabimre.skymate.domain.WeatherAction
import io.kaszabimre.skymate.domain.WeatherService
import io.kaszabimre.skymate.domain.WeatherStore
import io.kaszabimre.skymate.model.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val action: WeatherAction,
    private val store: WeatherStore
) : ViewModel() {

    val weather: Flow<Weather?> = store.selectedWeather()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val city = store.searchResults().firstOrNull()
                    ?.firstOrNull { it.longitude == longitude && it.latitude == latitude } ?: return@launch
                action.fetchSelectedWeather(city)
            } catch (@Suppress("SwallowedException", "TooGenericExceptionCaught") e: Exception) {
                _error.value = "Failed to load weather details"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
