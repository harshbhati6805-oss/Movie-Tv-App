package com.harsh.tmdbtvapp.movieViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.tmdbtvapp.data.model.Movie
import com.harsh.tmdbtvapp.data.repository.MovieRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    var movie by mutableStateOf<Movie?>(null)
        private set

    var isLoading by mutableStateOf(true)
        private set

    var relatedMovies by mutableStateOf<List<Movie>>(emptyList())
        private set

    fun loadMovie(id: Int) {
        viewModelScope.launch {

            isLoading = true

            val repo = MovieRepository()

            // Parallel API call 🔥
            val movieDeferred = async { repo.getMovieDetails(id) }
            val relatedDeferred = async { repo.getSimilarMovies(id) }

            movie = movieDeferred.await()
            relatedMovies = relatedDeferred.await().results

            isLoading = false
        }
    }
}