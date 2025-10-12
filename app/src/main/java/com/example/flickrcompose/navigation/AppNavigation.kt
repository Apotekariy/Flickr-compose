package com.example.flickrcompose.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flickrcompose.domain.model.Photo
import com.example.flickrcompose.presentation.HomeScreen
import com.example.flickrcompose.presentation.PhotoDetailScreen

/**
 * Корневая Composable-функция для навигации.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Храним выбранное фото на уровне NavHost
    val photoHolder = remember { PhotoHolder() }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        addHomeGraph(navController, photoHolder)
    }
}

// Простой holder для передачи данных между экранами
class PhotoHolder {
    var currentPhoto: Photo? = null
}

/**
 * Определяет граф навигации для главного экрана и связанных с ним экранов.
 */
private fun NavGraphBuilder.addHomeGraph(navController: NavController, photoHolder: PhotoHolder) {
    // Экран Home
    composable(route = Screen.Home.route) {
        HomeScreen(
            onPhotoClick = { photo ->
                Log.d("AppNavigation", "Photo clicked: ${photo.id}")

                // Сохраняем фото в holder
                photoHolder.currentPhoto = photo
                Log.d("AppNavigation", "Photo saved to holder")

                // Выполняем навигацию
                navController.navigate(Screen.PhotoDetail.route)
                Log.d("AppNavigation", "Navigation triggered")
            }
        )
    }

    // Экран деталей фото
    composable(route = Screen.PhotoDetail.route) { backStackEntry ->
        Log.d("AppNavigation", "PhotoDetail composable entered")

        val photo = photoHolder.currentPhoto
        Log.d("AppNavigation", "Photo from holder: ${photo?.id}")

        if (photo != null) {
            // Сохраняем в savedStateHandle для восстановления после process death
            backStackEntry.savedStateHandle["photo"] = photo

            PhotoDetailScreen(
                onBackClick = {
                    Log.d("AppNavigation", "Back clicked")
                    photoHolder.currentPhoto = null // Очищаем
                    navController.popBackStack()
                },
                photo = photo
            )
        } else {
            // Пытаемся восстановить из savedStateHandle (после process death)
            val savedPhoto = backStackEntry.savedStateHandle.get<Photo>("photo")
            if (savedPhoto != null) {
                Log.d("AppNavigation", "Photo restored from savedStateHandle")
                PhotoDetailScreen(
                    onBackClick = { navController.popBackStack() },
                    photo = savedPhoto
                )
            } else {
                Log.e("AppNavigation", "No photo available, going back")
                navController.popBackStack()
            }
        }
    }
}