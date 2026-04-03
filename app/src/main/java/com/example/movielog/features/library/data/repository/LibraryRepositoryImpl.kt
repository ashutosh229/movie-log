package com.example.movielog.features.library.data.repository

import com.example.movielog.features.library.data.mapper.UserContentMapper
import com.example.movielog.features.library.data.remote.LibraryRemoteDataSource
import com.example.movielog.features.library.domain.model.UserContent
import com.example.movielog.features.library.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LibraryRepositoryImpl(
    private val remoteDataSource: LibraryRemoteDataSource
) : LibraryRepository {

    override suspend fun addOrUpdateContent(userContent: UserContent): Result<Unit> {
        return try {
            val map = UserContentMapper.toMap(userContent)
            remoteDataSource.addOrUpdateContent(map)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 🔥 REACTIVE FLOW MAPPING
    override fun observeAllContent(): Flow<List<UserContent>> {
        return remoteDataSource.observeAllContent()
            .map { list ->
                list.map { UserContentMapper.fromMap(it) }
            }
    }

    override suspend fun deleteContent(contentId: String): Result<Unit> {
        return try {
            remoteDataSource.deleteContent(contentId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}