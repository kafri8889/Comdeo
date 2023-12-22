package com.anafthdev.comdeo.util

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import timber.log.Timber

object UriUtil {

	fun getRealPathFromURIAPI19(context: Context, uri: Uri): String? {

		// DocumentProvider
		if (DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				val docId = DocumentsContract.getDocumentId(uri)
				val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
				val type = split[0]

				if ("primary".equals(type, ignoreCase = true)) {
					return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
				}
			} else if (isDownloadsDocument(uri)) {
				var cursor: Cursor? = null
				try {
					cursor = context.contentResolver.query(uri, arrayOf(MediaStore.MediaColumns.DISPLAY_NAME), null, null, null)
					cursor!!.moveToNext()
					val fileName = cursor.getString(0)
					val path = Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName
					if (!TextUtils.isEmpty(path)) {
						return path
					}
				} finally {
					cursor?.close()
				}
				val id = DocumentsContract.getDocumentId(uri)
				if (id.startsWith("raw:")) {
					return id.replaceFirst("raw:".toRegex(), "")
				}
				val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads"), java.lang.Long.valueOf(id))

				return getDataColumn(context, contentUri, null, null)
			} else if (isMediaDocument(uri)) {
				val docId = DocumentsContract.getDocumentId(uri)
				val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
				val type = split[0]

				var contentUri: Uri? = null
				when (type) {
					"image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
					"video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
					"audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
				}

				val selection = "_id=?"
				val selectionArgs = arrayOf(split[1])

				return getDataColumn(context, contentUri, selection, selectionArgs)
			}// MediaProvider
			// DownloadsProvider
		} else if ("content".equals(uri.scheme!!, ignoreCase = true)) {
			return getDataColumn(context, uri, null, null)
		} else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
			return uri.path
		}// File
		// MediaStore (and general)

		return null
	}


	fun getTreeDocumentPath(uri: Uri): String? {
		if (isExternalStorageDocument(uri)) {
			val docId = DocumentsContract.getTreeDocumentId(uri)
			val split = docId.split(":".toRegex()).toTypedArray()

			Timber.i("split: ${split.contentDeepToString()}")

			val type = split[0]
			return if ("primary".equals(type, ignoreCase = true)) {
				Environment.getExternalStorageDirectory().toString() + "/" + split[1]
			} else "sdcard/${split[1]}"
		}

		return null
	}

	fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
		var cursor: Cursor? = null
		val column = "_data"
		val projection = arrayOf(column)
		try {
			cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs,null)
			if (cursor != null && cursor.moveToFirst()) {
				val columnIndex: Int = cursor.getColumnIndexOrThrow(column)
				return cursor.getString(columnIndex)
			}
		} finally {
			cursor?.close()
		}
		return null
	}

	fun isExternalStorageDocument(uri: Uri): Boolean {
		return "com.android.externalstorage.documents" == uri.authority
	}

	fun isDownloadsDocument(uri: Uri): Boolean {
		return "com.android.providers.downloads.documents" == uri.authority
	}

	fun isMediaDocument(uri: Uri): Boolean {
		return "com.android.providers.media.documents" == uri.authority
	}

}
