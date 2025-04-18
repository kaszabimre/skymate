package io.kaszabimre.skymate.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
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
import io.kaszabimre.skymate.components.ErrorText
import io.kaszabimre.skymate.components.InfoRow
import io.kaszabimre.skymate.components.LoadingIndicator
import io.kaszabimre.skymate.util.SkymateImageLoader
import io.kaszabimre.skymate.util.koinViewModel
import io.kaszabimre.skymate.viewmodel.DetailsViewModel
import org.jetbrains.compose.resources.stringResource
import skymate.shared.generated.resources.Res
import skymate.shared.generated.resources.back
import skymate.shared.generated.resources.humidity
import skymate.shared.generated.resources.weather_details
import skymate.shared.generated.resources.weather_icon
import skymate.shared.generated.resources.wind_speed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navHostController: NavHostController,
    viewModel: DetailsViewModel = koinViewModel()
) {
    val lat = navHostController.currentBackStackEntry?.arguments?.getString("lat")?.toDoubleOrNull()
    val lon = navHostController.currentBackStackEntry?.arguments?.getString("lon")?.toDoubleOrNull()

    val weather by viewModel.weather.collectAsState(null)
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(lat, lon) {
        if (lat != null && lon != null) viewModel.loadWeather(lat, lon)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(Res.string.weather_details)) },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(Res.string.back))
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())) {
            when {
                isLoading || weather == null && error == null -> LoadingIndicator()
                error != null -> ErrorText(error)
                weather != null -> {
                    val data = weather!!
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("${data.city}, ${data.country}", style = MaterialTheme.typography.headlineLarge)
                        Spacer(Modifier.height(24.dp))
                        AsyncImage(
                            model = data.largeIconUrl,
                            imageLoader = SkymateImageLoader(),
                            contentDescription = stringResource(Res.string.weather_icon),
                            modifier = Modifier.size(128.dp)
                        )
                        Spacer(Modifier.height(24.dp))
                        Text("${data.temperature}°C", style = MaterialTheme.typography.displayLarge)
                        Spacer(Modifier.height(12.dp))
                        Text(data.weatherCondition, style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.height(24.dp))
                        InfoRow(stringResource(Res.string.wind_speed), "${data.windSpeed} km/h")
                        InfoRow(stringResource(Res.string.humidity), "${data.humidity.toInt()}%")
                    }
                }
            }
        }
    }
}
