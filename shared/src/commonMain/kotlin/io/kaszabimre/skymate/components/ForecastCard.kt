package io.kaszabimre.skymate.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.kaszabimre.skymate.model.ForecastDay
import io.kaszabimre.skymate.util.SkymateImageLoader
import io.kaszabimre.skymate.util.formatDate
import org.jetbrains.compose.resources.stringResource
import skymate.shared.generated.resources.Res
import skymate.shared.generated.resources.forecast_icon
import skymate.shared.generated.resources.high
import skymate.shared.generated.resources.low

@Composable
fun ForecastCard(forecast: List<ForecastDay>) {
    LazyRow(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
        items(forecast) { day ->
            Card(
                modifier = Modifier.padding(8.dp).width(140.dp),
                elevation = CardDefaults.cardElevation()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(formatDate(day.date), style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(8.dp))
                    AsyncImage(
                        model = day.iconUrl,
                        imageLoader = SkymateImageLoader(),
                        contentDescription = stringResource(Res.string.forecast_icon),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    InfoRow(stringResource(Res.string.high), "${day.maxTemperature}°C")
                    InfoRow(stringResource(Res.string.low), "${day.minTemperature}°C")
                    InfoRow(value = day.weatherCondition)
                }
            }
        }
    }
}
