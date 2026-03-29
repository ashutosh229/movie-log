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

    fun getCurrentUser(): User?

    fun logout()
}