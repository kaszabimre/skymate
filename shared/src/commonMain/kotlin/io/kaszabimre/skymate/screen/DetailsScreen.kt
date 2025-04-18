package io.kaszabimre.skymate.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import io.kaszabimre.skymate.util.SkymateImageLoader
import io.kaszabimre.skymate.util.koinViewModel
import io.kaszabimre.skymate.viewmodel.DetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navHostController: NavHostController,
    viewModel: DetailsViewModel = koinViewModel()
) {
    val entry = navHostController.currentBackStackEntry
    val lat = entry?.arguments?.getString("lat")?.toDoubleOrNull()
    val lon = entry?.arguments?.getString("lon")?.toDoubleOrNull()

    val weather by viewModel.weather.collectAsState(null)
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(lat, lon) {
        if (lat != null && lon != null) {
            viewModel.loadWeather(lat, lon)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Weather Details") },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        when {
            isLoading || weather == null && error == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(error!!, color = MaterialTheme.colorScheme.error)
                }
            }

            else -> {
                val currentWeather = weather ?: return@Scaffold
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${currentWeather.city}, ${weather!!.country}",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(Modifier.height(24.dp))

                    AsyncImage(
                        model = currentWeather.largeIconUrl,
                        imageLoader = SkymateImageLoader(),
                        contentDescription = "Weather Icon",
                        modifier = Modifier.size(128.dp)
                    )
                    Spacer(Modifier.height(24.dp))

                    Text(
                        text = "${weather!!.temperature}°C",
                        style = MaterialTheme.typography.displayLarge
                    )
                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = weather!!.weatherCondition,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(24.dp))

                    InfoRow("Wind Speed", "${currentWeather.windSpeed} km/h")
                    InfoRow("Humidity", "${currentWeather.humidity.toInt()}%")
                }
            }
        }
    }
}
