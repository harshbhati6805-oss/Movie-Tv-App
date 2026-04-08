package com.harsh.tmdbtvapp.movieViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.tmdbtvapp.data.model.Movie
import com.harsh.tmdbtvapp.data.repository.MovieRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import kotlinx.coroutines.async

class MovieViewModel : ViewModel() {

    private val repository = MovieRepository()

    var trending by mutableStateOf<List<Movie>>(emptyList())
        private set

    var topRated by mutableStateOf<List<Movie>>(emptyList())
        private set

    var popular by mutableStateOf<List<Movie>>(emptyList())
        private set

    var upcoming by mutableStateOf<List<Movie>>(emptyList())
        private set

    var isLoading by mutableStateOf(true)
        private set

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            try {
                isLoading = true

                val trendingDeferred = async { repository.getTrending() }
                val topRatedDeferred = async { repository.getTopRated() }
                val popularDeferred = async { repository.getPopular() }
                val upcomingDeferred = async { repository.getUpcoming() }

                trending = trendingDeferred.await()
                topRated = topRatedDeferred.await()
                popular = popularDeferred.await()
                upcoming = upcomingDeferred.await()

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
}