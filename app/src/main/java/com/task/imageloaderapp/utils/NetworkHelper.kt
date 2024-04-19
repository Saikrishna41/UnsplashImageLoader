package com.task.imageloaderapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow


val Context.CurrentConnectivityStatus : ConnectionStatus
    get()  {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return getCurrentConnectivityStatus(connectivityManager)
    }


private fun getCurrentConnectivityStatus(connectivityManager: ConnectivityManager): ConnectionStatus {
    val connected = connectivityManager.allNetworks.any { network ->
        connectivityManager.getNetworkCapabilities(network)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    }

    return if (connected) {
        ConnectionStatus.Available
    } else {
        ConnectionStatus.Unavailable
    }

}

@RequiresApi(Build.VERSION_CODES.S)
fun Context.observeConnectivityAsFlow() = callbackFlow {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val callBack = NetworkCallback { connectionState -> trySend(connectionState) }

    val networkRequest =
        NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()

    connectivityManager.registerNetworkCallback(networkRequest,callBack )

    val currentStatus  = getCurrentConnectivityStatus(connectivityManager)
    trySend(currentStatus)
    awaitClose {
        connectivityManager.unregisterNetworkCallback(callBack)
    }
}

fun NetworkCallback(callBack : (ConnectionStatus) -> Unit) : ConnectivityManager.NetworkCallback {
    return object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network : Network) {
            callBack( ConnectionStatus.Available)
        }

        override fun onLost(network: Network) {
            callBack( ConnectionStatus.Unavailable)
        }
    }
}
