package com.example.movielog.core.auth

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

object AuthManager {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // 🔥 Reactive auth state
    private val _authState = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val authState: StateFlow<FirebaseUser?> = _authState

    init {
        auth.addAuthStateListener {
            _authState.value = it.currentUser
        }
    }

    // ✅ FIXED (use await)
    suspend fun login(email: String, password: String): AuthResult {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        auth.currentUser?.reload()?.await()
//        TODO: is it fine to use reload
        return result
    }

    // ✅ FIXED (use await)
    suspend fun register(email: String, password: String): AuthResult {
        return auth.createUserWithEmailAndPassword(email, password).await()
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun logout() {
        auth.signOut()
        _authState.value = null
    }
}