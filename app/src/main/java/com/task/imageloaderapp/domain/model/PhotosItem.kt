package com.task.imageloaderapp.domain.model

import com.google.gson.annotations.SerializedName

data class PhotosItem(
    @SerializedName("id")
    val id : String,
    @SerializedName("urls")
    val urls: Urls
)