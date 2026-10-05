package com.example.movieexplorer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_movies")
data class FavouriteMovie(

    @PrimaryKey
    val id: Int,

    val title: String,

    val posterPath: String?,

    val releaseDate: String?,

    val rating: Double
)