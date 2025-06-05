package com.suraj.gallery.presentation.ui.albums

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.gallery.R
import com.gallery.databinding.ItemAlbumGridBinding
import com.gallery.databinding.ItemAlbumLinearBinding
import com.suraj.gallery.data.model.Album
import com.suraj.gallery.utils.ImageLoader

/**
 * Created by Suraj Bahadur on 6/5/2025.
 */
class AlbumsAdapter(
    private val onAlbumClick: (Album) -> Unit
) : RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder>() {

    private var albums = listOf<Album>()
    private var viewMode = AlbumsViewModel.ViewMode.GRID

    fun submitList(newAlbums: List<Album>) {
        albums = newAlbums
        notifyDataSetChanged()
    }

    fun setViewMode(mode: AlbumsViewModel.ViewMode) {
        viewMode = mode
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (viewMode) {
            AlbumsViewModel.ViewMode.GRID -> R.layout.item_album_grid
            AlbumsViewModel.ViewMode.LINEAR -> R.layout.item_album_linear
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = when (viewType) {
            R.layout.item_album_grid -> ItemAlbumGridBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            else -> ItemAlbumLinearBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        }
        return AlbumViewHolder(binding as ViewBinding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albums[position])
    }

    override fun getItemCount(): Int = albums.size

    inner class AlbumViewHolder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {
            when (binding) {
                is ItemAlbumGridBinding -> bindGrid(binding, album)
                is ItemAlbumLinearBinding -> bindLinear(binding, album)
            }

            binding.root.setOnClickListener { onAlbumClick(album) }
        }

        private fun bindGrid(binding: ItemAlbumGridBinding, album: Album) {
            binding.tvAlbumName.text = album.name
            binding.tvItemCount.text = "${album.itemCount} items"

            album.coverImageUri?.let { uri ->
                ImageLoader.loadImage(binding.ivCover, uri)
            }
        }

        private fun bindLinear(binding: ItemAlbumLinearBinding, album: Album) {
            binding.tvAlbumName.text = album.name
            binding.tvItemCount.text = "${album.itemCount} items"

            album.coverImageUri?.let { uri ->
                ImageLoader.loadThumbnail(binding.ivCover, uri)
            }
        }
    }
}