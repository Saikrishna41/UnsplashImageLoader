package com.task.imageloaderapp.presentation.homescreen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.util.Log
import android.util.LruCache
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.task.imageloaderapp.domain.model.PhotosItem
import com.task.imageloaderapp.utils.addBitmapToMemCache
import com.task.imageloaderapp.utils.getBitmapFromMemCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.paging.log
import com.task.imageloaderapp.R
import com.task.imageloaderapp.presentation.Dimens
import com.task.imageloaderapp.presentation.common.PhotosCardShimmerEffect
import com.task.imageloaderapp.presentation.common.shimmerEffect
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getSystemService

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier, memoryCache: LruCache<String, Bitmap>) {
    val viewModel: HomeViewModel = hiltViewModel()
    val photos = viewModel.getPhotos().collectAsLazyPagingItems()
    val refreshing by viewModel.isRefreshing.collectAsState()
    val pullRefreshState = rememberPullRefreshState(refreshing, {
        viewModel.getPhotos()
    })
    Box(modifier = modifier.pullRefresh(pullRefreshState)) {
        PullRefreshIndicator(
            refreshing,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )
        PhotoLists(photos = photos, modifier, memoryCache)
    }
}

@Composable
fun PhotoLists(
    photos: LazyPagingItems<PhotosItem>,
    modifier: Modifier = Modifier, memoryCache: LruCache<String, Bitmap>
) {
    val handlePagingResult = handlePagingResult(photos = photos,modifier)
    if (handlePagingResult) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            modifier = modifier.padding(top = 100.dp)
        ) {
            items(count = photos.itemCount) { index ->
                val photo = photos[index]
                var bitmapState by rememberSaveable {
                    mutableStateOf<Bitmap?>(null)
                }
                photo?.urls?.small?.let { imgUrl ->
                    loadBitmapAsync(imgUrl) { bitmap ->
                        bitmapState = getBitmapFromMemCache(index.toString(), memoryCache) ?: bitmap
                        addBitmapToMemCache(index.toString(), bitmap, memoryCache)
                    }
                    Surface(
                        tonalElevation = 3.dp,
                        modifier = Modifier.aspectRatio(1f)
                    ) {
                        bitmapState?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentScale = ContentScale.FillBounds,
                                contentDescription = "",
                                modifier = Modifier.fillMaxHeight()

                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun handlePagingResult(
    photos: LazyPagingItems<PhotosItem>,
    modifier: Modifier = Modifier,
): Boolean {
    val loadState = photos.loadState
    val error = when {
        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
        else -> null
    }
    return when {
        loadState.refresh is LoadState.Loading -> {
            ShimmerEffect()
            false
        }

        error != null -> {
            false
        }

        else -> {
            ShimmerEffect()
            true
        }
    }
}

private fun loadBitmapAsync(url: String, bitmapCallback: (Bitmap?) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        val bitmap = urlToBitmap(url)
        bitmapCallback(bitmap)
    }
}

private fun urlToBitmap(url: String): Bitmap? {
    return try {
        val conn = URL(url).openConnection()
        conn.connect()
        val inputStream = conn.getInputStream()
        val bitmap = BitmapFactory.decodeStream(inputStream)
        bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
private fun ShimmerEffect() {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 100.dp)
            .fillMaxWidth()
    ) {
        repeat(20) {
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top) {
                repeat(4) {
                    PhotosCardShimmerEffect()
                }
            }
        }
    }
}







