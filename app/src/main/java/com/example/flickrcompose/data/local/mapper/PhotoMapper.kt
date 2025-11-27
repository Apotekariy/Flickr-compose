package com.example.flickrcompose.data.local.mapper

import com.example.flickrcompose.data.local.PhotoEntity
import com.example.flickrcompose.domain.model.Photo

fun Photo.toEntity(): PhotoEntity {
    return PhotoEntity(
        id = this.id,
        title = this.title,
        imageUrl = this.imageUrl,
        largeImageUrl = this.largeImageUrl,
        timestamp = System.currentTimeMillis()
    )
}

fun PhotoEntity.toDomain() : Photo {
    return Photo(
        id = this.id,
        title = this.title,
        imageUrl = this.imageUrl,
        largeImageUrl = this.largeImageUrl
    )
}