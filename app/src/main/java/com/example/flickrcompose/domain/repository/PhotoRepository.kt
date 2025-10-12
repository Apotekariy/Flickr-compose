package com.example.flickrcompose.domain.repository

import androidx.paging.PagingData
import com.example.flickrcompose.domain.model.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    fun searchPhotos(query: String): Flow<PagingData<Photo>>
}