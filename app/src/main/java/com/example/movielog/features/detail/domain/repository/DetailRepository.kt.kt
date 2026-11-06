package com.example.movielog.features.detail.domain.repository

import com.example.movielog.features.detail.domain.model.ContentDetail
import com.example.movielog.features.search.domain.model.ContentType

interface DetailRepository {

    suspend fun getContentDetail(
        contentId: String,
        contentType: ContentType
    ): Result<ContentDetail>
}