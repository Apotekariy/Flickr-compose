package com.example.flickrcompose.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.flickrcompose.di.NetworkModule
import com.example.flickrcompose.domain.model.Photo
import com.example.flickrcompose.domain.repository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val networkUtil: NetworkModule.NetworkUtil
) : ViewModel() {

    private val _photos = MutableStateFlow<PagingData<Photo>>(PagingData.empty())
    val photos: StateFlow<PagingData<Photo>> = _photos.asStateFlow()

    private val _cachedPhotos = MutableStateFlow<List<Photo>>(emptyList())
    val cachedPhotos: StateFlow<List<Photo>> = _cachedPhotos.asStateFlow()

    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private var currentSearchJob: Job? = null

    init {
        loadCachedPhotos()
        searchPhotos("cats")
    }

    private fun loadCachedPhotos() {
        viewModelScope.launch {
            photoRepository.getCachedPhotos()
                .collect { photos ->
                    _cachedPhotos.value = photos
                }
        }
    }

    fun searchPhotos(query: String) {
        // Отменяем предыдущий поиск
        currentSearchJob?.cancel()

        if (!networkUtil.isNetworkAvailable()) {
            _isOnline.value = false
            Log.d("HomeViewModel", "No internet connection, showing cached photos")
            return
        }

        // Запускаем новый поиск
        currentSearchJob = viewModelScope.launch {
            try {
                _isOnline.value = true
                _photos.value = PagingData.empty()

                photoRepository.clearCache()

                photoRepository.searchPhotos(query)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _photos.value = pagingData
                    }
            } catch (e: Exception) {
                _isOnline.value = false
            }
        }
    }
}