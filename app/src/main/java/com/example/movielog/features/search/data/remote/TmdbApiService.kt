package com.example.movielog.features.search.data.remote

import com.example.movielog.features.search.data.dto.TmdbMovieResponse
import com.example.movielog.features.search.data.dto.TmdbTvResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApiService {

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") apiKey: String
    ): TmdbMovieResponse

    @GET("search/tv")
    suspend fun searchTvSeries(
        @Query("query") query: String,
        @Query("api_key") apiKey: String
    ): TmdbTvResponse
}