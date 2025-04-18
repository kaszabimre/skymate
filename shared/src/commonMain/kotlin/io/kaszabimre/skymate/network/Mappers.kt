package io.kaszabimre.skymate.network

import io.kaszabimre.skymate.model.City
import io.kaszabimre.skymate.model.Forecast
import io.kaszabimre.skymate.model.ForecastDay
import io.kaszabimre.skymate.model.Weather

fun CityResult.toDomain(): City = City(
    name = name,
    country = country,
    latitude = latitude,
    longitude = longitude
)

fun CurrentWeatherResponse.toDomain(city: City): Weather = Weather(
    city = city.name,
    country = city.country,
    temperature = currentWeather.temperature,
    weatherCode = currentWeather.weatherCode,
    windSpeed = currentWeather.windSpeed,
    humidity = currentWeather.humidity?.toDouble() ?: 0.0
)

fun ForecastResponse.toDomain(): Forecast = Forecast(
    days = daily.dates.indices.map { index ->
        ForecastDay(
            date = daily.dates[index],
            minTemperature = daily.minTemperatures[index],
            maxTemperature = daily.maxTemperatures[index],
            weatherCode = daily.weatherCodes[index]
        )
    }
)

fun GeocodingResult.toDomain(): City = City(
    name = name,
    country = country,
    latitude = latitude,
    longitude = longitude
)

fun ReverseGeocodingResponse.toDomain(): City? =
    results
        ?.firstOrNull()
        ?.toDomain()
