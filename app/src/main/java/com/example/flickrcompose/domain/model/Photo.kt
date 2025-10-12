package com.example.flickrcompose.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    val id: String,
    val title: String,
    val imageUrl: String,
    val largeImageUrl: String
) : Parcelable
