@file:Suppress("MissingPackageDeclaration", "FunctionNaming")

import androidx.compose.ui.window.ComposeUIViewController
import io.kaszabimre.skymate.navigation.Navigation
import io.kaszabimre.skymate.permission.LocationService

fun MainViewController() = ComposeUIViewController {
    val locationService = LocationService()
    Navigation(locationService = locationService)
}
