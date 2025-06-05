package com.suraj.gallery.presentation.ui.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suraj.gallery.data.model.MediaItem
import com.suraj.gallery.domain.model.AlbumType
import com.suraj.gallery.domain.usecase.GetMediaItemsUseCase
import com.suraj.gallery.presentation.ui.common.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Suraj Bahadur on 6/5/2025.
 */
@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    private val getMediaItemsUseCase: GetMediaItemsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val albumId: String = savedStateHandle["album_id"] ?: ""
    private val albumType: AlbumType = AlbumType.valueOf(
        savedStateHandle["album_type"] ?: AlbumType.FOLDER.name
    )

    private val _uiState = MutableStateFlow<ViewState<List<MediaItem>>>(ViewState.Loading)
    val uiState: StateFlow<ViewState<List<MediaItem>>> = _uiState.asStateFlow()

    private val _viewMode = MutableStateFlow(ViewMode.GRID)
    val viewMode: StateFlow<ViewMode> = _viewMode.asStateFlow()

    init {
        loadMediaItems()
    }

    fun loadMediaItems() {
        viewModelScope.launch {
            _uiState.value = ViewState.Loading

            getMediaItemsUseCase(albumId, albumType)
                .onSuccess { items ->
                    _uiState.value = ViewState.Success(items)
                }
                .onFailure { exception ->
                    _uiState.value = ViewState.Error(exception)
                }
        }
    }

    fun toggleViewMode() {
        _viewMode.value = when (_viewMode.value) {
            ViewMode.GRID -> ViewMode.LINEAR
            ViewMode.LINEAR -> ViewMode.GRID
        }
    }

    enum class ViewMode {
        GRID, LINEAR
    }
}