package com.example.movielog.features.library.domain.mapper

import com.example.movielog.features.library.domain.model.UserContent
import com.example.movielog.features.library.domain.model.WatchStatus
import com.example.movielog.features.search.domain.model.ContentSnapshot

fun ContentSnapshot.toUserContent(
    status: WatchStatus
): UserContent {

    return UserContent(
        id = id,
        title = title,
        imageUrl = imageUrl,
        type = type,
        status = status,
        progress = null
    )
}