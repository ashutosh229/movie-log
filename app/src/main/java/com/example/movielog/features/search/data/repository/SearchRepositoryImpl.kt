package com.example.movielog.features.search.data.repository

import com.example.movielog.features.search.data.mapper.ContentMapper
import com.example.movielog.features.search.data.remote.SearchRemoteDataSource
import com.example.movielog.features.search.domain.model.Content
import com.example.movielog.features.search.domain.repository.SearchRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope


class SearchRepositoryImpl(
    private val remoteDataSource: SearchRemoteDataSource
) : SearchRepository {
    override suspend fun searchContent(query: String): Result<List<Content>> {
        return try {
            coroutineScope {

                val moviesDeferred = async { remoteDataSource.searchMovies(query) }
                val tvDeferred = async { remoteDataSource.searchTvSeries(query) }
                val animeDeferred = async { remoteDataSource.searchAnime(query) }

                val movies = moviesDeferred.await().results.map {
                    ContentMapper.mapMovieDtoToContent(it)
                }

                val series = tvDeferred.await().results.map {
                    ContentMapper.mapTvDtoToContent(it)
                }

                val anime = animeDeferred.await().data.map {
                    ContentMapper.mapAnimeDtoToContent(it)
                }

                val combined = (movies + series + anime)
                    .sortedByDescending { it.rating ?: 0.0 }

                Result.success(combined)
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}