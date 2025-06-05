package com.suraj.gallery.presentation.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.gallery.R
import com.gallery.databinding.ItemMediaGridBinding
import com.gallery.databinding.ItemMediaLinearBinding

import com.suraj.gallery.data.model.MediaItem
import com.suraj.gallery.utils.ImageLoader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by Suraj Bahadur on 6/5/2025.
 */
class MediaAdapter(
    private val onMediaClick: (MediaItem) -> Unit
) : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    private var mediaItems = listOf<MediaItem>()
    private var viewMode = AlbumDetailViewModel.ViewMode.GRID

    fun submitList(newItems: List<MediaItem>) {
        mediaItems = newItems
        notifyDataSetChanged()
    }

    fun setViewMode(mode: AlbumDetailViewModel.ViewMode) {
        viewMode = mode
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (viewMode) {
            AlbumDetailViewModel.ViewMode.GRID -> R.layout.item_media_grid
            AlbumDetailViewModel.ViewMode.LINEAR -> R.layout.item_media_linear
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding = when (viewType) {
            R.layout.item_media_grid -> ItemMediaGridBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            else -> ItemMediaLinearBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        }
        return MediaViewHolder(binding as ViewBinding)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.bind(mediaItems[position])
    }

    override fun getItemCount(): Int = mediaItems.size

    inner class MediaViewHolder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(mediaItem: MediaItem) {
            when (binding) {
                is ItemMediaGridBinding -> bindGrid(binding, mediaItem)
                is ItemMediaLinearBinding -> bindLinear(binding, mediaItem)
            }

            binding.root.setOnClickListener { onMediaClick(mediaItem) }
        }

        private fun bindGrid(binding: ItemMediaGridBinding, mediaItem: MediaItem) {
            ImageLoader.loadThumbnail(binding.ivMedia, mediaItem.uri)

            if (mediaItem.isVideo) {
                binding.ivPlayIcon.visibility = View.VISIBLE
                binding.tvDuration.visibility = View.VISIBLE
                binding.tvDuration.text = formatDuration(mediaItem.size) // This should be duration, not size
            } else {
                binding.ivPlayIcon.visibility = View.GONE
                binding.tvDuration.visibility = View.GONE
            }
        }

        private fun bindLinear(binding: ItemMediaLinearBinding, mediaItem: MediaItem) {
            ImageLoader.loadThumbnail(binding.ivMedia, mediaItem.uri)
            /*binding.tvFileName.text = mediaItem.displayName
            binding.tvFileSize.text = formatFileSize(mediaItem.size)
            binding.tvDateAdded.text = formatDate(mediaItem.dateAdded)*/

            if (mediaItem.isVideo) {
                binding.ivPlayIcon.visibility = View.VISIBLE
            } else {
                binding.ivPlayIcon.visibility = View.GONE
            }
        }

        private fun formatDuration(duration: Long): String {
            val minutes = duration / 60000
            val seconds = (duration % 60000) / 1000
            return String.format("%d:%02d", minutes, seconds)
        }

        private fun formatFileSize(size: Long): String {
            val kb = size / 1024
            return if (kb < 1024) {
                "${kb} KB"
            } else {
                val mb = kb / 1024
                "${mb} MB"
            }
        }

        private fun formatDate(timestamp: Long): String {
            val date = Date(timestamp * 1000)
            val format = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            return format.format(date)
        }
    }
}