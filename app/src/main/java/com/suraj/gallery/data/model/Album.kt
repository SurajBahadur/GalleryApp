package com.suraj.gallery.data.model

import com.suraj.gallery.domain.model.AlbumType

/**
 * Created by Suraj Bahadur on 6/5/2025.
 */
data class Album(
    val id: String,
    val name: String,
    val type: AlbumType,
    val coverImageUri: String?,
    val itemCount: Int,
    val lastModified: Long
)