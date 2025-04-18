package io.kaszabimre.skymate.navigation

sealed class SkymateScreens(val route: String) {
    data object HomeScreen : SkymateScreens("home_screen")
    data object DetailsScreen : SkymateScreens("details/{location}") {
        fun createRoute(location: String) = "details/$location"
    }
}
