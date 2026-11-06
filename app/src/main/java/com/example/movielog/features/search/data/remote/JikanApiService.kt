package com.example.movielog.features.search.data.remote

import com.example.movielog.features.detail.data.dto.JikanDetailResponse
import com.example.movielog.features.search.data.dto.JikanResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanApiService {

    @GET("anime")
    suspend fun searchAnime(
        @Query("q") query: String
    ): JikanResponse

    @GET("anime/{id}")
    suspend fun getAnimeDetail(
        @Path("id") id: Int
    ): JikanDetailResponse
}