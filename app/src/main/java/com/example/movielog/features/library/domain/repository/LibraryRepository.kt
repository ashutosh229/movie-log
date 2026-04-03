package com.example.movielog.features.library.domain.repository

import com.example.movielog.features.library.domain.model.UserContent
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {

    suspend fun addOrUpdateContent(userContent: UserContent): Result<Unit>

    fun observeAllContent(): Flow<List<UserContent>>

    suspend fun deleteContent(contentId: String): Result<Unit>
}