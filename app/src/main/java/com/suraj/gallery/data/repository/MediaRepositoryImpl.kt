package com.suraj.gallery.data.repository

import com.suraj.gallery.data.local.MediaContentProvider
import com.suraj.gallery.data.model.Album
import com.suraj.gallery.data.model.MediaItem
import com.suraj.gallery.domain.model.AlbumType
import com.suraj.gallery.domain.repository.MediaRepository
import javax.inject.Inject

/**
 * Created by Suraj Bahadur on 6/5/2025.
 */
class MediaRepositoryImpl @Inject constructor(
    private val mediaContentProvider: MediaContentProvider
) : MediaRepository {

    override suspend fun getAllAlbums(): List<Album> {
        val albums = mutableListOf<Album>()

        // Get all images and videos
        val allImages = mediaContentProvider.getAllImages()
        val allVideos = mediaContentProvider.getAllVideos()
        val cameraImages = mediaContentProvider.getCameraImages()

        // Create special albums
        if (allImages.isNotEmpty()) {
            albums.add(
                Album(
                    id = "all_images",
                    name = "All Images",
                    type = AlbumType.ALL_IMAGES,
                    coverImageUri = allImages.first().uri,
                    itemCount = allImages.size,
                    lastModified = allImages.maxOf { it.dateAdded }
                )
            )
        }

        if (allVideos.isNotEmpty()) {
            albums.add(
                Album(
                    id = "all_videos",
                    name = "All Videos",
                    type = AlbumType.ALL_VIDEOS,
                    coverImageUri = allVideos.first().uri,
                    itemCount = allVideos.size,
                    lastModified = allVideos.maxOf { it.dateAdded }
                )
            )
        }

        if (cameraImages.isNotEmpty()) {
            albums.add(
                Album(
                    id = "camera",
                    name = "Camera",
                    type = AlbumType.CAMERA,
                    coverImageUri = cameraImages.first().uri,
                    itemCount = cameraImages.size,
                    lastModified = cameraImages.maxOf { it.dateAdded }
                )
            )
        }

        // Group by folder
        val folderGroups = (allImages + allVideos)
            .filterNot { it.bucketDisplayName.isNullOrEmpty() }
            .groupBy { it.bucketDisplayName }

        folderGroups.forEach { (folderName, items) ->
            if (!isCameraFolder(folderName)) {
                albums.add(
                    Album(
                        id = items.first().bucketId,
                        name = folderName,
                        type = AlbumType.FOLDER,
                        coverImageUri = items.first().uri,
                        itemCount = items.size,
                        lastModified = items.maxOf { it.dateAdded }
                    )
                )
            }
        }

        return albums.sortedByDescending { it.lastModified }
    }

    override suspend fun getMediaItems(albumId: String, albumType: AlbumType): List<MediaItem> {
        return when (albumType) {
            AlbumType.ALL_IMAGES -> getAllImages()
            AlbumType.ALL_VIDEOS -> getAllVideos()
            AlbumType.CAMERA -> getCameraImages()
            AlbumType.FOLDER -> {
                val allMedia = getAllImages() + getAllVideos()
                allMedia.filter { it.bucketId == albumId }
            }
        }
    }

    override suspend fun getAllImages(): List<MediaItem> = mediaContentProvider.getAllImages()

    override suspend fun getAllVideos(): List<MediaItem> = mediaContentProvider.getAllVideos()

    override suspend fun getCameraImages(): List<MediaItem> = mediaContentProvider.getCameraImages()

    private fun isCameraFolder(folderName: String): Boolean {
        return folderName.contains("camera", ignoreCase = true) ||
                folderName.contains("dcim", ignoreCase = true)
    }
}