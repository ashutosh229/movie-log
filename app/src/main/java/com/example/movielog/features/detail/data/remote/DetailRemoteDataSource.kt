package com.example.movielog.features.detail.data.remote

import com.example.movielog.BuildConfig
import com.example.movielog.core.network.ApiProvider
import com.example.movielog.features.detail.data.dto.JikanDetailResponse
import com.example.movielog.features.detail.data.dto.TmdbMovieDetailDto
import com.example.movielog.features.detail.data.dto.TmdbTvDetailDto

class DetailRemoteDataSource {

    private val tmdbApi = ApiProvider.tmdbApi
    private val jikanApi = ApiProvider.jikanApi

    private val tmdbApiKey = BuildConfig.TMDB_API_KEY

    suspend fun getMovieDetail(id: Int): TmdbMovieDetailDto {
        return tmdbApi.getMovieDetail(id, tmdbApiKey)
    }

    suspend fun getTvDetail(id: Int): TmdbTvDetailDto {
        return tmdbApi.getTvDetail(id, tmdbApiKey)
    }

    suspend fun getAnimeDetail(id: Int): JikanDetailResponse {
        return jikanApi.getAnimeDetail(id)
    }
}