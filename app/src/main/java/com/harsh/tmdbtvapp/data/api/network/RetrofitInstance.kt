package com.harsh.tmdbtvapp.data.api.network

import com.harsh.tmdbtvapp.data.api.MovieApiNetworkService
import com.harsh.tmdbtvapp.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: MovieApiNetworkService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApiNetworkService::class.java)
    }
}