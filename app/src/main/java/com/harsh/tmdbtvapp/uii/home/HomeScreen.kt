package com.harsh.tmdbtvapp.uii.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.harsh.tmdbtvapp.movieViewModel.MovieViewModel

@Composable
fun HomeScreen(viewModel: MovieViewModel) {

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
        ) {

            item { CategoryRow("Trending", viewModel.trending) }
            item { CategoryRow("Top Rated", viewModel.topRated) }
            item { CategoryRow("Popular", viewModel.popular) }
            item { CategoryRow("Upcoming", viewModel.upcoming) }
        }
    }
}