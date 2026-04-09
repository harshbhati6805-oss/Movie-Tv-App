package com.harsh.tmdbtvapp.uii.home

import android.R.attr.scaleX
import android.R.attr.scaleY
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.harsh.tmdbtvapp.data.model.Movie
import com.harsh.tmdbtvapp.utils.Constants

@Composable
fun MovieItem(
    movie: Movie,
    isFirstItem: Boolean = false,
    isPreview: Boolean = false,
    onClick: (Int) -> Unit = {}
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
            .size(140.dp, 200.dp)
            .graphicsLayer {
                scaleX = if (isFocused) 1.05f else 1f
                scaleY = if (isFocused) 1.05f else 1f
            }
            .focusRequester(focusRequester)
            .onFocusChanged { isFocused = it.isFocused }
            .then(
                if (!isPreview) Modifier.focusable() else Modifier
            )
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown &&
                    (event.key == Key.DirectionCenter || event.key == Key.NumPadEnter)
                ) {
                    onClick(movie.id)
                    true
                } else {
                    false
                }
            }
    ) {

        // IMAGE
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

        // BORDER
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