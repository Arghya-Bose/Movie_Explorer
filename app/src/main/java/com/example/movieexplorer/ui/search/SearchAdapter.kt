package com.example.movieexplorer.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import com.example.movieexplorer.databinding.ItemSearchMovieBinding
import com.example.movieexplorer.model.Movie

class SearchAdapter(
    private var movies: List<Movie>,
    private val onMovieClick: (Movie) -> Unit
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(
        private val binding: ItemSearchMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {

            binding.tvSearchTitle.text = movie.title

            binding.tvSearchReleaseDate.text =
                movie.releaseDate?.take(4) ?: "N/A"

            binding.tvSearchRating.text =
                "★ %.1f".format(movie.rating)

            val posterUrl = movie.posterPath?.let {
                "https://image.tmdb.org/t/p/w500$it"
            }

            binding.progressBar.visibility = View.VISIBLE

            binding.ivSearchPoster.load(posterUrl) {

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
    ): SearchViewHolder {

        val binding = ItemSearchMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SearchViewHolder,
        position: Int
    ) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}