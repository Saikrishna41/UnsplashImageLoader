package com.task.imageloaderapp.di.modules

import com.task.imageloaderapp.data.PhotoApi
import com.task.imageloaderapp.data.remote.dto.Photos
import com.task.imageloaderapp.data.remote.repository.PhotoRepositoryImpl
import com.task.imageloaderapp.domain.repository.PhotoRepository
import com.task.imageloaderapp.usecases.GetPhotos
import com.task.imageloaderapp.usecases.PhotosUseCases
import com.task.imageloaderapp.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideBaseUrl() = BASE_URL

    @Provides
    @Singleton
    fun provideRetrofit(baseURl: String): Retrofit =
        Retrofit.Builder().baseUrl(baseURl).addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun providesPhotoApi(retrofit: Retrofit): PhotoApi = retrofit.create(PhotoApi::class.java)

    @Provides
    @Singleton
    fun providePhotoRepository(photoApi: PhotoApi): PhotoRepository = PhotoRepositoryImpl(photoApi)


    @Provides
    @Singleton
    fun providePhotosUseCases(photoRepository: PhotoRepository) =
        PhotosUseCases(GetPhotos(photoRepository))

}