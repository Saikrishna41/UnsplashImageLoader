package com.task.imageloaderapp.usecases

import androidx.paging.PagingData
import com.task.imageloaderapp.data.PhotoApi
import com.task.imageloaderapp.data.remote.dto.Photos
import com.task.imageloaderapp.domain.model.PhotosItem
import com.task.imageloaderapp.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow

class GetPhotos (private val photoRepository: PhotoRepository){
     operator fun invoke() : Flow<PagingData<PhotosItem>> {
        return photoRepository.getPhotos()
    }
}