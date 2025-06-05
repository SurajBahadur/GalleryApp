package com.suraj.gallery.presentation.ui.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suraj.gallery.data.model.Album
import com.suraj.gallery.domain.usecase.GetAlbumsUseCase
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
class AlbumsViewModel @Inject constructor(
    private val getAlbumsUseCase: GetAlbumsUseCase
)  : ViewModel() {

    private val _uiState = MutableStateFlow<ViewState<List<Album>>>(ViewState.Loading)
    val uiState: StateFlow<ViewState<List<Album>>> = _uiState.asStateFlow()

    private val _viewMode = MutableStateFlow(ViewMode.GRID)
    val viewMode: StateFlow<ViewMode> = _viewMode.asStateFlow()

    init {
        loadAlbums()
    }

    fun loadAlbums() {
        viewModelScope.launch {
            _uiState.value = ViewState.Loading

            getAlbumsUseCase()
                .onSuccess { albums ->
                    _uiState.value = ViewState.Success(albums)
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