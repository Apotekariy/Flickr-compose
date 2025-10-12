package com.example.flickrcompose.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PhotoDto(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    val ispublic: Int,
    val isfriend: Int,
    val isfamily: Int
)