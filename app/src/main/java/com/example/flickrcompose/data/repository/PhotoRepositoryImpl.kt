package com.example.flickrcompose.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.flickrcompose.data.remote.ApiService
import com.example.flickrcompose.data.remote.paging.PhotoPagingSource
import com.example.flickrcompose.domain.model.Photo
import com.example.flickrcompose.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val pagingConfig: PagingConfig
) : PhotoRepository {

    private val apiKey = "da9d38d3dee82ec8dda8bb0763bf5d9c"

    override fun searchPhotos(query: String): Flow<PagingData<Photo>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                PhotoPagingSource(
                    apiService = apiService,
                    apiKey = apiKey,
                    query = query
                )
            }
        ).flow
    }
}