package com.example.movielog.core.network


import com.example.movielog.core.config.ApiConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {

    private const val TMDB_BASE_URL = ApiConfig.TMDB_BASE_URL
    private const val JIKAN_BASE_URL = ApiConfig.JIKAN_BASE_URL

    // 🔹 Logger (VERY useful for debugging)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val request = chain.request()
            android.util.Log.d("TMDB_REQUEST", request.url.toString())
            val response = chain.proceed(request)
            android.util.Log.d("TMDB_RESPONSE", response.code.toString())
            val responseBody = response.peekBody(Long.MAX_VALUE)
            android.util.Log.d("TMDB_BODY", responseBody.string())
            response
        }
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