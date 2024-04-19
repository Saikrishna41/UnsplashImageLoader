package com.task.imageloaderapp.domain.repository

import androidx.paging.PagingData
import com.task.imageloaderapp.domain.model.PhotosItem
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    fun getPhotos() : Flow<PagingData<PhotosItem>>
}