package com.example.flickrcompose.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.flickrcompose.data.local.PhotoDao
import com.example.flickrcompose.data.local.mapper.toDomain
import com.example.flickrcompose.data.remote.ApiService
import com.example.flickrcompose.data.remote.paging.PhotoPagingSource
import com.example.flickrcompose.di.NetworkModule
import com.example.flickrcompose.domain.model.Photo
import com.example.flickrcompose.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val photoDao: PhotoDao,
    private val networkUtil: NetworkModule.NetworkUtil,
    private val pagingConfig: PagingConfig
) : PhotoRepository {

    private val apiKey = "da9d38d3dee82ec8dda8bb0763bf5d9c"

    override fun searchPhotos(query: String): Flow<PagingData<Photo>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                PhotoPagingSource(
                    apiService = apiService,
                    photoDao = photoDao,
                    networkUtil = networkUtil,
                    apiKey = apiKey,
                    query = query
                )
            }
        ).flow
    }

    override fun getCachedPhotos(): Flow<List<Photo>> {
        return photoDao.getAllPhotos()
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    override suspend fun clearCache() {
        photoDao.clearAll()
    }
}