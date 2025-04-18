package io.kaszabimre.skymate.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import io.kaszabimre.skymate.navigation.Navigation
import io.kaszabimre.skymate.permission.LocationService
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val locationService: LocationService = LocationService(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation(locationService = locationService)
            askLocationPermission()
        }
    }

    private fun askLocationPermission() {
        lifecycleScope.launch {
            locationService.askLocationPermission()
        }
    }
}
