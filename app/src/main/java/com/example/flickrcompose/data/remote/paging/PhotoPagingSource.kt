package com.example.flickrcompose.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.flickrcompose.data.local.PhotoDao
import com.example.flickrcompose.data.local.mapper.toEntity
import com.example.flickrcompose.data.remote.ApiService
import com.example.flickrcompose.data.remote.mapper.toDomain
import com.example.flickrcompose.di.NetworkModule
import com.example.flickrcompose.domain.model.Photo

class PhotoPagingSource(
    private val apiService: ApiService,
    private val photoDao: PhotoDao,
    private val networkUtil: NetworkModule.NetworkUtil,
    private val apiKey: String,
    private val query: String
) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        if (!networkUtil.isNetworkAvailable()) {
            return LoadResult.Error(
                Exception("No internet connection")
            )
        }

        return try {
            val page = params.key ?: 1
            val response = apiService.searchPhotos(
                apiKey = apiKey,
                searchText = query,
                page = page
            )

            val photos = response.photos.photo.map { it.toDomain() }

            if (page == 1) {
                // При первой странице очищаем старый кэш
                photoDao.clearAll()
            }
            val entities = photos.map { it.toEntity() }
            photoDao.insertPhotos(entities)

            LoadResult.Page(
                data = photos,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page < response.photos.pages) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}