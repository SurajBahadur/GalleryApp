package com.suraj.gallery.domain.usecase

import com.suraj.gallery.data.model.MediaItem
import com.suraj.gallery.domain.model.AlbumType
import com.suraj.gallery.domain.repository.MediaRepository
import javax.inject.Inject

/**
 * Created by Suraj Bahadur on 6/5/2025.
 */
class GetMediaItemsUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(albumId: String, albumType: AlbumType): Result<List<MediaItem>> =
        runCatching {
            repository.getMediaItems(albumId, albumType)
        }
}