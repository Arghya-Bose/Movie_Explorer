package com.example.movieexplorer.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieexplorer.data.remote.dto.MovieDto
import com.example.movieexplorer.model.Movie
import com.example.movieexplorer.model.MovieSection
import com.example.movieexplorer.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = MovieRepository()

    private val _sections =
        MutableStateFlow<List<MovieSection>>(emptyList())

    val sections: StateFlow<List<MovieSection>> =
        _sections.asStateFlow()

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()

    private val _error =
        MutableStateFlow<String?>(null)

    val error: StateFlow<String?> =
        _error.asStateFlow()

    fun loadMovies() {

        if (_sections.value.isNotEmpty()) {
            return
        }

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try {

                val popular = repository
                    .getPopularMovies()
                    .map { it.toMovie() }

                val nowPlaying = repository
                    .getNowPlayingMovies()
                    .map { it.toMovie() }

                val upcoming = repository
                    .getUpcomingMovies()
                    .map { it.toMovie() }

                val topRated = repository
                    .getTopRatedMovies()
                    .map { it.toMovie() }

                _sections.value = listOf(

                    MovieSection(
                        title = "Popular Movies",
                        movies = popular
                    ),

                    MovieSection(
                        title = "Now Playing",
                        movies = nowPlaying
                    ),

                    MovieSection(
                        title = "Upcoming Movies",
                        movies = upcoming
                    ),

                    MovieSection(
                        title = "Top Rated",
                        movies = topRated
                    )
                )

            } catch (e: Exception) {

                _error.value =
                    e.message ?: "Unable to load movies"

            } finally {

                _isLoading.value = false
            }
        }
    }

    private fun MovieDto.toMovie(): Movie {

        return Movie(
            id = id,
            title = title,
            rating = voteAverage,
            posterPath = posterPath,
            releaseDate = releaseDate
        )
    }
}