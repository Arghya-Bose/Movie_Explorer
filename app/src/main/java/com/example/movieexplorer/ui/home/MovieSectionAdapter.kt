package com.example.movieexplorer.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieexplorer.databinding.ItemMovieSectionBinding
import com.example.movieexplorer.model.MovieSection

class MovieSectionAdapter(
    private val sections: List<MovieSection>
) : RecyclerView.Adapter<MovieSectionAdapter.SectionViewHolder>() {

    inner class SectionViewHolder(
        private val binding: ItemMovieSectionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(section: MovieSection) {

            binding.tvSectionTitle.text = section.title

            binding.rvMovies.apply {

                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )

                adapter = MovieAdapter(section.movies)

                setHasFixedSize(true)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SectionViewHolder {

        val binding = ItemMovieSectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return SectionViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SectionViewHolder,
        position: Int
    ) {
        holder.bind(sections[position])
    }

    override fun getItemCount(): Int {
        return sections.size
    }
}