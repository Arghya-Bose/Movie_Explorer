package com.example.movieexplorer.ui.favourites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieexplorer.data.local.AppDatabase
import com.example.movieexplorer.data.local.FavouriteMovieDao
import com.example.movieexplorer.databinding.FragmentFavouritesBinding
import com.example.movieexplorer.model.Movie
import com.example.movieexplorer.ui.details.DetailsActivity
import kotlinx.coroutines.launch

class FavouritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var favouriteDao: FavouriteMovieDao
    private lateinit var favouritesAdapter: FavouritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavouritesBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        favouriteDao = AppDatabase
            .getDatabase(requireContext())
            .favouriteMovieDao()

        observeFavourites()
    }

    private fun setupRecyclerView() {

        favouritesAdapter = FavouritesAdapter(
            emptyList()
        ) { movie ->

            val intent = Intent(
                requireContext(),
                DetailsActivity::class.java
            )

            intent.putExtra(
                "movieId",
                movie.id
            )

            startActivity(intent)
        }

        binding.rvFavourites.apply {

            layoutManager = LinearLayoutManager(
                requireContext()
            )

            adapter = favouritesAdapter
        }
    }

    private fun observeFavourites() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                favouriteDao
                    .getAllFavourites()
                    .collect { favourites ->

                        val movies = favourites.map { favourite ->

                            Movie(
                                id = favourite.id,
                                title = favourite.title,
                                rating = favourite.rating,
                                posterPath = favourite.posterPath,
                                releaseDate = favourite.releaseDate
                            )
                        }

                        favouritesAdapter.updateMovies(
                            movies
                        )

                        if (movies.isEmpty()) {

                            binding.tvEmptyFavourites.visibility =
                                View.VISIBLE

                            binding.rvFavourites.visibility =
                                View.GONE

                        } else {

                            binding.tvEmptyFavourites.visibility =
                                View.GONE

                            binding.rvFavourites.visibility =
                                View.VISIBLE
                        }
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}