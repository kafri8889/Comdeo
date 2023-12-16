package com.anafthdev.comdeo.util

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.core.database.getStringOrNull
import com.anafthdev.comdeo.data.model.Video

object VideoUtil {

	fun findAllVideo(context: Context): Set<Video> {
		return hashSetOf<Video>().apply {
			val externalContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

			val projection = arrayOf(
				MediaStore.Video.Media._ID,
				MediaStore.Video.Media.DISPLAY_NAME,
				MediaStore.Video.Media.DATE_MODIFIED,
				MediaStore.Video.Media.DURATION,
				MediaStore.Video.Media.SIZE,
				MediaStore.Video.Media.WIDTH,
				MediaStore.Video.Media.HEIGHT,
				MediaStore.Video.Media.RESOLUTION,
				MediaStore.Video.Media.BITRATE,
				MediaStore.Video.Media.RELATIVE_PATH,
			)

			val cursor = context.contentResolver.query(
				externalContentUri,
				projection,
				null,
				null,
				null
			)

			if (cursor != null) {
				val cursorIndexVideoID = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
				val cursorIndexVideoDisplayName =
					cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
				val cursorIndexVideoDateAdded =
					cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)
				val cursorIndexVideoDuration =
					cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
				val cursorIndexVideoSize = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
				val cursorIndexVideoWidth =
					cursor.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH)
				val cursorIndexVideoHeight =
					cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT)
				val cursorIndexVideoResolution =
					cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION)
				val cursorIndexVideoBitrate =
					cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BITRATE)
				val cursorIndexVideoRelativePath =
					cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RELATIVE_PATH)

				while (cursor.moveToNext()) {
					val id = cursor.getLong(cursorIndexVideoID)
					val displayName = cursor.getString(cursorIndexVideoDisplayName)
					val dateAdded = cursor.getLong(cursorIndexVideoDateAdded)
					val duration = cursor.getLong(cursorIndexVideoDuration)
					val size = cursor.getLong(cursorIndexVideoSize)
					val width = cursor.getInt(cursorIndexVideoWidth)
					val height = cursor.getInt(cursorIndexVideoHeight)
					val resolution = cursor.getStringOrNull(cursorIndexVideoResolution)
					val bitrate = cursor.getInt(cursorIndexVideoBitrate)
					val relativePath = cursor.getString(cursorIndexVideoRelativePath)

					val path = Uri.withAppendedPath(externalContentUri, id.toString()).toString()

					add(
						Video(
							id = id,
							path = path,
							displayName = displayName,
							dateAdded = dateAdded * 1000,
							duration = duration,
							size = size,
							width = width,
							height = height,
							resolution = resolution,
							bitrate = bitrate,
							relativePath = relativePath,
						)
					)
				}
			}

			cursor?.close()
		}
	}

	/**
	 * Convert width and height to resolution like 240p, 360p, etc.
	 */
	fun getResolution(width: Int, height: Int): String {
		return when {
			width >= 7680 && height >= 4320 -> "8K"
			width >= 3840 && height >= 2160 -> "4K"
			width >= 1920 && height >= 1080 -> "1080p"
			width >= 1280 && height >= 720 -> "720p"
			width >= 854 && height >= 480 -> "480p"
			width >= 640 && height >= 360 -> "360p"
			width >= 426 && height >= 240 -> "240p"
			width >= 256 && height >= 144 -> "144p"
			else -> ""
		}
	}

}