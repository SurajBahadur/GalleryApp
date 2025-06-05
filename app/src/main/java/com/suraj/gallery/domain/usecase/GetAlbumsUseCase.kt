package com.suraj.gallery.domain.usecase

import com.suraj.gallery.data.model.Album
import com.suraj.gallery.domain.repository.MediaRepository
import javax.inject.Inject

/**
 * Created by Suraj Bahadur on 6/5/2025.
 */
class GetAlbumsUseCase @Inject constructor(
    private val repository: MediaRepository
) {

    suspend operator fun invoke(): Result<List<Album>> = runCatching {
        repository.getAllAlbums()
    }
}