package com.example.movielog.features.search.data.remote

import com.example.movielog.features.detail.data.dto.TmdbMovieDetailDto
import com.example.movielog.features.detail.data.dto.TmdbTvDetailDto
import com.example.movielog.features.search.data.dto.TmdbMovieResponse
import com.example.movielog.features.search.data.dto.TmdbTvResponse
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): TmdbMovieDetailDto

    @GET("tv/{id}")
    suspend fun getTvDetail(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): TmdbTvDetailDto
}