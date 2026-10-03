package com.example.movieexplorer.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import com.example.movieexplorer.databinding.ItemMovieBinding
import com.example.movieexplorer.model.Movie

class MovieAdapter(
    private val movies: List<Movie>
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(
        private val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {

            binding.tvMovieTitle.text = movie.title

            binding.tvRating.text =
                "★ %.1f".format(movie.rating)

            val posterUrl = movie.posterPath?.let {
                "https://image.tmdb.org/t/p/w500$it"
            }

            binding.progressBar.visibility = View.VISIBLE

            binding.ivMoviePoster.load(posterUrl) {

                crossfade(true)

                listener(
                    onSuccess = { _, _ ->
                        binding.progressBar.visibility = View.GONE
                    },
                    onError = { _, _ ->
                        binding.progressBar.visibility = View.GONE
                    }
                )
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {

        val binding = ItemMovieBinding.inflate(
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