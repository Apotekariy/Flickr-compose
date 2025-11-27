package com.example.flickrcompose.presentation

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil3.compose.AsyncImage
import com.example.flickrcompose.domain.model.Photo

@Composable
fun PhotoGrid(
    photos: LazyPagingItems<Photo>,
    columns: Int,
    onPhotoClick: (Photo) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Обрабатываем состояние начальной загрузки или ошибки
        when (val refreshState = photos.loadState.refresh) {
            is LoadState.Loading -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
            is LoadState.Error -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Ошибка загрузки: ${refreshState.error.message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            is LoadState.NotLoading -> {
                if (photos.itemCount == 0) {
                    Text(
                        text = "Ничего не найдено",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        if (photos.loadState.refresh is LoadState.NotLoading || photos.loadState.append is LoadState.Loading) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(photos.itemCount) { index ->
                    photos[index]?.let { photo ->
                        PhotoItem(
                            photo = photo,
                            onClick = {
                                Log.d("PhotoGrid", "Photo clicked: ${photo.id}, title: ${photo.title}")
                                onPhotoClick(photo)
                            }
                        )
                    }
                }

                // Индикатор загрузки для следующих страниц
                item {
                    LoadingItem(photos.loadState.append)
                }
            }
        }
    }
}

@Composable
fun LoadingItem(loadState: LoadState) {
    when (loadState) {
        is LoadState.Loading -> {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is LoadState.Error -> {
            Text(
                text = "Error loading",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }
        else -> {}
    }
}

@Composable
fun PhotoItem(photo: Photo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable {
                Log.d("PhotoItem", "Card clicked: ${photo.id}")
                onClick()
            }
    ) {
        AsyncImage(
            model = photo.imageUrl,
            contentDescription = photo.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
    }

@Composable
fun CachedPhotoGrid(
    photos: List<Photo>,
    columns: Int,
    onPhotoClick: (Photo) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (photos.isEmpty()) {
            Text(
                text = "Нет сохранённых фотографий",
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    count = photos.size,
                    key = { index -> photos[index].id }
                ) { index ->
                    val photo = photos[index]
                    PhotoItem(
                        photo = photo,
                        onClick = {
                            Log.d("CachedPhotoGrid", "Photo clicked: ${photo.id}, title: ${photo.title}")
                            onPhotoClick(photo)
                        }
                    )
                }
            }
        }
    }
}