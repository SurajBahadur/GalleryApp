package com.suraj.gallery.presentation.ui.albums

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.gallery.R
import com.gallery.databinding.ActivityAlbumsBinding
import com.suraj.gallery.presentation.ui.common.BaseActivity
import com.suraj.gallery.presentation.ui.common.ViewState
import com.suraj.gallery.presentation.ui.detail.AlbumDetailActivity
import com.suraj.gallery.utils.PermissionHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Created by Suraj Bahadur on 6/5/2025.
 */
@AndroidEntryPoint
class AlbumsActivity : BaseActivity() {
    private lateinit var binding: ActivityAlbumsBinding
    private lateinit var adapter: AlbumsAdapter
    private val viewModel: AlbumsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()

        checkPermissions()
    }


    private fun setupRecyclerView() {
        adapter = AlbumsAdapter { album ->
            startActivity(
                Intent(this, AlbumDetailActivity::class.java).apply {
                    putExtra("album_id", album.id)
                    putExtra("album_name", album.name)
                    putExtra("album_type", album.type.name)
                })
        }

        binding.recyclerView.adapter = adapter
        updateLayoutManager(AlbumsViewModel.ViewMode.GRID)
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
            viewModel.loadAlbums()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun updateLayoutManager(mode: AlbumsViewModel.ViewMode) {
        binding.recyclerView.layoutManager = when (mode) {
            AlbumsViewModel.ViewMode.GRID -> GridLayoutManager(this, 2)
            AlbumsViewModel.ViewMode.LINEAR -> LinearLayoutManager(this)
        }
    }

    private fun updateViewModeIcon(mode: AlbumsViewModel.ViewMode) {
        val iconRes = when (mode) {
            AlbumsViewModel.ViewMode.GRID -> R.drawable.ic_view_list
            AlbumsViewModel.ViewMode.LINEAR -> R.drawable.ic_view_grid
        }
        binding.fabViewMode.setImageResource(iconRes)
    }

    private fun checkPermissions() {
        if (!PermissionHelper.hasStoragePermissions(this)) {
            PermissionHelper.requestStoragePermissions(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionHelper.STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.loadAlbums()
            } else {
                showError("Storage permission is required to access media files")
                finish()
            }
        }
    }
}