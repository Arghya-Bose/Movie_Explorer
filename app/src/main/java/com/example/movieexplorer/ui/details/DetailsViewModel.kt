package com.example.movieexplorer.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieexplorer.data.remote.dto.MovieDetailsDto
import com.example.movieexplorer.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {

    private val repository = MovieRepository()

    private val _movie = MutableStateFlow<MovieDetailsDto?>(null)
    val movie: StateFlow<MovieDetailsDto?> = _movie

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadMovieDetails(movieId: Int) {

        if (movieId == -1) {
            _error.value = "Invalid movie ID"
            return
        }

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try {

                val result = repository.getMovieDetails(movieId)

                _movie.value = result

            } catch (e: Exception) {

                _error.value = e.message ?: "Failed to load movie details"

            } finally {

                _isLoading.value = false
            }
        }
    }
}