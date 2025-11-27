package com.example.flickrcompose.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "photos")
data class PhotoEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val imageUrl: String,
    val largeImageUrl: String,
    val timestamp: Long = System.currentTimeMillis()
)