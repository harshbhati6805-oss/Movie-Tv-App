package com.harsh.tmdbtvapp.uii.home

import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.runtime.remember
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    val focusRequesters = remember { List(4) { FocusRequester() } }

    val categories = viewModel.categories.entries.toList()



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

            items(categories.size) { index ->
                val (title, movies) = categories[index]

                CategoryRow(
                    title = title,
                    movies = movies,
                    upFocus = focusRequesters.getOrNull(index - 1),
                    downFocus = focusRequesters.getOrNull(index + 1),
                    modifier = Modifier.focusRequester(focusRequesters[index]),
                    rowIndex = index,
                    lastClickedRowIndex = viewModel.lastClickedPosition?.first,
                    lastClickedColIndex = viewModel.lastClickedPosition?.second,
                    onMovieClick = { colIndex, movieId ->
                        viewModel.lastClickedPosition = Pair(index, colIndex)
                        navController.navigate("${NavRoutes.DETAIL}/$movieId")
                    }
                )
            }
        }
    }
}