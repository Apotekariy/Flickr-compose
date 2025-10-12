package com.example.flickrcompose.presentation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.example.flickrcompose.R
import com.example.flickrcompose.domain.model.Photo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetailScreen(
    onBackClick: () -> Unit,
    photo: Photo
) {
    Log.d("PhotoDetailScreen", "Screen opened with photo: ${photo.id}")
    Log.d("PhotoDetailScreen", "Large image URL: ${photo.largeImageUrl}")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(photo.title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.outline_arrow_back_24),
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = photo.largeImageUrl,
                contentDescription = photo.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),  // отношение 1:1
                contentScale = ContentScale.Crop,
                onSuccess = { Log.d("PhotoDetailScreen", "Image loaded successfully") },
                onError = { Log.e("PhotoDetailScreen", "Image loading error: ${it.result.throwable}") }
            )
        }
    }
}