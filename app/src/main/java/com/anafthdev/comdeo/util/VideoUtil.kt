package com.anafthdev.comdeo.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.RequiresApi
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import androidx.core.net.toUri
import com.anafthdev.comdeo.data.model.Video
import timber.log.Timber

object VideoUtil {

	fun findAllVideo(context: Context): Set<Video> {
		return hashSetOf<Video>().apply {
			val externalContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

			val projection = arrayListOf(
				MediaStore.Video.Media._ID,
				MediaStore.Video.Media.DISPLAY_NAME,
				MediaStore.Video.Media.DATE_MODIFIED,
				MediaStore.Video.Media.DURATION,
				MediaStore.Video.Media.SIZE,
				MediaStore.Video.Media.WIDTH,
				MediaStore.Video.Media.HEIGHT
			).apply {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) add(MediaStore.Video.Media.RELATIVE_PATH)
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
					add(MediaStore.Video.Media.RESOLUTION)
					add(MediaStore.Video.Media.BITRATE)
				}
			}

			val cursor = try {
				context.contentResolver.query(
					externalContentUri,
					projection.toTypedArray(),
					null,
					null,
					null
				)
			} catch (e: Exception) {
				Timber.e(e, e.message)
				null
			}

			if (cursor != null) {
				val cursorIndexVideoID = cursor.getColumnIndex(MediaStore.Video.Media._ID)
				val cursorIndexVideoDisplayName =
					cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)
				val cursorIndexVideoDateAdded =
					cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED)
				val cursorIndexVideoDuration =
					cursor.getColumnIndex(MediaStore.Video.Media.DURATION)
				val cursorIndexVideoSize = cursor.getColumnIndex(MediaStore.Video.Media.SIZE)
				val cursorIndexVideoWidth =
					cursor.getColumnIndex(MediaStore.Video.Media.WIDTH)
				val cursorIndexVideoHeight =
					cursor.getColumnIndex(MediaStore.Video.Media.HEIGHT)
				val cursorIndexVideoRelativePath =
					cursor.getColumnIndex(MediaStore.Video.Media.RELATIVE_PATH)
				val cursorIndexVideoResolution =
					cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION)
				val cursorIndexVideoBitrate =
					cursor.getColumnIndex(MediaStore.Video.Media.BITRATE)

				while (cursor.moveToNext()) {
					val id = cursor.getLong(cursorIndexVideoID)
					val displayName = cursor.getString(cursorIndexVideoDisplayName)
					val dateAdded = cursor.getLong(cursorIndexVideoDateAdded)
					val duration = cursor.getLongOrNull(cursorIndexVideoDuration)
					val size = cursor.getLong(cursorIndexVideoSize)
					val width = cursor.getInt(cursorIndexVideoWidth)
					val height = cursor.getInt(cursorIndexVideoHeight)
					val relativePath = cursor.getStringOrNull(cursorIndexVideoRelativePath)
					val resolution = cursor.getStringOrNull(cursorIndexVideoResolution)
					val bitrate = cursor.getIntOrNull(cursorIndexVideoBitrate)

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

	/**
	 * @param onDeleted invoked when videos are deleted, used for API 29 and below
	 */
	fun deleteMultipleVideo(
		context: Context,
		videos: List<Video>,
		onDeleted: () -> Unit = {}
	): IntentSenderRequest? {
		return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
			deleteMultipleVideoAPI29(context, videos, onDeleted)
			null
		} else deleteMultipleVideoAPI30(context, videos)
	}

	private fun deleteMultipleVideoAPI29(context: Context, videos: List<Video>, onDeleted: () -> Unit) {
		val externalContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
		context.contentResolver.delete(
			externalContentUri,
			"${MediaStore.Video.Media._ID} IN (" +  Array(videos.size) { "?" }.joinToString() + ")",
			videos.map { it.id.toString() }.toTypedArray()
		).let { rowsDeleted ->
			if (rowsDeleted > 0) onDeleted()
		}
	}

	@RequiresApi(Build.VERSION_CODES.R)
	private fun deleteMultipleVideoAPI30(context: Context, videos: List<Video>): IntentSenderRequest {
		val intentSender = MediaStore.createDeleteRequest(context.contentResolver, videos.map { it.path.toUri() })
		return IntentSenderRequest.Builder(intentSender)
			.setFillInIntent(null)
			.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION, 0)
			.build()
	}

}