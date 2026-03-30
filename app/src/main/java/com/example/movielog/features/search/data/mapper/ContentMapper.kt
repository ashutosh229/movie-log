package com.example.movielog.features.search.data.mapper

import com.example.movielog.features.search.data.dto.JikanAnimeDto
import com.example.movielog.features.search.data.dto.TmdbMovieDto
import com.example.movielog.features.search.data.dto.TmdbTvDto
import com.example.movielog.features.search.domain.model.Content
import com.example.movielog.features.search.domain.model.ContentType

object ContentMapper {

    private const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

    fun mapMovieDtoToContent(dto: TmdbMovieDto): Content {
        return Content(
            id = dto.id.toString(),
            title = dto.title,
            imageUrl = dto.poster_path?.let { TMDB_IMAGE_BASE_URL + it },
            type = ContentType.MOVIE,
            totalSeasons = null,
            releaseYear = dto.release_date?.take(4),
            rating = dto.vote_average
        )
    }

    fun mapTvDtoToContent(dto: TmdbTvDto): Content {
        return Content(
            id = dto.id.toString(),
            title = dto.name,
            imageUrl = dto.poster_path?.let { TMDB_IMAGE_BASE_URL + it },
            type = ContentType.SERIES,
            totalSeasons = null, // TMDB search API doesn’t give this directly
            releaseYear = dto.first_air_date?.take(4),
            rating = dto.vote_average
        )
    }

    fun mapAnimeDtoToContent(dto: JikanAnimeDto): Content {
        return Content(
            id = dto.mal_id.toString(),
            title = dto.title,
            imageUrl = dto.images.jpg.image_url,
            type = ContentType.ANIME,
            totalSeasons = null, // handled later in detail screen (future)
            releaseYear = null,
            rating = dto.score
        )
    }
}