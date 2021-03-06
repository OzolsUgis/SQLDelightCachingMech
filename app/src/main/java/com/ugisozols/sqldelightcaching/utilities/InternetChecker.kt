package com.ugisozols.sqldelightcaching.utilities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun checkInternetConnectivity(
    context: Context
) : Boolean {
    val connectivityManager = context
        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val network = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

    return network.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}