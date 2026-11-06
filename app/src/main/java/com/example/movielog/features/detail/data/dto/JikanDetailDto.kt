package com.example.movielog.features.detail.data.dto

import com.example.movielog.features.search.data.dto.JikanImages
import com.example.movielog.features.search.data.dto.airedInformation

data class JikanDetailResponse(
    val data: JikanAnimeDetailDto
)

data class JikanGenreDto(
    val name: String?
)

data class JikanAnimeDetailDto(
    val mal_id: Int,
    val title_english: String?,
    val title: String?,
    val images: JikanImages?,
    val aired: airedInformation?,
    val synopsis: String?,
    val episodes: Int?,
    val score: Double?,
    val type: String?,
    val status: String?,
    val duration: String?,
    val genres: List<JikanGenreDto>?
)