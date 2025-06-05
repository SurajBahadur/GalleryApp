package com.suraj.gallery.presentation.ui.common

/**
 * Created by Suraj Bahadur on 6/5/2025.
 */
sealed class ViewState<out T> {
    object Loading : ViewState<Nothing>()
    data class Success<T>(val data: T) : ViewState<T>()
    data class Error(val exception: Throwable) : ViewState<Nothing>()
}