package com.example.movielog.features.library.domain.model

import com.example.movielog.features.search.domain.model.ContentType

data class UserContent(
    val id: String,                 // same as contentId
    val title: String,
    val imageUrl: String?,
    val type: ContentType,

    val status: WatchStatus,

    val progress: Progress? = null,

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)