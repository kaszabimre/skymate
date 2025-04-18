@file:Suppress("MagicNumber")
package io.kaszabimre.skymate.util
import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.compose.LocalPlatformContext
import coil3.memory.MemoryCache
import coil3.request.crossfade

@Composable
fun SkymateImageLoader(): ImageLoader {
    val context = LocalPlatformContext.current
    return ImageLoader(context).newBuilder()
        .crossfade(true)
        .components {
        }
        .memoryCache {
            MemoryCache.Builder()
                .maxSizePercent(context = context, percent = 0.25)
                .build()
        }
        .build()
}

@Suppress("CyclomaticComplexMethod")
fun getWeatherIconUrl(code: Int, showLarge: Boolean = false): String {
    val icon = when (code) {
        0 -> "01d" // Clear sky
        1 -> "02d" // Mainly clear
        2 -> "03d" // Partly cloudy
        3 -> "04d" // Overcast

        45, 48 -> "50d" // Fog, Rime fog

        51, 53, 55 -> "09d" // Drizzle: light, moderate, dense
        56, 57 -> "09d" // Freezing drizzle

        61, 63, 64, 65 -> "10d" // Rain: slight, moderate, heavy
        66, 67 -> "13d" // Freezing rain: light, heavy

        71, 73, 75 -> "13d" // Snow fall: slight, moderate, heavy
        77 -> "13d" // Snow grains

        80, 81, 82 -> "09d" // Rain showers: slight, moderate, violent
        85, 86 -> "13d" // Snow showers

        95 -> "11d" // Thunderstorm: slight or moderate
        96, 99 -> "11d" // Thunderstorm with hail

        else -> "01d" // Default fallback: clear sky
    }

    val size = if (showLarge) "4x" else "2x"
    return "https://openweathermap.org/img/wn/$icon@$size.png"
}
