package com.example.movielog.features.profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielog.core.auth.AuthManager
import com.example.movielog.features.profile.domain.repository.ProfileRepository
import com.example.movielog.features.profile.presentation.state.ProfileUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private var observeJob: Job? = null

    fun startObserving() {
        if (observeJob != null) return

        observeJob = viewModelScope.launch {
            AuthManager.authState.collect { user ->
                if (user == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            profile = null,
                            error = null
                        )
                    }
                    return@collect
                }

                _uiState.update {
                    it.copy(
                        isLoading = true,
                        error = null
                    )
                }

                repository.observeProfile()
                    .catch { throwable ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = throwable.message ?: "Unable to load profile."
                            )
                        }
                    }
                    .collect { profile ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                profile = profile,
                                error = null
                            )
                        }
                    }
            }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null, error = null) }
    }

    fun updateProfile(displayName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUpdating = true, message = null, error = null) }

            repository.updateProfile(displayName.trim())
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            message = "Profile updated successfully."
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            error = throwable.message ?: "Unable to update profile."
                        )
                    }
                }
        }
    }

    fun deleteAccount(password: String, onDeleted: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true, message = null, error = null) }

            repository.deleteAccount(password)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            message = "Account deleted successfully."
                        )
                    }
                    onDeleted()
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            error = throwable.message ?: "Unable to delete account."
                        )
                    }
                }
        }
    }
}
