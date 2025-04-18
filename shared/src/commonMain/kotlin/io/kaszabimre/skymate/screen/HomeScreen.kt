package io.kaszabimre.skymate.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import io.kaszabimre.skymate.model.City
import io.kaszabimre.skymate.model.ForecastDay
import io.kaszabimre.skymate.model.Weather
import io.kaszabimre.skymate.navigation.SkymateScreens
import io.kaszabimre.skymate.util.SkymateImageLoader
import io.kaszabimre.skymate.util.formatDate
import io.kaszabimre.skymate.util.koinViewModel
import io.kaszabimre.skymate.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    navHostController: NavHostController
) {
    val focusManager = LocalFocusManager.current
    val state by viewModel.uiState.collectAsState()
    val isDropdownExpanded by viewModel.isDropdownExpanded.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(title = { Text("SkyMate") })
                SearchBarWithDropdown(
                    query = searchQuery,
                    onQueryChanged = { viewModel.updateSearchQuery(it) },
                    cities = state.searchResults,
                    expanded = isDropdownExpanded,
                    onExpandedChange = { viewModel.setDropdownExpanded(it) },
                    onCitySelected = {
                        focusManager.clearFocus(force = true)
                        viewModel.onCitySelected(it)
                        navHostController.navigate(
                            SkymateScreens.DetailsScreen.createRoute(it.latitude, it.longitude)
                        )
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(16.dp))

            when {
                state.isLoading -> LoadingIndicator()
                state.error != null -> ErrorText(state.error)
                else -> {
                    state.currentWeather?.let { CurrentWeatherSection(it) }
                    state.forecast?.let { ForecastSection(it.days) }
                }
            }
        }
    }
}

@Composable
fun SearchBarWithDropdown(
    query: String,
    onQueryChanged: (String) -> Unit,
    cities: List<City>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onCitySelected: (City) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = {
                onQueryChanged(it)
                onExpandedChange(true)
            },
            label = { Text("Search City") },
            trailingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        if (expanded && cities.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                cities.forEach { city ->
                    DropdownMenuItem(
                        text = { Text("${city.name}, ${city.country}") },
                        onClick = {
                            onCitySelected(city)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorText(message: String?) {
    message?.let {
        Text(
            text = it,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun CurrentWeatherSection(weather: Weather, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                onClick()
            },
        elevation = CardDefaults.cardElevation()
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${weather.city}, ${weather.country}",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(Modifier.height(8.dp))

            Text(
                text = "${weather.temperature}°C",
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(Modifier.height(12.dp))

            AsyncImage(
                model = weather.smallIconUrl,
                imageLoader = SkymateImageLoader(),
                contentDescription = "Weather Icon",
                modifier = Modifier.size(72.dp)
            )

            Spacer(Modifier.height(12.dp))

            InfoRow(label = "Condition", value = weather.weatherCondition)
            InfoRow(label = "Wind", value = "${weather.windSpeed} km/h")
            InfoRow(label = "Humidity", value = "${weather.humidity.toInt()}%")
        }
    }
}

@Composable
fun ForecastSection(forecastDays: List<ForecastDay>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        items(forecastDays) { day ->
            Card(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .width(140.dp),
                elevation = CardDefaults.cardElevation()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = formatDate(day.date),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(Modifier.height(8.dp))

                    AsyncImage(
                        model = day.iconUrl,
                        imageLoader = SkymateImageLoader(),
                        contentDescription = "Forecast Icon",
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(Modifier.height(8.dp))

                    InfoRow("High", "${day.maxTemperature}°C")
                    InfoRow("Low", "${day.minTemperature}°C")
                    InfoRow(value = day.weatherCondition)
                }
            }
        }
    }
}

@Composable
internal fun InfoRow(label: String? = null, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        label?.let {
            Text(text = "$label:", style = MaterialTheme.typography.bodySmall)
        }
        Text(text = value, style = MaterialTheme.typography.bodySmall)
    }
}
