package com.task.imageloaderapp

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.LruCache
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.task.imageloaderapp.presentation.homescreen.HomeScreen
import com.task.imageloaderapp.ui.theme.ImageLoaderAppTheme
import com.task.imageloaderapp.utils.ConnectionStatus
import com.task.imageloaderapp.utils.CurrentConnectivityStatus
import dagger.hilt.android.AndroidEntryPoint
import com.task.imageloaderapp.utils.observeConnectivityAsFlow
import com.task.imageloaderapp.presentation.common.UnavailableScreen


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var memoryCache : LruCache<String, Bitmap>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8
        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String?, bitmap: Bitmap?): Int {
                return bitmap?.byteCount?.div(1024)!!
            }
        }
        setContent {
            ImageLoaderAppTheme {
                if (checkConnectivityStatus()) {
                    HomeScreen(modifier = Modifier.fillMaxSize(),memoryCache)
                }
                else {
                    UnavailableScreen(modifier = Modifier.fillMaxSize())
                }

            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
 fun checkConnectivityStatus() : Boolean {
    val connection by connectivityStatus()
    val isConnected = connection === ConnectionStatus.Available
    return isConnected
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun connectivityStatus() : State<ConnectionStatus> {
    val mCtx = LocalContext.current
    return produceState(initialValue = mCtx.CurrentConnectivityStatus) {
        mCtx.observeConnectivityAsFlow().collect{value = it}
    }
}

