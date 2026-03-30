package com.example.movielog.features.search.domain.repository

import com.example.movielog.features.search.domain.model.Content

interface SearchRepository {

    suspend fun searchContent(query: String): Result<List<Content>>
}