package com.anafthdev.comdeo.data.model

import android.provider.MediaStore
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @property id video id [MediaStore.Video.Media._ID]
 * @property path path in uri
 * @property displayName display name [MediaStore.Video.Media.DISPLAY_NAME]
 * @property dateAdded date added [MediaStore.Video.Media.DATE_ADDED]
 * @property duration duration in ms [MediaStore.Video.Media.DURATION]
 * @property size size in bytes [MediaStore.Video.Media.SIZE]
 */
@Entity(tableName = "video")
data class Video(
    @PrimaryKey
    @ColumnInfo(name = "id_video") val id: Long,
    @ColumnInfo(name = "path_video") val path: String,
    @ColumnInfo(name = "displayName_video") val displayName: String,
    @ColumnInfo(name = "dateAdded_video") val dateAdded: Long,
    @ColumnInfo(name = "duration_video") val duration: Long,
    @ColumnInfo(name = "size_video") val size: Long,
)
