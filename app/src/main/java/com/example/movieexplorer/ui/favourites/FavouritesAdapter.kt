package com.example.movieexplorer.ui.favourites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieexplorer.databinding.ItemFavouriteMovieBinding
import coil3.load
import coil3.request.crossfade
import com.example.movieexplorer.model.Movie

class FavouritesAdapter(
    private var movies: List<Movie>,
    private val onMovieClick: (Movie) -> Unit
) : RecyclerView.Adapter<FavouritesAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(
        private val binding: ItemFavouriteMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {

            binding.tvFavouriteTitle.text = movie.title

            binding.tvFavouriteRating.text =
                "★ %.1f".format(movie.rating)

            binding.tvFavouriteReleaseDate.text =
                movie.releaseDate ?: "Release date unavailable"

            val posterUrl = movie.posterPath?.let {
                "https://image.tmdb.org/t/p/w500$it"
            }

            binding.progressBar.visibility = View.VISIBLE

            binding.ivFavouritePoster.load(posterUrl) {

                crossfade(true)

                listener(

                    onSuccess = { _, _ ->
                        binding.progressBar.visibility =
                            View.GONE
                    },

                    onError = { _, _ ->
                        binding.progressBar.visibility =
                            View.GONE
                    }
                )
            }

            binding.root.setOnClickListener {
                onMovieClick(movie)
            }
        }
    }

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {

        val binding = ItemFavouriteMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int
    ) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}