package io.kaszabimre.skymate.navigation

sealed class SkymateScreens(val route: String) {
    data object HomeScreen : SkymateScreens("home_screen")
    data object DetailsScreen : SkymateScreens("details/{lat}/{lon}") {
        fun createRoute(lat: Double, lon: Double) = "details/$lat/$lon"
    }
}
