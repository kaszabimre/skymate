package io.kaszabimre.skymate.permission

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import io.kaszabimre.skymate.logger.logger
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.tasks.await

actual class LocationService(private val activity: ComponentActivity) : BaseLocationService() {
    private val permissionResult = CompletableDeferred<Boolean>()
    private val requestPermissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionResult.complete(granted)
        if (!granted) {
            showLocationDisabledInfoSharedFlow.tryEmit(Unit)
        }
    }

    @Suppress("ReturnCount")
    actual suspend fun askLocationPermission(): Boolean {
        // Already granted?
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        // Show rationale?
        if (activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            showEducationalUISharedFlow.tryEmit(Unit)
            return false
        }
        // Fire request
        launchPermissionRequest()
        return permissionResult.await()
    }

    actual fun launchPermissionRequest() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    actual fun goToSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", activity.packageName, null)
        }
        activity.startActivity(intent)
    }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    actual suspend fun getCurrentLocation(): CurrentLocation? = try {
        val client = LocationServices.getFusedLocationProviderClient(activity)
        val loc = client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
        loc?.let { CurrentLocation(it.latitude, it.longitude) }
        loc?.let {
            val geocoder = Geocoder(activity)
            val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
            val address = addresses?.firstOrNull()
            val cityName = address?.locality ?: address?.subAdminArea ?: ""
            val countryName = address?.countryName ?: ""
            CurrentLocation(it.latitude, it.longitude, cityName, countryName)
        }
    } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
        logger().d("Failed to get location: $e")
        null
    }
}
