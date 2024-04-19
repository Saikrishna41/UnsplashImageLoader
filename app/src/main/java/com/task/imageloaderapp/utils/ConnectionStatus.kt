package com.task.imageloaderapp.utils

sealed class ConnectionStatus {
    data object Available : ConnectionStatus()
    data object Unavailable : ConnectionStatus()
}