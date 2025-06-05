package com.suraj.gallery.domain.repository

import com.suraj.gallery.data.model.Album
import com.suraj.gallery.data.model.MediaItem
import com.suraj.gallery.domain.model.AlbumType


/**
 * Created by Suraj Bahadur on 6/5/2025.
 */
interface MediaRepository {
    suspend fun getAllAlbums(): List<Album>
    suspend fun getMediaItems(albumId: String, albumType: AlbumType): List<MediaItem>
    suspend fun getAllImages(): List<MediaItem>
    suspend fun getAllVideos(): List<MediaItem>
    suspend fun getCameraImages(): List<MediaItem>
}