package com.example.movielog.features.search.domain.model

data class Content(
    val id: String,
    val title: String,
//    TODO: for now, nullability handled from UI, later design placeholder
    val imageUrl: String?,
    val releaseYear: String,
//  TODO: later, we will se whether to write default values or not
    val description: String,
    val language: String,
    val type: ContentType,
)