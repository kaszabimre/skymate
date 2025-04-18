package io.kaszabimre.skymate.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import io.kaszabimre.skymate.components.CurrentWeatherCard
import io.kaszabimre.skymate.components.ErrorText
import io.kaszabimre.skymate.components.ForecastCard
import io.kaszabimre.skymate.components.LoadingIndicator
import io.kaszabimre.skymate.components.SearchBarWithDropdown
import io.kaszabimre.skymate.navigation.SkymateScreens
import io.kaszabimre.skymate.permission.LocationService
import io.kaszabimre.skymate.util.koinViewModel
import io.kaszabimre.skymate.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import skymate.shared.generated.resources.Res
import skymate.shared.generated.resources.app_name

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    locationService: LocationService,
    navHostController: NavHostController
) {
    val focusManager = LocalFocusManager.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.uiState.collectAsState()
    val expanded by viewModel.isDropdownExpanded.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                coroutineScope.launch {
                    if (locationService.askLocationPermission()) {
                        locationService.getCurrentLocation()?.let { viewModel.onDeviceLocation(it) }
                    }
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(title = { Text(stringResource(Res.string.app_name)) })
                SearchBarWithDropdown(
                    query = query,
                    onQueryChanged = viewModel::updateSearchQuery,
                    cities = state.searchResults,
                    expanded = expanded,
                    onExpandedChange = viewModel::setDropdownExpanded,
                    onCitySelected = {
                        focusManager.clearFocus(force = true)
                        viewModel.onCitySelected(it)
                        navHostController.navigate(SkymateScreens.DetailsScreen.createRoute(it.latitude, it.longitude))
                    }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(16.dp))

            when {
                state.isLoading -> LoadingIndicator()
                state.error != null -> ErrorText(state.error)
            }

            state.currentWeather?.let { CurrentWeatherCard(it) }
            state.forecast?.let { ForecastCard(it.days) }
        }
    }
}
