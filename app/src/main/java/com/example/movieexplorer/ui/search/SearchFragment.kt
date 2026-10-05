package com.example.movieexplorer.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.content.Context

import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.movieexplorer.databinding.FragmentSearchBinding
import com.example.movieexplorer.ui.details.DetailsActivity

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    private lateinit var searchAdapter: SearchAdapter

    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(
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
        setupSearch()
        setupClearButton()
        observeViewModel()
    }

    private fun setupRecyclerView() {

        searchAdapter = SearchAdapter(
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

        binding.rvSearchResults.apply {

            layoutManager = LinearLayoutManager(
                requireContext()
            )

            adapter = searchAdapter
        }
    }

    private fun setupSearch() {

        binding.etSearch.doAfterTextChanged { text ->

            val query = text
                ?.toString()
                ?.trim()
                ?: ""

            binding.ivClearSearch.visibility =
                if (query.isNotEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

            searchJob?.cancel()

            if (query.isEmpty()) {

                searchAdapter.updateMovies(emptyList())

                binding.rvSearchResults.visibility =
                    View.GONE

                binding.tvEmpty.visibility =
                    View.VISIBLE

                binding.tvEmpty.text =
                    "Search for a movie"

                binding.tvError.visibility =
                    View.GONE

                return@doAfterTextChanged
            }

            searchJob = viewLifecycleOwner.lifecycleScope.launch {

                delay(500)

                viewModel.searchMovies(query)
            }
        }

        binding.etSearch.setOnEditorActionListener { _, actionId, event ->

            if (
                actionId == EditorInfo.IME_ACTION_SEARCH ||
                event?.keyCode == KeyEvent.KEYCODE_ENTER
            ) {

                val query = binding.etSearch
                    .text
                    .toString()
                    .trim()

                if (query.isNotEmpty()) {

                    searchJob?.cancel()

                    viewModel.searchMovies(query)

                    hideKeyboard()
                }

                true

            } else {
                false
            }
        }
    }

    private fun setupClearButton() {

        binding.ivClearSearch.setOnClickListener {

            binding.etSearch.text?.clear()

            binding.etSearch.requestFocus()

            showKeyboard()
        }
    }

    private fun observeViewModel() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                launch {

                    viewModel.movies.collect { movies ->

                        searchAdapter.updateMovies(
                            movies
                        )

                        if (movies.isNotEmpty()) {

                            binding.rvSearchResults.visibility =
                                View.VISIBLE

                            binding.tvEmpty.visibility =
                                View.GONE

                            binding.tvError.visibility =
                                View.GONE

                        } else if (
                            !binding.etSearch
                                .text
                                .toString()
                                .trim()
                                .isEmpty()
                        ) {

                            binding.rvSearchResults.visibility =
                                View.GONE

                            binding.tvEmpty.visibility =
                                View.VISIBLE

                            binding.tvEmpty.text =
                                "No movies found"

                        }
                    }
                }

                launch {

                    viewModel.isLoading.collect { loading ->

                        binding.progressBar.visibility =
                            if (loading) {
                                View.VISIBLE
                            } else {
                                View.GONE
                            }
                    }
                }

                launch {

                    viewModel.error.collect { error ->

                        if (error != null) {

                            binding.tvError.visibility =
                                View.VISIBLE

                            binding.tvError.text =
                                "Failed to load movies"

                            binding.rvSearchResults.visibility =
                                View.GONE

                            binding.tvEmpty.visibility =
                                View.GONE

                        } else {

                            binding.tvError.visibility =
                                View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun hideKeyboard() {

        val inputMethodManager =
            requireContext().getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager

        inputMethodManager.hideSoftInputFromWindow(
            binding.etSearch.windowToken,
            0
        )
    }

    private fun showKeyboard() {

        val inputMethodManager =
            requireContext().getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager

        inputMethodManager.showSoftInput(
            binding.etSearch,
            InputMethodManager.SHOW_IMPLICIT
        )
    }

    override fun onDestroyView() {

        searchJob?.cancel()

        super.onDestroyView()

        _binding = null
    }
}