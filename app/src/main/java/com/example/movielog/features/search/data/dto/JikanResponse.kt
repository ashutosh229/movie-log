package com.example.movielog.features.search.data.dto

data class JikanResponse(
    val data: List<JikanAnimeDto>
)

data class JikanAnimeDto(
    val mal_id: Int,
    val title_english: String?,
    val title: String?,
    val images: JikanImages?,
    val aired: airedInformation?,
    val synopsis: String?,
    val episodes: Int?,
    val score: Double?,
    val type: String?,
)

data class airedInformation(
    val prop: FromToInformation?
)

data class FromToInformation(
    val from: FromInformation?,
    val to: ToInformation?
)

data class FromInformation(
    val day: Int?,
    val month: Int?,
    val year: Int?,
)

data class ToInformation(
    val day: Int?,
    val month: Int?,
    val year: Int?,
)

data class JikanImages(
    val webp: JikanImageUrl?
)

data class JikanImageUrl(
    val image_url: String?
)
