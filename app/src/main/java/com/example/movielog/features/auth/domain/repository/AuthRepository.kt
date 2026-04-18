package com.example.movielog.features.auth.domain.repository

import com.example.movielog.features.auth.domain.model.User

interface AuthRepository {

    suspend fun register(
        email: String,
        password: String
    ): Result<User>

    suspend fun login(
        email: String,
        password: String
    ): Result<User>

    suspend fun sendPasswordResetEmail(email: String): Result<Unit>

    fun getCurrentUser(): User?

    fun logout()
}
