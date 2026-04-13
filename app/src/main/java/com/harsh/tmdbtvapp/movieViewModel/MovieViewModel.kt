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

    var lastClickedPosition by mutableStateOf<Pair<Int, Int>?>(null)

    var categories by mutableStateOf<Map<String, List<Movie>>>(emptyMap())
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

                val fetchers = listOf(
                    "Trending" to async { repository.getTrending() },
                    "Top Rated" to async { repository.getTopRated() },
                    "Popular" to async { repository.getPopular() },
                    "Upcoming" to async { repository.getUpcoming() },
                )

                categories = fetchers.associate { (title, deferred) ->
                    title to deferred.await()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
}