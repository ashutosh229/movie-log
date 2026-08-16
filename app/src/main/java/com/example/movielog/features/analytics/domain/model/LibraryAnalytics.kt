package com.example.movielog.features.analytics.domain.model

import com.example.movielog.features.library.domain.model.UserContent

data class LibraryAnalytics(
    val totalTitles: Int,

    val completedTitles: Int,
    val ongoingTitles: Int,
    val toWatchTitles: Int,
    val repositoryTitles: Int,

    val movieCount: Int,
    val seriesCount: Int,
    val animeCount: Int,

    val completionPercentage: Float,

    val titlesWithProgress: Int,

    val recentlyAdded: List<UserContent>,
    val recentlyUpdated: List<UserContent>
)