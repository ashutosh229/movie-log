package com.example.movielog.features.detail.domain.model

import com.example.movielog.features.search.domain.model.ContentType

data class ContentDetail(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val type: ContentType,
    val releaseYear: String,
    val overview: String,
    val rating: Double?,
    val genres: List<String>,
    val runtime: String?,
    val seasons: Int?,
    val episodes: Int?,
    val status: String?,
    val tagline: String?
)