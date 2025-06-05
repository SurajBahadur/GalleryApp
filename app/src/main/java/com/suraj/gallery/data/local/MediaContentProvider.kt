package com.suraj.gallery.data.local

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import com.suraj.gallery.data.model.MediaItem
import com.suraj.gallery.domain.model.MediaType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Suraj Bahadur on 6/5/2025.
 */
class MediaContentProvider @Inject constructor(
    private val contentResolver: ContentResolver
) {

    private val imageProjection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATE_ADDED,
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media.MIME_TYPE,
        MediaStore.Images.Media.BUCKET_ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    )

    private val videoProjection = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.DATA,
        MediaStore.Video.Media.DISPLAY_NAME,
        MediaStore.Video.Media.DATE_ADDED,
        MediaStore.Video.Media.SIZE,
        MediaStore.Video.Media.MIME_TYPE,
        MediaStore.Video.Media.BUCKET_ID,
        MediaStore.Video.Media.BUCKET_DISPLAY_NAME
    )

    suspend fun getAllImages(): List<MediaItem> = withContext(Dispatchers.IO) {
        val images = mutableListOf<MediaItem>()

        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            imageProjection,
            "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} NOT LIKE '%cache%' AND " + "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} NOT LIKE '%thumbnail%' AND " + "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} NOT LIKE '%.nomedia%'",
            null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val mediaItem = createMediaItemFromCursor(cursor, MediaType.IMAGE)
                images.add(mediaItem)
            }
        }

        images
    }

    suspend fun getAllVideos(): List<MediaItem> = withContext(Dispatchers.IO) {
        val videos = mutableListOf<MediaItem>()

        contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            videoProjection,
            null,
            null,
            "${MediaStore.Video.Media.DATE_ADDED} DESC"
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val mediaItem = createMediaItemFromCursor(cursor, MediaType.VIDEO)
                videos.add(mediaItem)
            }
        }

        videos
    }

    suspend fun getCameraImages(): List<MediaItem> = withContext(Dispatchers.IO) {
        val cameraImages = mutableListOf<MediaItem>()

        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            imageProjection,
            "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} LIKE '%camera%' OR " + "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} LIKE '%Camera%' OR " + "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} LIKE '%DCIM%'",
            null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val mediaItem = createMediaItemFromCursor(cursor, MediaType.IMAGE)
                cameraImages.add(mediaItem)
            }
        }

        cameraImages
    }

    private fun createMediaItemFromCursor(cursor: Cursor, type: MediaType): MediaItem {
        val id = cursor.getLong(
            cursor.getColumnIndexOrThrow(
                when (type) {
                    MediaType.IMAGE -> MediaStore.Images.Media._ID
                    MediaType.VIDEO -> MediaStore.Video.Media._ID
                }
            )
        )

        val data = cursor.getString(
            cursor.getColumnIndexOrThrow(
                when (type) {
                    MediaType.IMAGE -> MediaStore.Images.Media.DATA
                    MediaType.VIDEO -> MediaStore.Video.Media.DATA
                }
            )
        )

        val displayName = cursor.getString(
            cursor.getColumnIndexOrThrow(
                when (type) {
                    MediaType.IMAGE -> MediaStore.Images.Media.DISPLAY_NAME
                    MediaType.VIDEO -> MediaStore.Video.Media.DISPLAY_NAME
                }
            )
        )

        val dateAdded = cursor.getLong(
            cursor.getColumnIndexOrThrow(
                when (type) {
                    MediaType.IMAGE -> MediaStore.Images.Media.DATE_ADDED
                    MediaType.VIDEO -> MediaStore.Video.Media.DATE_ADDED
                }
            )
        )

        val size = cursor.getLong(
            cursor.getColumnIndexOrThrow(
                when (type) {
                    MediaType.IMAGE -> MediaStore.Images.Media.SIZE
                    MediaType.VIDEO -> MediaStore.Video.Media.SIZE
                }
            )
        )

        val mimeType = cursor.getString(
            cursor.getColumnIndexOrThrow(
                when (type) {
                    MediaType.IMAGE -> MediaStore.Images.Media.MIME_TYPE
                    MediaType.VIDEO -> MediaStore.Video.Media.MIME_TYPE
                }
            )
        )

        val bucketId = cursor.getString(
            cursor.getColumnIndexOrThrow(
                when (type) {
                    MediaType.IMAGE -> MediaStore.Images.Media.BUCKET_ID
                    MediaType.VIDEO -> MediaStore.Video.Media.BUCKET_ID
                }
            )
        )

        val bucketDisplayName = cursor.getString(
            cursor.getColumnIndexOrThrow(
                when (type) {
                    MediaType.IMAGE -> MediaStore.Images.Media.BUCKET_DISPLAY_NAME
                    MediaType.VIDEO -> MediaStore.Video.Media.BUCKET_DISPLAY_NAME
                }
            )
        )

        return MediaItem(
            id = id,
            uri = data,
            displayName = displayName,
            dateAdded = dateAdded,
            size = size,
            mimeType = mimeType,
            bucketId = bucketId,
            bucketDisplayName = bucketDisplayName,
            type = type
        )
    }
}