package com.harsh.tmdbtvapp.data.model

data class Movie(
    val id: Int,
    val title: String,
    val poster_path: String,
    val overview: String,
    val backdrop_path: String?
)

