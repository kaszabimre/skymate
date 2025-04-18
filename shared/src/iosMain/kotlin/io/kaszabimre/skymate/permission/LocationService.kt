package io.kaszabimre.skymate.permission

import io.kaszabimre.skymate.logger.logger
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

actual class LocationService : BaseLocationService() {
    actual suspend fun askLocationPermission(): Boolean = true

    @Suppress("MagicNumber")
    // TODO
    actual suspend fun getCurrentLocation(): CurrentLocation? = CurrentLocation(
        latitude = 47.497912,
        longitude = 19.040235,
        cityName = "Budapest",
        countryName = "Hungary"
    )

    actual fun launchPermissionRequest() {
        // TODO
    }

    actual fun goToSettings() {
        NSURL.URLWithString(UIApplicationOpenSettingsURLString)?.let {
            UIApplication.sharedApplication.openURL(it, emptyMap<Any?, Any>()) { success ->
                if (!success) logger().d("Failed to open settings URL: $it")
            }
        }
    }
}
