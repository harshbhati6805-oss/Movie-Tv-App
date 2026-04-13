package com.harsh.tmdbtvapp.uii.home.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.harsh.tmdbtvapp.movieViewModel.DetailViewModel
import com.harsh.tmdbtvapp.uii.home.MovieItem
import com.harsh.tmdbtvapp.uii.home.component.TvFocusableButton
import com.harsh.tmdbtvapp.utils.Constants
import com.harsh.tmdbtvapp.navigation.NavRoutes

@Composable
fun DetailScreen(
    movieId: Int,
    viewModel: DetailViewModel = viewModel(),
    navController: NavController
) {

    BackHandler {
        navController.popBackStack()
    }

    val focusRequester = remember { FocusRequester() }

    // Load data
    LaunchedEffect(movieId) {
        viewModel.loadMovie(movieId)
    }

    val movie = viewModel.movie
    val isLoading = viewModel.isLoading

    // LOADER
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.BottomStart
    ) {

        // BACKGROUND IMAGE
        AsyncImage(
            model = movie?.backdrop_path?.let {
                Constants.IMAGE_BASE_URL + it
            } ?: "",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // GRADIENT
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.9f),
                            Color.Transparent
                        )
                    )
                )
        )

        // MAIN CONTENT
        Column(
            modifier = Modifier
                .fillMaxSize()
//                .onPreviewKeyEvent { event ->
//                    if (event.key == Key.Back && event.type == KeyEventType.KeyUp) {
//                        navController.popBackStack()
//                        true
//                    } else {
//                        false
//                    }
//                }
                .padding(start = 40.dp, top = 20.dp, end = 40.dp, bottom = 10.dp)
        ) {

            // TITLE
            Text(
                text = movie?.title ?: "",
                color = Color.White,
                fontSize = 34.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // DESCRIPTION
            Text(
                text = movie?.overview ?: "",
                color = Color.LightGray,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 20.sp,
                modifier = Modifier.width(500.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // BUTTONS
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                TvFocusableButton(
                    text = "Watch Now",
                    isDefaultFocused = true,
                    focusRequester = focusRequester,
                    onClick = {
                        val movieUrl = "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4"
                        val movieTitle = movie?.title ?: "Movie"

                        navController.navigate(
                            "player?url=$movieUrl&title=$movieTitle"
                        )
                    }
                )

                TvFocusableButton(
                    text = "Play Trailer",
                    onClick = {
                        val trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
                        val movieTitle = movie?.title ?: "Movie"

                        navController.navigate(
                            "player?url=$trailerUrl&title=$movieTitle"
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))


        }


        // RELATED SECTION (FIXED)
        Column {

            // TITLE (ONLY ONCE)
            Text(
                text = "Related",
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 40.dp, bottom = 10.dp)
            )

            // MOVIES ROW
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentPadding = PaddingValues(horizontal = 40.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.relatedMovies) { movie ->
                    MovieItem(
                        movie = movie,
                        isFirstItem = false,
                        onClick = { movieId ->
                            navController.navigate("${NavRoutes.DETAIL}/$movieId")
                        }
                    )
                }
            }
        }

        // DEFAULT FOCUS
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}