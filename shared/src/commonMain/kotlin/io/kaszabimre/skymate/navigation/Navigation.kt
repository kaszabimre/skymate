package io.kaszabimre.skymate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.kaszabimre.skymate.permission.LocationService
import io.kaszabimre.skymate.screen.DetailsScreen
import io.kaszabimre.skymate.screen.HomeScreen
import io.kaszabimre.skymate.theme.MyApplicationTheme

@Composable
fun Navigation(locationService: LocationService) {
    val navController = rememberNavController()
    MyApplicationTheme(
        content = {
            NavHost(
                navController = navController,
                startDestination = SkymateScreens.HomeScreen.route,
            ) {
                composable(SkymateScreens.HomeScreen.route) {
                    HomeScreen(navHostController = navController, locationService = locationService)
                }
                composable(route = SkymateScreens.DetailsScreen.route) {
                    DetailsScreen(navHostController = navController)
                }
            }
        }
    )
}
