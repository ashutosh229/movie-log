package com.example.movielog.features.analytics.domain.calculator

import com.example.movielog.features.analytics.domain.model.LibraryAnalytics
import com.example.movielog.features.library.domain.model.UserContent
import com.example.movielog.features.library.domain.model.WatchStatus
import com.example.movielog.features.search.domain.model.ContentType

object AnalyticsCalculator {

    private const val RECENT_ITEMS_LIMIT = 5

    fun calculate(
        content: List<UserContent>
    ): LibraryAnalytics {

        val totalTitles = content.size

        val completedTitles = content.count {
            it.status == WatchStatus.COMPLETED
        }

        val ongoingTitles = content.count {
            it.status == WatchStatus.ONGOING
        }

        val toWatchTitles = content.count {
            it.status == WatchStatus.TO_WATCH
        }

        val repositoryTitles = content.count {
            it.status == WatchStatus.REPOSITORY
        }

        val movieCount = content.count {
            it.type == ContentType.MOVIE
        }

        val seriesCount = content.count {
            it.type == ContentType.SERIES
        }

        val animeCount = content.count {
            it.type == ContentType.ANIME
        }

        val completionPercentage = if (totalTitles == 0) {
            0f
        } else {
            completedTitles.toFloat() / totalTitles.toFloat()
        }

        val trackedProgressCount = content.count {
            it.progress != null
        }

        val recentlyAdded = content
            .sortedByDescending { it.createdAt }
            .take(RECENT_ITEMS_LIMIT)

        val recentlyUpdated = content
            .sortedByDescending { it.updatedAt }
            .take(RECENT_ITEMS_LIMIT)

        val titlesWithProgress = content.count {
            it.progress != null
        }

        return LibraryAnalytics(
            totalTitles = totalTitles,
            completedTitles = completedTitles,
            ongoingTitles = ongoingTitles,
            toWatchTitles = toWatchTitles,
            repositoryTitles = repositoryTitles,
            movieCount = movieCount,
            seriesCount = seriesCount,
            animeCount = animeCount,
            completionPercentage = completionPercentage,
            titlesWithProgress = titlesWithProgress,
            recentlyAdded = recentlyAdded,
            recentlyUpdated = recentlyUpdated
        )
    }
}