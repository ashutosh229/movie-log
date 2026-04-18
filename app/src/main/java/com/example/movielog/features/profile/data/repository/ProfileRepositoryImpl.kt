package com.example.movielog.features.profile.data.repository

import com.example.movielog.core.auth.AuthManager
import com.example.movielog.features.profile.data.remote.ProfileRemoteDataSource
import com.example.movielog.features.profile.domain.model.UserProfile
import com.example.movielog.features.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class ProfileRepositoryImpl(
    private val remoteDataSource: ProfileRemoteDataSource
) : ProfileRepository {

    override fun observeProfile(): Flow<UserProfile> {
        return remoteDataSource.observeProfile()
            .onEach { profile ->
                remoteDataSource.ensureProfile(profile)
            }
    }

    override suspend fun updateProfile(displayName: String): Result<Unit> {
        return try {
            remoteDataSource.updateProfile(displayName)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAccount(password: String): Result<Unit> {
        return try {
            AuthManager.reauthenticate(password)
            remoteDataSource.deleteAllUserData()
            AuthManager.deleteCurrentUser()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
