package com.example.movielog.features.detail.data.mapper

import com.example.movielog.core.config.ApiConfig
import com.example.movielog.features.detail.data.dto.JikanAnimeDetailDto
import com.example.movielog.features.detail.data.dto.TmdbMovieDetailDto
import com.example.movielog.features.detail.data.dto.TmdbTvDetailDto
import com.example.movielog.features.detail.domain.model.ContentDetail
import com.example.movielog.features.search.domain.model.ContentType

object DetailMapper {

    private const val TMDB_IMAGE_BASE_URL = ApiConfig.TMDB_IMAGE_BASE_URL
    private const val NO_DESCRIPTION = "No description available"

    fun mapMovieDetail(dto: TmdbMovieDetailDto): ContentDetail {
        return ContentDetail(
            id = dto.id.toString(),
            title = dto.title ?: "Unknown Title",
            imageUrl = dto.poster_path?.let { TMDB_IMAGE_BASE_URL + it },
            type = ContentType.MOVIE,
            releaseYear = dto.release_date?.take(4)?.ifBlank { null } ?: "N/A",
            overview = dto.overview?.ifBlank { null } ?: NO_DESCRIPTION,
            rating = dto.vote_average?.takeIf { it > 0.0 },
            genres = dto.genres.orEmpty().mapNotNull { it.name },
            runtime = dto.runtime?.takeIf { it > 0 }?.let { formatMinutes(it) },
            seasons = null,
            episodes = null,
            status = dto.status?.ifBlank { null },
            tagline = dto.tagline?.ifBlank { null }
        )
    }

    fun mapTvDetail(dto: TmdbTvDetailDto): ContentDetail {
        return ContentDetail(
            id = dto.id.toString(),
            title = dto.name ?: "Unknown Title",
            imageUrl = dto.poster_path?.let { TMDB_IMAGE_BASE_URL + it },
            type = ContentType.SERIES,
            releaseYear = dto.first_air_date?.take(4)?.ifBlank { null } ?: "N/A",
            overview = dto.overview?.ifBlank { null } ?: NO_DESCRIPTION,
            rating = dto.vote_average?.takeIf { it > 0.0 },
            genres = dto.genres.orEmpty().mapNotNull { it.name },
            runtime = dto.episode_run_time
                ?.firstOrNull()
                ?.takeIf { it > 0 }
                ?.let { "${formatMinutes(it)} per ep" },
            seasons = dto.number_of_seasons,
            episodes = dto.number_of_episodes,
            status = dto.status?.ifBlank { null },
            tagline = dto.tagline?.ifBlank { null }
        )
    }

    fun mapAnimeDetail(dto: JikanAnimeDetailDto): ContentDetail {
        val isMovie = dto.type.equals("Movie", ignoreCase = true)

        return ContentDetail(
            id = dto.mal_id.toString(),
            title = dto.title_english ?: dto.title ?: "Unknown Title",
            imageUrl = dto.images?.webp?.image_url,
            type = if (isMovie) ContentType.ANIME_MOVIE else ContentType.ANIME,
            releaseYear = dto.aired?.prop?.from?.year?.toString() ?: "N/A",
            overview = dto.synopsis?.ifBlank { null } ?: NO_DESCRIPTION,
            rating = dto.score?.takeIf { it > 0.0 },
            genres = dto.genres.orEmpty().mapNotNull { it.name },
            runtime = dto.duration?.ifBlank { null },
            seasons = null,
            episodes = if (isMovie) null else dto.episodes,
            status = dto.status?.ifBlank { null },
            tagline = null
        )
    }

    private fun formatMinutes(totalMinutes: Int): String {
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60

        return when {
            hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
            hours > 0 -> "${hours}h"
            else -> "${minutes}m"
        }
    }
}