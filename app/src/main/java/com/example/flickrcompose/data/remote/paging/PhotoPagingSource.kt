package com.example.flickrcompose.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.flickrcompose.data.remote.ApiService
import com.example.flickrcompose.data.remote.mapper.toDomain
import com.example.flickrcompose.domain.model.Photo

class PhotoPagingSource(
    private val apiService: ApiService,
    private val apiKey: String,
    private val query: String
) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return try {
            val page = params.key ?: 1
            val response = apiService.searchPhotos(
                apiKey = apiKey,
                searchText = query,
                page = page
            )

            val photos = response.photos.photo.map { it.toDomain() }

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