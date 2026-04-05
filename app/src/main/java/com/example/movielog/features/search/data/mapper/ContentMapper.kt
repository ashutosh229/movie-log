package com.example.movielog.features.search.data.mapper

import com.example.movielog.features.search.data.dto.JikanAnimeDto
import com.example.movielog.features.search.data.dto.TmdbMovieDto
import com.example.movielog.features.search.data.dto.TmdbTvDto
import com.example.movielog.features.search.domain.model.Content
import com.example.movielog.features.search.domain.model.ContentType

object ContentMapper {

    //    TODO: Move this to config file
    private const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

    fun mapMovieDtoToContent(dto: TmdbMovieDto): Content {
        return Content(
            id = dto.id.toString(),
            title = dto.title ?: "Unknown Title",
            imageUrl = dto.poster_path?.let { TMDB_IMAGE_BASE_URL + it },
            releaseYear = dto.release_date?.take(4) ?: "N/A",
            description = dto.overview ?: "No description available",
            language = dto.original_language ?: "Unknown",
            type = ContentType.MOVIE,
//            totalSeasons = null, // TODO: Need to think of some other number due to analytics
        )
    }

    fun mapTvDtoToContent(dto: TmdbTvDto): Content {
        return Content(
            id = dto.id.toString(),
            title = dto.name ?: "Unknown Title",
            imageUrl = dto.poster_path?.let { TMDB_IMAGE_BASE_URL + it },
            releaseYear = dto.first_air_date?.take(4) ?: "N/A",
            description = dto.overview ?: "No description available",
            language = dto.original_language ?: "Unknown",
            type = ContentType.SERIES,
//            totalSeasons = null, // TODO: TMDB search API doesn’t give this directly
        )
    }

    fun mapAnimeDtoToContent(dto: JikanAnimeDto): Content {
        return Content(
            id = dto.mal_id.toString(),
            title = dto.title_english ?: dto.title ?: "Unknown Title",
            imageUrl = dto.images?.webp?.image_url,
            releaseYear = dto.aired?.prop?.from?.year?.toString() ?: "N/A",
            description = dto.synopsis ?: "No description available",
            language = "JP", //TODO: we do not get this directly.
            type = if (dto.type == "Movie") {
                ContentType.MOVIE
            } else {
                ContentType.ANIME
            },
//            totalSeasons = if (dto.type == "Movie") {
//                null // TODO: Need to think of some other number due to analytics
//            } else {
//                dto.episodes
//            }
        )
    }
}