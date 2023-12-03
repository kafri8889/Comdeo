package com.anafthdev.comdeo.data.model

import android.provider.MediaStore

/**
 * @property id video id [MediaStore.Video.Media._ID]
 * @property path path in uri
 * @property displayName display name [MediaStore.Video.Media.DISPLAY_NAME]
 * @property dateAdded date added [MediaStore.Video.Media.DATE_ADDED]
 * @property duration duration in ms [MediaStore.Video.Media.DURATION]
 * @property size size in bytes [MediaStore.Video.Media.SIZE]
 */
data class Video(
    val id: Long,
    val path: String,
    val displayName: String,
    val dateAdded: Long,
    val duration: Long,
    val size: Long,
)
