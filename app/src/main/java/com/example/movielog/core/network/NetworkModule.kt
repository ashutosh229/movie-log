package com.example.movielog.core.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    private const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
    private const val JIKAN_BASE_URL = "https://api.jikan.moe/v4/"
//    TODO: Need to think of a config file for storing constants

    // 🔹 Logger (VERY useful for debugging)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // 🔹 TMDB Retrofit
    val tmdbRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(TMDB_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 🔹 JIKAN Retrofit
    val jikanRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(JIKAN_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}