package com.example.movieexplorer.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieexplorer.databinding.FragmentHomeBinding
import com.example.movieexplorer.model.MovieSection
import com.example.movieexplorer.ui.details.DetailsActivity
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var sectionAdapter: MovieSectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(
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
        observeViewModel()

        viewModel.loadMovies()
    }

    private fun setupRecyclerView() {

        sectionAdapter = MovieSectionAdapter(
            listOf(
                MovieSection("Popular Movies", emptyList()),
                MovieSection("Now Playing", emptyList()),
                MovieSection("Upcoming", emptyList()),
                MovieSection("Top Rated", emptyList())
            )
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

        sectionAdapter.updateLoadingState(true)

        binding.rvHomeSections.apply {

            layoutManager = LinearLayoutManager(
                requireContext()
            )

            adapter = sectionAdapter

            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                launch {

                    viewModel.sections.collect { sections ->

                        sectionAdapter.updateSections(
                            sections
                        )
                    }
                }

                launch {

                    viewModel.isLoading.collect { isLoading ->

                        sectionAdapter.updateLoadingState(isLoading)
                    }
                }

                launch {

                    viewModel.error.collect { errorMessage ->

                        if (errorMessage != null) {

                            // Error UI / retry will be added later.
                        }
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