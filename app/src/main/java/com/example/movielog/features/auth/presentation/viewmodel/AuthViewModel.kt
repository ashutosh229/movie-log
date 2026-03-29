package com.example.movielog.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielog.features.auth.domain.model.User
import com.example.movielog.features.auth.domain.repository.AuthRepository
import com.example.movielog.features.auth.presentation.state.AuthState
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    var authState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    fun register(email: String, password: String) {
        viewModelScope.launch {
            authState = AuthState.Loading

            val result = repository.register(email, password)

            authState = result.fold(
                onSuccess = { AuthState.Success(it) },
                onFailure = { AuthState.Error(it.message ?: "Registration failed") }
            )
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authState = AuthState.Loading

            val result = repository.login(email, password)

            authState = result.fold(
                onSuccess = { AuthState.Success(it) },
                onFailure = { AuthState.Error(it.message ?: "Login failed") }
            )
        }
    }

    fun getCurrentUser(): User? = repository.getCurrentUser()

    fun logout() = repository.logout()
}