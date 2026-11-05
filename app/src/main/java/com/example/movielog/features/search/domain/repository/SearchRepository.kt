package com.example.movielog.features.search.domain.repository

import com.example.movielog.features.search.domain.model.Content
import com.example.movielog.features.search.domain.model.ContentType

interface SearchRepository {

    suspend fun searchContent(
        query: String,
        contentType: ContentType? = null
    ): Result<List<Content>>
}
