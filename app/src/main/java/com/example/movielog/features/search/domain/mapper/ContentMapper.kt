package com.example.movielog.features.search.domain.mapper

import com.example.movielog.features.search.domain.model.Content
import com.example.movielog.features.search.domain.model.ContentSnapshot

fun Content.toSnapshot(): ContentSnapshot {
    return ContentSnapshot(
        id = id,
        title = title,
        imageUrl = imageUrl,
        type = type
    )
}