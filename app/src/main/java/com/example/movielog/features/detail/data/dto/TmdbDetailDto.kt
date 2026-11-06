package com.example.movielog.features.detail.data.dto

data class TmdbGenreDto(
    val id: Int?,
    val name: String?
)

data class TmdbMovieDetailDto(
    val id: Int,
    val title: String?,
    val poster_path: String?,
    val release_date: String?,
    val overview: String?,
    val runtime: Int?,
    val vote_average: Double?,
    val genres: List<TmdbGenreDto>?,
    val tagline: String?,
    val status: String?
)

data class TmdbTvDetailDto(
    val id: Int,
    val name: String?,
    val poster_path: String?,
    val first_air_date: String?,
    val overview: String?,
    val episode_run_time: List<Int>?,
    val number_of_seasons: Int?,
    val number_of_episodes: Int?,
    val vote_average: Double?,
    val genres: List<TmdbGenreDto>?,
    val tagline: String?,
    val status: String?
)