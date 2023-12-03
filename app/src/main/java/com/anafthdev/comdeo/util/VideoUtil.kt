package com.anafthdev.comdeo.util

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.anafthdev.comdeo.data.model.Video

object VideoUtil {

    fun findAllVideo(context: Context): Set<Video> {
        return hashSetOf<Video>().apply {
            val externalContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

            val projection = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
            )

            val cursorIndexVideoID: Int
            val cursorIndexVideoDisplayName: Int
            val cursorIndexVideoDateAdded: Int
            val cursorIndexVideoDuration: Int
            val cursorIndexVideoSize: Int

            val cursor = context.contentResolver.query(
                externalContentUri,
                projection,
                null,
                null,
                null
            )

            if (cursor != null) {
                cursorIndexVideoID = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                cursorIndexVideoDisplayName = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                cursorIndexVideoDateAdded = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)
                cursorIndexVideoDuration = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                cursorIndexVideoSize = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(cursorIndexVideoID)
                    val displayName = cursor.getString(cursorIndexVideoDisplayName)
                    val dateAdded = cursor.getLong(cursorIndexVideoDateAdded)
                    val duration = cursor.getLong(cursorIndexVideoDuration)
                    val size = cursor.getLong(cursorIndexVideoSize)

                    val path = Uri.withAppendedPath(externalContentUri, id.toString()).toString()

                    add(
                        Video(
                            id = id,
                            path = path,
                            displayName = displayName,
                            dateAdded = dateAdded,
                            duration = duration,
                            size = size
                        )
                    )
                }
            }

            cursor?.close()
        }
    }

}