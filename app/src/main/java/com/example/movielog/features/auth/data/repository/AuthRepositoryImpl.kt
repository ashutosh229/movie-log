package com.example.movielog.features.auth.data.repository

import com.example.movielog.features.auth.data.remote.FirebaseAuthDataSource
import com.example.movielog.features.auth.domain.model.User
import com.example.movielog.features.auth.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val dataSource: FirebaseAuthDataSource
) : AuthRepository {

    override suspend fun register(email: String, password: String): Result<User> {
        return try {
            val result = dataSource.register(email, password)
            val firebaseUser = result.user!!

            Result.success(
                User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val result = dataSource.login(email, password)
            val firebaseUser = result.user!!

            Result.success(
                User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            dataSource.sendPasswordResetEmail(email)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): User? {
        val uid = dataSource.getCurrentUserId()

        return uid?.let {
            User(
                uid = it,
                email = null
            )
        }
    }

    override fun logout() {
        dataSource.logout()
    }
}
