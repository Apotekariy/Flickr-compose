package com.example.flickrcompose.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PhotosDataDto(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: Int,
    val photo: List<PhotoDto>
)