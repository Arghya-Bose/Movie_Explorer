package com.example.movieexplorer.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieexplorer.data.local.entity.FavouriteMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: FavouriteMovie)

    @Delete
    suspend fun delete(movie: FavouriteMovie)

    @Query("DELETE FROM favourite_movies WHERE id = :movieId")
    suspend fun deleteById(movieId: Int)

    @Query("SELECT * FROM favourite_movies ORDER BY id DESC")
    fun getAllFavourites(): Flow<List<FavouriteMovie>>

    @Query(
        "SELECT EXISTS(" +
                "SELECT 1 FROM favourite_movies " +
                "WHERE id = :movieId" +
                ")"
    )
    suspend fun isFavourite(movieId: Int): Boolean
}