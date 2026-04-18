package com.example.movielog.core.auth

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

object AuthManager {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val authState: StateFlow<FirebaseUser?> = _authState

    init {
        auth.addAuthStateListener {
            _authState.value = it.currentUser
        }
    }

    suspend fun login(email: String, password: String): AuthResult {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        auth.currentUser?.reload()?.await()
        return result
    }

    suspend fun register(email: String, password: String): AuthResult {
        return auth.createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun sendPasswordResetEmail() {
        val email = requireCurrentUser().email
            ?: throw IllegalStateException("Current user email not available")
        auth.sendPasswordResetEmail(email).await()
    }

    suspend fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun requireCurrentUser(): FirebaseUser {
        return auth.currentUser ?: throw IllegalStateException("User not logged in")
    }

    suspend fun updateDisplayName(displayName: String) {
        val user = requireCurrentUser()
        val request = userProfileChangeRequest {
            this.displayName = displayName
        }
        user.updateProfile(request).await()
        user.reload().await()
        _authState.value = auth.currentUser
    }

    suspend fun reauthenticate(password: String) {
        val user = requireCurrentUser()
        val email = user.email ?: throw IllegalStateException("Current user email not available")
        val credential = EmailAuthProvider.getCredential(email, password)
        user.reauthenticate(credential).await()
    }

    suspend fun deleteCurrentUser() {
        requireCurrentUser().delete().await()
        _authState.value = null
    }

    fun logout() {
        auth.signOut()
        _authState.value = null
    }
}
