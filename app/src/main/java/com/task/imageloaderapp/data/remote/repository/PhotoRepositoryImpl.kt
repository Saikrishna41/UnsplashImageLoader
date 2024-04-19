package com.task.imageloaderapp.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.task.imageloaderapp.data.PhotoApi
import com.task.imageloaderapp.data.remote.dto.PhotosPagingSource
import com.task.imageloaderapp.domain.model.PhotosItem
import com.task.imageloaderapp.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow

class PhotoRepositoryImpl (private val photoApi: PhotoApi) : PhotoRepository{
    override fun getPhotos(): Flow<PagingData<PhotosItem>> {
        return Pager(
            config = PagingConfig(pageSize = 100),
            pagingSourceFactory = {
                PhotosPagingSource(photoApi)
            }
        ).flow
    }
}