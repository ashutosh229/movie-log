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

//TODO: Migrate this to other like mapper or utils
fun Progress.toReadableString(): String {
    return when (this) {
        is Progress.MovieProgress -> "At ${timestamp}s"
        is Progress.EpisodeProgress -> "S$season E$episode @ ${timestamp}s"
    }
}