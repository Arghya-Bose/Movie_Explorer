package com.example.movieexplorer.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieexplorer.databinding.ItemMovieShimmerBinding

class ShimmerMovieAdapter(
    private val itemCount: Int = 6
) : RecyclerView.Adapter<ShimmerMovieAdapter.ShimmerViewHolder>() {

    class ShimmerViewHolder(
        val binding: ItemMovieShimmerBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShimmerViewHolder {

        val binding = ItemMovieShimmerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ShimmerViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ShimmerViewHolder,
        position: Int
    ) {
        // Nothing to bind
    }

    override fun getItemCount(): Int = itemCount
}