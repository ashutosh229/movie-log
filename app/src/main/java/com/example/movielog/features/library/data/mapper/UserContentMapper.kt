package com.example.movielog.features.library.data.mapper

import com.example.movielog.features.library.domain.model.Progress
import com.example.movielog.features.library.domain.model.UserContent
import com.example.movielog.features.library.domain.model.WatchStatus
import com.example.movielog.features.search.domain.model.ContentType

//TODO: When updating the entry, updated time should be logged, use the time module functions
object UserContentMapper {

    // 🔹 DOMAIN → FIRESTORE MAP
    fun toMap(userContent: UserContent): Map<String, Any> {
        val baseMap = mutableMapOf<String, Any>(
            "id" to userContent.id,
            "title" to userContent.title,
            "imageUrl" to (userContent.imageUrl ?: ""),
            "type" to userContent.type.name,
            "status" to userContent.status.name,
            "createdAt" to userContent.createdAt,
            "updatedAt" to userContent.updatedAt
        )

        // 🔥 Flatten progress
        userContent.progress?.let { progress ->
            when (progress) {

                is Progress.MovieProgress -> {
                    baseMap["timestamp"] = progress.timestamp
                }

                is Progress.EpisodeProgress -> {
                    baseMap["season"] = progress.season
                    baseMap["episode"] = progress.episode
                    baseMap["timestamp"] = progress.timestamp
                }
            }
        }

        return baseMap
    }

    // 🔹 FIRESTORE MAP → DOMAIN
    fun fromMap(map: Map<String, Any>): UserContent {

        val type = ContentType.valueOf(map["type"] as String)
        val status = WatchStatus.valueOf(map["status"] as String)

        val progress = when (type) {

            ContentType.MOVIE -> {
                (map["timestamp"] as? Long)?.let {
                    Progress.MovieProgress(it)
                }
            }

            ContentType.ANIME,
            ContentType.SERIES -> {
                val season = (map["season"] as? Long)?.toInt()
                val episode = (map["episode"] as? Long)?.toInt()
                val timestamp = (map["timestamp"] as? Long)

                if (season != null && episode != null && timestamp != null) {
                    Progress.EpisodeProgress(
                        season = season,
                        episode = episode,
                        timestamp = timestamp
                    )
                } else null
            }
        }

        return UserContent(
            id = map["id"] as String,
            title = map["title"] as String,
            imageUrl = (map["imageUrl"] as String).ifEmpty { null },
            type = type,
            status = status,
            progress = progress,
            createdAt = map["createdAt"] as Long,
            updatedAt = map["updatedAt"] as Long
        )
    }
}