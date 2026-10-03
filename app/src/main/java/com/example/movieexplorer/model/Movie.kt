package com.example.movieexplorer.model

data class Movie(
    val id: Int,
    val title: String,
    val rating: Double,
    val posterPath: String?,
    val releaseDate: String?
)