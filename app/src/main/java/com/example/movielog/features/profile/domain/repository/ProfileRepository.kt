package com.example.movielog.features.profile.domain.repository

import com.example.movielog.features.profile.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeProfile(): Flow<UserProfile>

    suspend fun updateProfile(displayName: String): Result<Unit>

    suspend fun deleteAccount(password: String): Result<Unit>
}
