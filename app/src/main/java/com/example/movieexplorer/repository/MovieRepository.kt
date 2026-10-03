package com.example.movieexplorer.repository

import com.example.movieexplorer.data.remote.RetrofitClient
import com.example.movieexplorer.data.remote.dto.MovieDto

class MovieRepository {

    private val api = RetrofitClient.api

    suspend fun getPopularMovies(): List<MovieDto> {
        return api.getPopularMovies().results
    }

    suspend fun getNowPlayingMovies(): List<MovieDto> {
        return api.getNowPlayingMovies().results
    }

    suspend fun getUpcomingMovies(): List<MovieDto> {
        return api.getUpcomingMovies().results
    }

    suspend fun getTopRatedMovies(): List<MovieDto> {
        return api.getTopRatedMovies().results
    }
}