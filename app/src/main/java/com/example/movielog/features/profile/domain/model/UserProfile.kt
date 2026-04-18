package com.example.movielog.features.profile.domain.model

data class UserProfile(
    val uid: String,
    val email: String,
    val displayName: String,
    val createdAt: Long? = null,
    val updatedAt: Long? = null
)
