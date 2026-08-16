package com.example.movielog.features.search.data.remote

import com.example.movielog.BuildConfig

import com.example.movielog.core.network.ApiProvider
import com.example.movielog.features.search.data.dto.JikanResponse
import com.example.movielog.features.search.data.dto.TmdbMovieResponse
import com.example.movielog.features.search.data.dto.TmdbTvResponse

class SearchRemoteDataSource {

    private val tmdbApi = ApiProvider.tmdbApi
    private val jikanApi = ApiProvider.jikanApi

    private val tmdbApiKey = BuildConfig.TMDB_API_KEY

    suspend fun searchMovies(query: String): TmdbMovieResponse {
        return tmdbApi.searchMovies(query, tmdbApiKey)
    }

    suspend fun searchTvSeries(query: String): TmdbTvResponse {
        return tmdbApi.searchTvSeries(query, tmdbApiKey)
    }

    suspend fun searchAnime(query: String): JikanResponse {
        return jikanApi.searchAnime(query)
    }
}