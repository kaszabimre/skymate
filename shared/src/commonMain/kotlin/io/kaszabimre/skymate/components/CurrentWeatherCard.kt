package io.kaszabimre.skymate.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.kaszabimre.skymate.model.Weather
import io.kaszabimre.skymate.util.SkymateImageLoader
import org.jetbrains.compose.resources.stringResource
import skymate.shared.generated.resources.Res
import skymate.shared.generated.resources.condition
import skymate.shared.generated.resources.humidity
import skymate.shared.generated.resources.weather_icon
import skymate.shared.generated.resources.wind_speed

@Composable
fun CurrentWeatherCard(weather: Weather) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("${weather.city}, ${weather.country}", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
            Text("${weather.temperature}°C", style = MaterialTheme.typography.displaySmall)
            Spacer(Modifier.height(12.dp))

            AsyncImage(
                model = weather.smallIconUrl,
                imageLoader = SkymateImageLoader(),
                contentDescription = stringResource(Res.string.weather_icon),
                modifier = Modifier.size(72.dp)
            )

            Spacer(Modifier.height(12.dp))

            InfoRow(stringResource(Res.string.condition), weather.weatherCondition)
            InfoRow(stringResource(Res.string.wind_speed), "${weather.windSpeed} km/h")
            InfoRow(stringResource(Res.string.humidity), "${weather.humidity.toInt()}%")
        }
    }
}
