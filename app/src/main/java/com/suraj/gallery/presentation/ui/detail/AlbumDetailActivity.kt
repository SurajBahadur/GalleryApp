package com.suraj.gallery.presentation.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.gallery.R
import com.gallery.databinding.ActivityAlbumDetailBinding
import com.suraj.gallery.data.local.MediaContentProvider
import com.suraj.gallery.data.model.MediaItem
import com.suraj.gallery.data.repository.MediaRepositoryImpl
import com.suraj.gallery.domain.model.AlbumType
import com.suraj.gallery.domain.usecase.GetMediaItemsUseCase
import com.suraj.gallery.presentation.ui.albums.AlbumsViewModel
import com.suraj.gallery.presentation.ui.common.BaseActivity
import com.suraj.gallery.presentation.ui.common.ViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

/**
 * Created by Suraj Bahadur on 6/5/2025.
 */
@AndroidEntryPoint
class AlbumDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityAlbumDetailBinding
    private lateinit var adapter: MediaAdapter
    private val viewModel: AlbumDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupToolbar() {
        val albumName = intent.getStringExtra("album_name") ?: "Album"
        binding.toolbar.title = albumName
        binding.toolbar.setNavigationOnClickListener { finish() }
    }



    private fun setupRecyclerView() {
        adapter = MediaAdapter { mediaItem ->
            // Handle media item click - could open full screen viewer
            openMediaViewer(mediaItem)
        }

        binding.recyclerView.adapter = adapter
        updateLayoutManager(AlbumDetailViewModel.ViewMode.GRID)
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is ViewState.Loading -> showLoading(true)
                    is ViewState.Success -> {
                        showLoading(false)
                        adapter.submitList(state.data)
                    }

                    is ViewState.Error -> {
                        showLoading(false)
                        showError(state.exception.message ?: "Unknown error")
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.viewMode.collect { mode ->
                adapter.setViewMode(mode)
                updateLayoutManager(mode)
                updateViewModeIcon(mode)
            }
        }
    }

    private fun setupClickListeners() {
        binding.fabViewMode.setOnClickListener {
            viewModel.toggleViewMode()
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadMediaItems()
            binding.swipeRefresh.isRefreshing = false
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun updateLayoutManager(mode: AlbumDetailViewModel.ViewMode) {
        binding.recyclerView.layoutManager = when (mode) {
            AlbumDetailViewModel.ViewMode.GRID -> GridLayoutManager(this, 3)
            AlbumDetailViewModel.ViewMode.LINEAR -> LinearLayoutManager(this)
        }
    }

    private fun updateViewModeIcon(mode: AlbumDetailViewModel.ViewMode) {
        val iconRes = when (mode) {
            AlbumDetailViewModel.ViewMode.GRID -> R.drawable.ic_view_list
            AlbumDetailViewModel.ViewMode.LINEAR -> R.drawable.ic_view_grid
        }
        binding.fabViewMode.setImageResource(iconRes)
    }

    private fun openMediaViewer(mediaItem: MediaItem) {
        // Implement full screen media viewer
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(Uri.parse(mediaItem.uri), mediaItem.mimeType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        try {
            startActivity(intent)
        } catch (e: Exception) {
            showError("No app found to open this media file")
        }
    }
}