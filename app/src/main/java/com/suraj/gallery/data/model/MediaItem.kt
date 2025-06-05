package com.suraj.gallery.data.model

import com.suraj.gallery.domain.model.MediaType

/**
 * Created by Suraj Bahadur on 6/5/2025.
 */
data class MediaItem(
    val id: Long,
    val uri: String,
    val displayName: String,
    val dateAdded: Long,
    val size: Long,
    val mimeType: String,
    val bucketId: String,
    val bucketDisplayName: String,
    val type: MediaType
) {
    val isImage: Boolean get() = type == MediaType.IMAGE
    val isVideo: Boolean get() = type == MediaType.VIDEO
}
