package com.harsh.tmdbtvapp.uii.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.harsh.tmdbtvapp.data.model.Movie
import com.harsh.tmdbtvapp.utils.Constants

@Composable
fun MovieItem(
    movie: Movie,
    isFirstItem: Boolean = false,
    isPreview: Boolean = false,
    isFocused:Boolean  =  false
) {

    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        if (isFirstItem && !isPreview) {
            focusRequester.requestFocus()
        }
    }

    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 6.dp)
            .size(140.dp, 200.dp)
            .scale(if (isFocused) 1.05f else 1f)
            .focusRequester(focusRequester)
            .onFocusChanged { isFocused = it.isFocused }
            .then(
                if (!isPreview) Modifier.focusable() else Modifier
            )
    ) {

        // IMAGE WITH ROUNDED CORNERS
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(6.dp))
                .background(Color.Black)
        ) {

            if (isPreview) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray)
                )
            } else {
                AsyncImage(
                    model = Constants.IMAGE_BASE_URL + movie.poster_path,
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // BORDER OVERLAY (only in real app)
        if (isFocused && !isPreview) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(6.dp))
                    .border(
                        width = 1.5.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(6.dp)
                    )
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun MovieItemPreview() {

    val dummyMovie = Movie(
        id = 1,
        title = "Sample Movie",
        poster_path = ""
    )

    MovieItem(
        movie = dummyMovie,
        isFirstItem = false,
        isPreview = true,
        isFocused = true
    )
}