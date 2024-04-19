package com.task.imageloaderapp.data.remote.dto

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.task.imageloaderapp.data.PhotoApi
import com.task.imageloaderapp.domain.model.PhotosItem

private const val TAG = "TAGSS-pagingsource"

class PhotosPagingSource(private val photosApi: PhotoApi) : PagingSource<Int, PhotosItem>() {
    private var totalNewsCount = 0
    private val photosList = mutableListOf<List<PhotosItem>>()
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotosItem> {
        val page = params.key ?: 1
        return try {
            val photos = photosApi.getPhotos(page).distinctBy { it.id }
            totalNewsCount += photos.size
            photosList.add(photos)
            LoadResult.Page(
                data = photos,
                nextKey = if (totalNewsCount == photosList.size) {
                    Log.d(TAG, "page id is --- $page")
                    null
                } else {
                    Log.d(TAG, "page id is $page")
                    page + 1
                },
                prevKey = null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PhotosItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}