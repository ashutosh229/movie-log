package com.example.movielog.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielog.core.auth.AuthManager
import com.example.movielog.features.auth.domain.model.User
import com.example.movielog.features.auth.domain.repository.AuthRepository
import com.example.movielog.features.auth.presentation.state.AuthState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    var authState: AuthState = AuthState.Loading
        private set

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            AuthManager.authState.collectLatest { firebaseUser ->
                authState = if (firebaseUser != null) {
                    AuthState.Success(
                        User(
                            uid = firebaseUser.uid,
                            email = firebaseUser.email
                        )
                    )
                } else {
                    AuthState.Idle
                }
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            authState = AuthState.Loading
            repository.register(email, password)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authState = AuthState.Loading
            repository.login(email, password)
        }
    }

    fun sendPasswordResetEmail(
        email: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            repository.sendPasswordResetEmail(email.trim())
                .onSuccess {
                    onSuccess()
                }
                .onFailure { throwable ->
                    onFailure(throwable.message ?: "Unable to send password reset email.")
                }
        }
    }

    fun logout() {
        repository.logout()
    }
}
