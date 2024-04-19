package com.task.imageloaderapp.presentation.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.task.imageloaderapp.domain.model.PhotosItem
import com.task.imageloaderapp.usecases.PhotosUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val photosUseCases: PhotosUseCases) : ViewModel() {
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    val photoData = photosUseCases.getPhotos().cachedIn(viewModelScope)


    fun getPhotos(): Flow<PagingData<PhotosItem>> {
        val  photos = photosUseCases.getPhotos()
         _isRefreshing.value = false
         return photos
    }
}