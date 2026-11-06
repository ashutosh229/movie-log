package com.example.movielog.features.detail.data.repository

import com.example.movielog.features.detail.data.mapper.DetailMapper
import com.example.movielog.features.detail.data.remote.DetailRemoteDataSource
import com.example.movielog.features.detail.domain.model.ContentDetail
import com.example.movielog.features.detail.domain.repository.DetailRepository
import com.example.movielog.features.search.domain.model.ContentType
import kotlinx.coroutines.CancellationException

class DetailRepositoryImpl(
    private val remoteDataSource: DetailRemoteDataSource
) : DetailRepository {

    override suspend fun getContentDetail(
        contentId: String,
        contentType: ContentType
    ): Result<ContentDetail> {

        val id = contentId.toIntOrNull()
            ?: return Result.failure(IllegalArgumentException("Invalid content id"))

        return try {
            val detail = when (contentType) {
                ContentType.MOVIE -> {
                    DetailMapper.mapMovieDetail(remoteDataSource.getMovieDetail(id))
                }

                ContentType.SERIES -> {
                    DetailMapper.mapTvDetail(remoteDataSource.getTvDetail(id))
                }

                ContentType.ANIME,
                ContentType.ANIME_MOVIE -> {
                    DetailMapper.mapAnimeDetail(remoteDataSource.getAnimeDetail(id).data)
                }
            }

            Result.success(detail)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}