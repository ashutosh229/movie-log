package com.example.movielog.features.search.data.repository

import com.example.movielog.core.utils.calculateRelevance
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

//                TODO: Apply deduplication further
                val combined = (movies + series + anime)
                    .map { content ->
                        content to calculateRelevance(query, content.title)
                    }
                    .filter { (_, score) ->
//                        TODO: Store this hyperparameter in a config file
                        score > 200
                    }
                    .sortedWith(
                        compareByDescending<Pair<Content, Int>> { it.second }
                            .thenByDescending { it.first.rating ?: 0.0 }
                    )
                    .map { it.first }
                Result.success(combined)
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}