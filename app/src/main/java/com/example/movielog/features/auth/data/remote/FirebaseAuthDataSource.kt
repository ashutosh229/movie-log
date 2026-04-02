package com.example.movielog.features.auth.data.remote

import com.example.movielog.core.auth.AuthManager
import com.google.firebase.auth.AuthResult

class FirebaseAuthDataSource {

    suspend fun register(email: String, password: String): AuthResult {
        return AuthManager.register(email, password)
    }

    suspend fun login(email: String, password: String): AuthResult {
        return AuthManager.login(email, password)
    }

    fun getCurrentUserId(): String? {
        return AuthManager.getCurrentUserId()
    }

//    fun isLoggedIn(): Boolean {
//        return AuthManager.isLoggedIn()
//    }

    fun logout() {
        AuthManager.logout()
    }
}