package com.example.movielog.features.search.domain.model

data class ContentSnapshot(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val type: ContentType
)