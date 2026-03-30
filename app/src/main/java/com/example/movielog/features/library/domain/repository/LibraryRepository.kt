package com.example.movielog.features.library.domain.repository

import com.example.movielog.features.library.domain.model.UserContent

interface LibraryRepository {

    suspend fun addOrUpdateContent(userContent: UserContent): Result<Unit>

    suspend fun getAllContent(): Result<List<UserContent>>

    suspend fun deleteContent(contentId: String): Result<Unit>
}