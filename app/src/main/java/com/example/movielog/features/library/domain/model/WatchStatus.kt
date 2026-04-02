package com.example.movielog.features.library.domain.model

enum class WatchStatus {
    COMPLETED,
    ONGOING,
    TO_WATCH,
    REPOSITORY
}

//TODO: Migrate the utility later
fun WatchStatus.displayName(): String {
    return when (this) {
        WatchStatus.TO_WATCH -> "To Watch"
        WatchStatus.ONGOING -> "Ongoing"
        WatchStatus.COMPLETED -> "Completed"
        WatchStatus.REPOSITORY -> "Repository"
    }
}