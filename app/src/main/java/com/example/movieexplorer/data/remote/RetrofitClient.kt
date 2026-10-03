package com.example.movieexplorer.data.remote

import com.example.movieexplorer.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL =
        "https://api.themoviedb.org/"

    private val authInterceptor = Interceptor { chain ->

        val request = chain.request()
            .newBuilder()
            .addHeader(
                "Authorization",
                "Bearer ${BuildConfig.TMDB_ACCESS_TOKEN}"
            )
            .addHeader(
                "accept",
                "application/json"
            )
            .build()

        chain.proceed(request)
    }

    private val okHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

    val api: TmdbApiService by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(TmdbApiService::class.java)
    }
}