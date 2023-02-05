package com.example.weatherapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object Constants {
    const val APP_ID: String = "2c50325f427689340a03ff16215d8fc4"
    const val BASE_URL: String = "http://api.openweathermap.org/data/"
    const val METRIC_UNIT: String = "metric"
    const val PREFERENCE_NAME: String = "WeatherAppPreference"
    const val WEATHER_RESPONSE_DATA: String = "weather_response_data"

    fun isNetworkAvailable(context: Context): Boolean {
        val connectionManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectionManager.activeNetwork ?: return false // like in javascript A ?? B
            val activeNetwork = connectionManager.getNetworkCapabilities(network)
            return when {
                activeNetwork!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                activeNetwork!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                activeNetwork!!.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
                else -> false
            }
        } else {
            val networkInfo = connectionManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnectedOrConnecting
        }
    }
}