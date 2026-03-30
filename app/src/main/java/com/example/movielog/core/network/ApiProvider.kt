package com.example.movielog.core.network

import com.example.movielog.features.search.data.remote.JikanApiService
import com.example.movielog.features.search.data.remote.TmdbApiService

object ApiProvider {

    val tmdbApi: TmdbApiService =
        NetworkModule.tmdbRetrofit.create(TmdbApiService::class.java)

    val jikanApi: JikanApiService =
        NetworkModule.jikanRetrofit.create(JikanApiService::class.java)
}