package io.kaszabimre.skymate.viewmodel

import app.cash.turbine.test
import io.kaszabimre.skymate.domain.WeatherAction
import io.kaszabimre.skymate.domain.WeatherStore
import io.kaszabimre.skymate.model.City
import io.kaszabimre.skymate.model.Weather
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DetailsViewModelTest {

    private val store: WeatherStore = mockk()
    private val action: WeatherAction = mockk(relaxed = true)
    private val weather: Weather = Weather("Budapest", "Hungary", 10.0, 1, 10.0, 10.0)
    private lateinit var viewModel: DetailsViewModel

    @BeforeTest
    fun setup() {
        coEvery { store.selectedWeather() } returns flowOf(weather)
        coEvery { store.searchResults() } returns flowOf(
            listOf(
                City(
                    latitude = 47.497912,
                    longitude = 19.040235,
                    name = "Budapest",
                    country = "Hungary"
                )
            )
        )
        viewModel = DetailsViewModel(action, store)
    }

    @Test
    fun `weather flow emits selected weather from store`() = runTest {
        viewModel.weather.test {
            assertEquals(weather, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { store.selectedWeather() }
    }
}
