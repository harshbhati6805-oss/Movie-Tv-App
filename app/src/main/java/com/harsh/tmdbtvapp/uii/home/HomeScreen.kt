package com.harsh.tmdbtvapp.uii.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.harsh.tmdbtvapp.movieViewModel.MovieViewModel
import com.harsh.tmdbtvapp.navigation.NavRoutes

@Composable
fun HomeScreen(
    viewModel: MovieViewModel,
    navController: NavController
) {

    val isLoading = viewModel.isLoading

    if (isLoading) {

        // Loader UI
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }

    } else {

        // Main Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
              ,
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item {
                CategoryRow(
                    title = "Trending",
                    movies = viewModel.trending,
                    onMovieClick = { movieId ->
                        navController.navigate("${NavRoutes.DETAIL}/$movieId")

                    }
                )
            }

            item {
                CategoryRow(
                    title = "Top Rated",
                    movies = viewModel.topRated,
                    onMovieClick = { movieId ->
                        navController.navigate("${NavRoutes.DETAIL}/$movieId")
                    }
                )
            }

            item {
                CategoryRow(
                    title = "Popular",
                    movies = viewModel.popular,
                    onMovieClick = { movieId ->
                        navController.navigate("${NavRoutes.DETAIL}/$movieId")
                    }
                )
            }

            item {
                CategoryRow(
                    title = "Upcoming",
                    movies = viewModel.upcoming,
                    onMovieClick = { movieId ->
                        navController.navigate("${NavRoutes.DETAIL}/$movieId")
                    }
                )
            }
        }
    }
}