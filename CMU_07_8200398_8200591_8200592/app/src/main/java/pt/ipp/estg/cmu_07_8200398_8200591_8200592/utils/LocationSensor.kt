package pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import java.util.*


@Composable
fun SetPermissionsLocation(
    setIsPermitted: (Boolean) -> Unit
) {
    val context = LocalContext.current

    val permission_given = remember {
        mutableStateOf(0)
    }
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        permission_given.value = 2
    }
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                permission_given.value += 1
            }
        }
    LaunchedEffect(key1 = "Permission") {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        permissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    if (permission_given.value == 2) {
        setIsPermitted(true)
    } else {
        setIsPermitted(false)
    }
}

@Composable
@SuppressLint("MissingPermission")
fun ReadLocation(
    setLatitudeAndLongitude: (Double, Double) -> Unit,
    readLocation: Boolean
) {
    val context = LocalContext.current
    DisposableEffect(readLocation) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locations: LocationResult) {
                for (location in locations.locations) {
                    setLatitudeAndLongitude(location.latitude, location.longitude)
                }
            }
        }

        if (readLocation) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                try {
                    setLatitudeAndLongitude(location.latitude, location.longitude)
                }catch (_: Exception){
                }
            }.addOnFailureListener {
                //"Unable to get locatoins"
            }

            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                10000,
            ).setMinUpdateIntervalMillis(30000).build();
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }

        onDispose { fusedLocationClient.removeLocationUpdates(locationCallback) }
    }
}

fun getCoordinatesToAddress(Context: Context, LATITUDE: Double, LONGITUDE: Double): String {
    var strAdd = ""
    val geocoder = Geocoder(Context, Locale.getDefault())
    try {
        val addresses: List<Address>? = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
        if (addresses != null) {
            val returnedAddress: Address = addresses[0]
            val strReturnedAddress = StringBuilder("")
            for (i in 0..returnedAddress.maxAddressLineIndex) {
                strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
            }
            strAdd = strReturnedAddress.toString()
        }
    } catch (_: Exception) {
    }
    return strAdd
}


fun getAddressToCoordinates(Context: Context, address: String): Address? {
    try {
        val geocoder = Geocoder(Context, Locale.getDefault())
        val addresses = geocoder.getFromLocationName(address, 1)

        if (addresses == null || addresses.size != 1) {
            return null
        }

        return addresses[0]
    } catch (_: Exception) {
    }
    return null
}