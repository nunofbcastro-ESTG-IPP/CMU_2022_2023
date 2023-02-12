package pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import coil.Coil
import coil.ImageLoader

fun setImageLoader(context: Context) {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            val imageLoader = ImageLoader.Builder(context)
                .respectCacheHeaders(false)
                .build()
            Coil.setImageLoader(imageLoader)
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            val imageLoader = ImageLoader.Builder(context)
                .respectCacheHeaders(true)
                .build()
            Coil.setImageLoader(imageLoader)
        } /*else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {

        }*/
    }
}

fun checkConnectedToNetwork(context: Context): Boolean{
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    return capabilities != null
}