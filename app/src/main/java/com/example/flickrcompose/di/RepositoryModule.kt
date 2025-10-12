package com.example.flickrcompose.di

import com.example.flickrcompose.data.repository.PhotoRepositoryImpl
import com.example.flickrcompose.domain.repository.PhotoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPhotoRepository(
        photoRepositoryImpl: PhotoRepositoryImpl
    ) : PhotoRepository
}