package com.example.flickrcompose.data.remote.mapper

import com.example.flickrcompose.data.remote.dto.PhotoDto
import com.example.flickrcompose.domain.model.Photo

fun PhotoDto.toDomain(): Photo {
    val baseUrl = "https://live.staticflickr.com/$server/${id}_${secret}"
    return Photo(
        id = id,
        title = title.ifEmpty { "Untitled" },
        imageUrl = "${baseUrl}_m.jpg",        // m = small (240px)
        largeImageUrl = "${baseUrl}_b.jpg"    // b = large (1024px)
    )
}
