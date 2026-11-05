package com.example.movielog.features.search.data.repository

import com.example.movielog.core.utils.calculateRelevance
import com.example.movielog.features.search.data.mapper.ContentMapper
import com.example.movielog.features.search.data.remote.SearchRemoteDataSource
import com.example.movielog.features.search.domain.model.Content
import com.example.movielog.features.search.domain.model.ContentType
import com.example.movielog.features.search.domain.repository.SearchRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class SearchRepositoryImpl(
    private val remoteDataSource: SearchRemoteDataSource
) : SearchRepository {

    override suspend fun searchContent(
        query: String,
        contentType: ContentType?
    ): Result<List<Content>> {

        return try {

            val results = when (contentType) {

                ContentType.MOVIE -> {
                    remoteDataSource.searchMovies(query)
                        .results
                        .map { ContentMapper.mapMovieDtoToContent(it) }
                }

                ContentType.SERIES -> {
                    remoteDataSource.searchTvSeries(query)
                        .results
                        .map { ContentMapper.mapTvDtoToContent(it) }
                }

                ContentType.ANIME_MOVIE,
                ContentType.ANIME -> {
                    remoteDataSource.searchAnime(query)
                        .data
                        .map { ContentMapper.mapAnimeDtoToContent(it) }
                }

                null -> {
                    coroutineScope {

                        val moviesDeferred = async {
                            remoteDataSource.searchMovies(query)
                        }

                        val seriesDeferred = async {
                            remoteDataSource.searchTvSeries(query)
                        }

                        val animeDeferred = async {
                            remoteDataSource.searchAnime(query)
                        }

                        val movies = moviesDeferred.await()
                            .results
                            .map { ContentMapper.mapMovieDtoToContent(it) }

                        val series = seriesDeferred.await()
                            .results
                            .map { ContentMapper.mapTvDtoToContent(it) }

                        val anime = animeDeferred.await()
                            .data
                            .map { ContentMapper.mapAnimeDtoToContent(it) }

                        movies + series + anime
                    }
                }
            }

            val sortedResults = results
                .map { content ->
                    content to calculateRelevance(query, content.title)
                }
                .sortedByDescending { it.second }
                .map { it.first }

            Result.success(sortedResults)

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
