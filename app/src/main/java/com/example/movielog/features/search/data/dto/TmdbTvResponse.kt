package com.example.movielog.features.search.data.dto

data class TmdbTvResponse(
    val results: List<TmdbTvDto>
)

data class TmdbTvDto(
    val id: Int,
    val name: String,
    val poster_path: String?,
    val first_air_date: String?,
    val vote_average: Double?
)