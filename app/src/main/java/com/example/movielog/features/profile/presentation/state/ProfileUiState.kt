package com.example.movielog.features.profile.presentation.state

import com.example.movielog.features.profile.domain.model.UserProfile

data class ProfileUiState(
    val isLoading: Boolean = true,
    val profile: UserProfile? = null,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val message: String? = null,
    val error: String? = null
)
