package com.harsh.tmdbtvapp.data.repository

import com.harsh.tmdbtvapp.data.api.network.RetrofitInstance
import com.harsh.tmdbtvapp.utils.Constants

class MovieRepository {

    private val api = RetrofitInstance.api

    suspend fun getTrending() =
        api.getTrending(Constants.API_KEY).results

    suspend fun getTopRated() =
        api.getTopRated(Constants.API_KEY).results

    suspend fun getPopular() =
        api.getPopular(Constants.API_KEY).results

    suspend fun getUpcoming() =
        api.getUpcoming(Constants.API_KEY).results
}