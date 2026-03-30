package com.example.movielog.features.search.data.remote

import com.example.movielog.features.search.data.dto.JikanResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface JikanApiService {

    @GET("anime")
    suspend fun searchAnime(
        @Query("q") query: String
    ): JikanResponse
}