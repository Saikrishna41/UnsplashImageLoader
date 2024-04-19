package com.task.imageloaderapp.data

import com.task.imageloaderapp.BuildConfig
import com.task.imageloaderapp.data.remote.dto.Photos
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoApi {

    @GET("photos")
    suspend fun getPhotos(
        @Query("page") page : Int,
        @Query("client_id") accessKey : String = BuildConfig.ACCESS_KEY) : Photos
}