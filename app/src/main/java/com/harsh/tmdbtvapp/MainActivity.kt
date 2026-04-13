package com.harsh.tmdbtvapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.harsh.tmdbtvapp.movieViewModel.MovieViewModel
import com.harsh.tmdbtvapp.navigation.NavRoutes
import com.harsh.tmdbtvapp.uii.home.HomeScreen
import com.harsh.tmdbtvapp.uii.home.detail.DetailScreen
import com.harsh.tmdbtvapp.uii.home.player.PlayerScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = NavRoutes.HOME
            ) {

                // HOME SCREEN
                composable(NavRoutes.HOME) {

                    val movieVM: MovieViewModel = viewModel()

                    HomeScreen(
                        viewModel = movieVM,
                        navController = navController
                    )
                }

                // DETAIL SCREEN
                composable("${NavRoutes.DETAIL}/{id}") { backStackEntry ->

                    val movieId =
                        backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0

                    DetailScreen(
                        movieId = movieId,
                        navController = navController
                    )
                }

                // 🎥 PLAYER (👉 ADD THIS HERE)
                composable("player?url={url}&title={title}") { backStackEntry ->

                    val url = backStackEntry.arguments?.getString("url") ?: ""
                    val title = backStackEntry.arguments?.getString("title") ?: ""

                    PlayerScreen(
                        videoUrl = url,
                        movieTitle = title,
                        navController = navController
                    )
                }

                // PLAYER SCREEN
//                composable("player?url={url}&title={title}") { backStackEntry ->
//
//                    val url = backStackEntry.arguments?.getString("url") ?: ""
//                    val title = backStackEntry.arguments?.getString("title") ?: ""
//
//                    PlayerScreen(
//                        videoUrl = url,
//                        movieTitle = title,
//                        navController = navController
//                    )
//                }
            }
        }
    }
}