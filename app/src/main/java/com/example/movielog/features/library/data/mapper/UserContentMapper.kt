package com.example.movielog.features.library.data.mapper

import com.example.movielog.features.library.domain.model.Progress
import com.example.movielog.features.library.domain.model.UserContent
import com.example.movielog.features.library.domain.model.WatchStatus
import com.example.movielog.features.search.domain.model.ContentType

//TODO: dyanamic data show should be there in library
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

            val progressMap = when (progress) {

                is Progress.MovieProgress -> mapOf(
                    "type" to "MOVIE",
                    "timestamp" to progress.timestamp
                )

                is Progress.EpisodeProgress -> mapOf(
                    "type" to "EPISODE",
                    "season" to progress.season,
                    "episode" to progress.episode,
                    "timestamp" to progress.timestamp
                )
            }

            baseMap["progress"] = progressMap
        }

        return baseMap
    }

    // 🔹 FIRESTORE MAP → DOMAIN
    fun fromMap(map: Map<String, Any>): UserContent {

        val type = ContentType.valueOf(map["type"] as String)
        val status = WatchStatus.valueOf(map["status"] as String)

        val progressMap = map["progress"] as? Map<*, *>

        val progress = progressMap?.let {

            val type = it["type"] as? String

            when (type) {

                "MOVIE" -> {
                    val timestamp = it["timestamp"] as? Long ?: 0L
                    Progress.MovieProgress(timestamp)
                }

                "EPISODE" -> {
                    val season = (it["season"] as? Long)?.toInt()
                    val episode = (it["episode"] as? Long)?.toInt()
                    val timestamp = it["timestamp"] as? Long

                    if (season != null && episode != null && timestamp != null) {
                        Progress.EpisodeProgress(season, episode, timestamp)
                    } else null
                }

                else -> null
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