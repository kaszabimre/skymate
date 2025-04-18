package io.kaszabimre.skymate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.kaszabimre.skymate.domain.WeatherAction
import io.kaszabimre.skymate.domain.WeatherStore
import io.kaszabimre.skymate.model.City
import io.kaszabimre.skymate.model.Forecast
import io.kaszabimre.skymate.model.Weather
import io.kaszabimre.skymate.permission.CurrentLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val DELAY = 300L
private const val CHAR_LIMIT = 3

@OptIn(kotlinx.coroutines.FlowPreview::class)
class HomeViewModel(
    private val action: WeatherAction,
    store: WeatherStore
) : ViewModel() {

    private val _isDropdownExpanded = MutableStateFlow(false)
    val isDropdownExpanded: StateFlow<Boolean> = _isDropdownExpanded.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        combine(_searchQuery, _isDropdownExpanded)
            { query, expanded ->
                if (query.length >= CHAR_LIMIT && expanded) {
                    action.searchCity(query)
                }
            }
            .launchIn(viewModelScope)
    }

    val uiState = combine(
        store.currentWeather(),
        store.forecast(),
        store.isLoading(),
        store.error(),
        store.searchResults(),
    ) { weather, forecast, isLoading, error, searchResults ->
        HomeUiState(
            currentWeather = weather,
            forecast = forecast,
            isLoading = isLoading,
            error = error?.let { "No results" },
            searchResults = searchResults,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())

    fun onCitySelected(city: City) {
        viewModelScope.launch {
            _isDropdownExpanded.value = false
            _searchQuery.value = city.name
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setDropdownExpanded(expanded: Boolean) {
        _isDropdownExpanded.value = expanded
    }

    fun onDeviceLocation(cords: CurrentLocation) {
        viewModelScope.launch {
            action.fetchCurrentWeatherByCoordinates(cords)
            action.fetchForecastByCoordinates(cords.latitude, cords.longitude)
        }
    }
}

data class HomeUiState(
    val currentWeather: Weather? = null,
    val forecast: Forecast? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchResults: List<City> = emptyList()
)
