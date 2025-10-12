package com.example.flickrcompose.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class FlickrResponseDto(
    val photos: PhotosDataDto,
    val stat: String
)
