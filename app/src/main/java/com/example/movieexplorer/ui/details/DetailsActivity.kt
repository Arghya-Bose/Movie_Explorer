package com.example.movieexplorer.ui.details

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil3.load
import android.view.View
import com.example.movieexplorer.data.local.AppDatabase
import com.example.movieexplorer.data.local.FavouriteMovieDao
import com.example.movieexplorer.data.local.entity.FavouriteMovie
import com.example.movieexplorer.data.remote.dto.MovieDetailsDto
import com.example.movieexplorer.databinding.ActivityDetailsBinding
import kotlinx.coroutines.launch

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var favouriteDao: FavouriteMovieDao

    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.detailsShimmer.visibility = View.VISIBLE
        binding.detailsContent.visibility = View.GONE

        // Get movie ID from Intent
        val movieId = intent.getIntExtra("movieId", -1)

        // Initialize Room database
        favouriteDao = AppDatabase
            .getDatabase(applicationContext)
            .favouriteMovieDao()

        // Back button
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Observe movie details
        observeMovie()

        // Load movie details
        viewModel.loadMovieDetails(movieId)
    }

    private fun observeMovie() {

        lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Movie details
                launch {

                    viewModel.movie.collect { movie ->

                        movie?.let {

                            // Movie loaded successfully
                            binding.detailsShimmer.visibility = View.GONE
                            binding.detailsContent.visibility = View.VISIBLE

                            displayMovie(it)

                            setupFavouriteButton(it)
                        }
                    }
                }

                // Loading state
                launch {

                    viewModel.isLoading.collect { isLoading ->

                        if (isLoading) {
                            binding.detailsShimmer.visibility = View.VISIBLE
                            binding.detailsContent.visibility = View.GONE
                        }
                    }
                }

                // Error state
                launch {

                    viewModel.error.collect { error ->

                        if (error != null) {

                            Toast.makeText(
                                this@DetailsActivity,
                                error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun displayMovie(movie: MovieDetailsDto) {

        // Movie title
        binding.tvMovieTitle.text = movie.title

        // Release date
        binding.tvReleaseDate.text =
            movie.releaseDate ?: "N/A"

        // Runtime
        binding.tvRuntime.text =
            movie.runtime?.let {
                "$it min"
            } ?: "N/A"

        // Rating
        binding.tvRating.text =
            "★ %.1f".format(movie.voteAverage)

        // Overview
        binding.tvOverview.text =
            if (movie.overview.isNullOrBlank()) {
                "No overview available."
            } else {
                movie.overview
            }

        // ---------------------------------------------------------
        // Backdrop
        // ---------------------------------------------------------

        val backdropUrl = movie.backdropPath?.let {
            "https://image.tmdb.org/t/p/w1280$it"
        }

        binding.ivBackdrop.load(backdropUrl)

        // ---------------------------------------------------------
        // Genres
        // ---------------------------------------------------------

        binding.genreContainer.removeAllViews()

        movie.genres?.forEach { genre ->

            val textView = TextView(this)

            textView.text = genre.name

            textView.setPadding(
                24,
                10,
                24,
                10
            )

            textView.setBackgroundResource(
                android.R.drawable.btn_default
            )

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            params.setMargins(
                0,
                0,
                12,
                0
            )

            textView.layoutParams = params

            binding.genreContainer.addView(textView)
        }
    }

    // -------------------------------------------------------------
    // Favourite Toggle
    // -------------------------------------------------------------

    private fun setupFavouriteButton(movie: MovieDetailsDto) {

        lifecycleScope.launch {

            // Check whether movie already exists in Room
            val alreadyFavourite =
                favouriteDao.isFavourite(movie.id)

            updateFavouriteButton(alreadyFavourite)

            binding.btnAddFavourite.setOnClickListener {

                lifecycleScope.launch {

                    val isFavourite =
                        favouriteDao.isFavourite(movie.id)

                    if (isFavourite) {

                        // -------------------------------------------------
                        // REMOVE FROM FAVOURITES
                        // -------------------------------------------------

                        favouriteDao.deleteById(movie.id)

                        updateFavouriteButton(false)

                        Toast.makeText(
                            this@DetailsActivity,
                            "Removed from Favourites",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        // -------------------------------------------------
                        // ADD TO FAVOURITES
                        // -------------------------------------------------

                        val favouriteMovie =
                            FavouriteMovie(
                                id = movie.id,
                                title = movie.title,
                                posterPath = movie.posterPath,
                                releaseDate = movie.releaseDate,
                                rating = movie.voteAverage
                            )

                        favouriteDao.insert(favouriteMovie)

                        updateFavouriteButton(true)

                        Toast.makeText(
                            this@DetailsActivity,
                            "Added to Favourites",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    // -------------------------------------------------------------
    // Update Favourite Button
    // -------------------------------------------------------------

    private fun updateFavouriteButton(isFavourite: Boolean) {

        if (isFavourite) {

            binding.btnAddFavourite.text =
                "Remove from Favourites"

        } else {

            binding.btnAddFavourite.text =
                "Add to Favourites"
        }
    }
}