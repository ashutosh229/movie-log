package com.example.movielog.features.search.data.remote

import com.example.movielog.core.network.ApiProvider
import com.example.movielog.features.search.data.dto.JikanResponse
import com.example.movielog.features.search.data.dto.TmdbMovieResponse
import com.example.movielog.features.search.data.dto.TmdbTvResponse

class SearchRemoteDataSource {

    private val tmdbApi = ApiProvider.tmdbApi
    private val jikanApi = ApiProvider.jikanApi

    // 🔑 Replace with your actual TMDB API key
    private val tmdbApiKey = "ea41e93397072c9e58b2602a197434b7"

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