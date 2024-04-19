package com.task.imageloaderapp.utils

import android.graphics.Bitmap
import android.util.LruCache

fun getBitmapFromMemCache(key: String, memoryCache: LruCache<String, Bitmap>): Bitmap? {
    return memoryCache.get(key)
}

fun addBitmapToMemCache(
    key: String,
    bitmap: Bitmap?,
    memoryCache: LruCache<String, Bitmap>
) {
    if (getBitmapFromMemCache(key, memoryCache) == null) {
        memoryCache.put(key, bitmap)
    }
}
