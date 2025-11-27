package com.example.flickrcompose.presentation

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.flickrcompose.R
import com.example.flickrcompose.domain.model.Photo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onPhotoClick: (Photo) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val photos = viewModel.photos.collectAsLazyPagingItems()
    val columns = integerResource(R.integer.photo_grid_columns)

    var searchQuery by remember { mutableStateOf("cats") }
    val keyboardController = LocalSoftwareKeyboardController.current

    val isOnline by viewModel.isOnline.collectAsState()
    val cachedPhotos by viewModel.cachedPhotos.collectAsState()

    Log.d("HomeScreen", "Photos count: ${photos.itemCount}")
    Log.d("HomeScreen", "Is online: $isOnline")
    Log.d("HomeScreen", "Cached photos: ${cachedPhotos.size}")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isOnline) "Flickr Photos" else "Flickr Photos (Offline)") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            // Строка поиска
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search photos...") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.outline_search_24),
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                painter = painterResource(R.drawable.outline_cancel_24),
                                contentDescription = "Clear"
                            )
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (searchQuery.isNotBlank()) {
                            Log.d("HomeScreen", "Searching for: $searchQuery")
                            viewModel.searchPhotos(searchQuery.trim())
                            keyboardController?.hide()
                        }
                    }
                )
            )

            // Сетка фотографий
            if (isOnline) {
                // Онлайн - показываем Paging
                PhotoGrid(
                    photos = photos,
                    columns = columns,
                    onPhotoClick = { photo ->
                        Log.d("HomeScreen", "onPhotoClick received: ${photo.id}")
                        onPhotoClick(photo)
                    },
                    modifier = Modifier.weight(1f)
                )
            } else {
                // Оффлайн - показываем кэшированные фото
                CachedPhotoGrid(
                    photos = cachedPhotos,
                    columns = columns,
                    onPhotoClick = { photo ->
                        Log.d("HomeScreen", "onPhotoClick (cached) received: ${photo.id}")
                        onPhotoClick(photo)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}