package com.harsh.tmdbtvapp.uii.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harsh.tmdbtvapp.data.model.Movie

@Composable
fun CategoryRow(
    title: String,
    movies: List<Movie>
) {

    Column {

        Text(
            text = title,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            itemsIndexed(movies) { index, movie ->
                MovieItem(
                    movie = movie,
                    isFirstItem = index == 0 && title == "Trending"
                )
            }
        }
    }
}