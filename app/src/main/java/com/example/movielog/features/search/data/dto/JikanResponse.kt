package com.example.movielog.features.search.data.dto

data class JikanResponse(
    val data: List<JikanAnimeDto>
)

data class JikanAnimeDto(
    val mal_id: Int,
    val title: String,
    val images: JikanImages,
    val episodes: Int?,
    val score: Double?
)

data class JikanImages(
    val jpg: JikanImageUrl
)

data class JikanImageUrl(
    val image_url: String?
)
