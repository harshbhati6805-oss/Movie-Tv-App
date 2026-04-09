package com.harsh.tmdbtvapp.data.api

import com.harsh.tmdbtvapp.data.model.Movie
import com.harsh.tmdbtvapp.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiNetworkService {

    @GET("trending/movie/day")
    suspend fun getTrending(
        @Query("api_key") apiKey: String
    ): MovieResponse

    @GET("movie/top_rated")
    suspend fun getTopRated(
        @Query("api_key") apiKey: String
    ): MovieResponse

    @GET("movie/popular")
    suspend fun getPopular(
        @Query("api_key") apiKey: String
    ): MovieResponse

    @GET("movie/upcoming")
    suspend fun getUpcoming(
        @Query("api_key") apiKey: String
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Movie

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): MovieResponse
}