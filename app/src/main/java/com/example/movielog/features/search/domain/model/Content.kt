package com.example.movielog.features.search.domain.model

data class Content(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val type: ContentType,
    val totalSeasons: Int? = null,
    val releaseYear: String? = null,
    val rating: Double? = null
)