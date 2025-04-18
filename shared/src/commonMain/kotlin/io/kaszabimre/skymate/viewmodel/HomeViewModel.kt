package io.kaszabimre.skymate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.kaszabimre.skymate.domain.WeatherService
import io.kaszabimre.skymate.model.City
import io.kaszabimre.skymate.model.Forecast
import io.kaszabimre.skymate.model.Weather
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
    private val service: WeatherService
) : ViewModel() {

    private val _isDropdownExpanded = MutableStateFlow(false)
    val isDropdownExpanded: StateFlow<Boolean> = _isDropdownExpanded.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        _searchQuery
            .debounce(DELAY)
            .distinctUntilChanged()
            .onEach { query ->
                if (query.length >= CHAR_LIMIT) {
                    service.searchCity(query)
                }
            }
            .launchIn(viewModelScope)
    }

    val uiState = combine(
        service.currentWeather(),
        service.forecast(),
        service.isLoading(),
        service.error(),
        service.searchResults(),
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
            service.fetchCurrentWeather(city)
            service.fetchForecast(city)
            _searchQuery.value = city.name
            _isDropdownExpanded.value = false
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setDropdownExpanded(expanded: Boolean) {
        _isDropdownExpanded.value = expanded
    }
}

data class HomeUiState(
    val currentWeather: Weather? = null,
    val forecast: Forecast? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchResults: List<City> = emptyList()
)
