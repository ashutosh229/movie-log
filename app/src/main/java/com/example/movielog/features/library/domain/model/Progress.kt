package com.example.movielog.features.library.domain.model

sealed class Progress {

    data class MovieProgress(
        val timestamp: Long // in seconds
    ) : Progress()

    data class EpisodeProgress(
        val season: Int,
        val episode: Int,
        val timestamp: Long
    ) : Progress()
}