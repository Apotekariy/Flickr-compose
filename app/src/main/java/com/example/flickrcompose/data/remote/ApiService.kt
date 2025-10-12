package com.example.flickrcompose.data.remote

import com.example.flickrcompose.data.remote.dto.FlickrResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("services/rest/")
    suspend fun searchPhotos(
        @Query("method") method: String = "flickr.photos.search",
        @Query("api_key") apiKey: String,
        @Query("text") searchText: String,
        @Query("page") page: Int = 1,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: Int = 1
    ): FlickrResponseDto
}