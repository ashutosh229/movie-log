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

    // 🔥 FIXED
//    TODO: firestore instance ka dekhna hain woh multiuser same data wala issue
//    TODO: is function ke uses dekhne hain and unko hatana hain
    override fun getCurrentUser(): User? {
        val uid = dataSource.getCurrentUserId()

        return uid?.let {
            User(
                uid = it,
                email = null  // ⚠️ Firebase doesn't expose email here safely without user object
//                TODO: when there will be use of email, think something
            )
        }
    }

    override fun logout() {
        dataSource.logout()
    }
}