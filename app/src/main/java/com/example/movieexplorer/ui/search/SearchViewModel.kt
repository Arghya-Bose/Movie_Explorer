package com.example.movieexplorer.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieexplorer.model.Movie
import com.example.movieexplorer.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val repository = MovieRepository()

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun searchMovies(query: String) {

        if (query.isBlank()) {
            _movies.value = emptyList()
            _error.value = null
            return
        }

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try {

                val results = repository.searchMovies(query)

                _movies.value = results.map { movie ->

                    Movie(
                        id = movie.id,
                        title = movie.title,
                        rating = movie.voteAverage,
                        posterPath = movie.posterPath,
                        releaseDate = movie.releaseDate
                    )
                }

            } catch (e: Exception) {

                _movies.value = emptyList()

                _error.value =
                    e.message ?: "Something went wrong"

            } finally {

                _isLoading.value = false
            }
        }
    }
}