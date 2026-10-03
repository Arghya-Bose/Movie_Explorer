package com.example.movieexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MovieResponse(

    @SerializedName("page")
    val page: Int,

    @SerializedName("results")
    val results: List<MovieDto>
)