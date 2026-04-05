package com.example.movielog.features.search.data.dto

data class TmdbMovieResponse(
    val results: List<TmdbMovieDto>
)

data class TmdbMovieDto(
    val id: Int,
    val title: String?,
    val poster_path: String?,
    val release_date: String?,
    val overview: String?,
    val original_language: String?
)




