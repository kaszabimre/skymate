package io.kaszabimre.skymate.permission

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

data class CurrentLocation(
    val latitude: Double,
    val longitude: Double,
    val cityName: String = "",
    val countryName: String = ""
)

expect class LocationService {
    val showEducationalUI: Flow<Unit>
    val showLocationDisabledInfo: Flow<Unit>

    suspend fun askLocationPermission(): Boolean
    suspend fun getCurrentLocation(): CurrentLocation?
    fun goToSettings()
    fun launchPermissionRequest()
}

abstract class BaseLocationService {
    protected val showEducationalUISharedFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val showEducationalUI: Flow<Unit> = showEducationalUISharedFlow

    protected val showLocationDisabledInfoSharedFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val showLocationDisabledInfo: Flow<Unit> = showLocationDisabledInfoSharedFlow
}
