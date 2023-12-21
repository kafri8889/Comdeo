package com.anafthdev.comdeo.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.anafthdev.comdeo.R

object FileUtils {

	fun shareMultipleFiles(context: Context, uris: List<Uri>) {
		val intent = Intent(Intent.ACTION_SEND).apply {
			type = "video/*"

			putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(uris))
		}

		context.startActivity(
			Intent.createChooser(intent, context.getString(R.string.share_to))
		)
	}

	/**
	 * @param uri file uri
	 * @param where condition
	 */
	fun renameFile(context: Context, uri: Uri, newName: String, where: () -> String = {""}) {
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) renameAPI29(context, uri, newName, where)
		else renameAPI30(context, uri, newName)
	}

	/**
	 * @param uri file uri
	 */
	@RequiresApi(Build.VERSION_CODES.R)
	private fun renameAPI30(context: Context, uri: Uri, newName: String) {
		val contentValues = ContentValues().apply {
			put(MediaStore.Video.Media.DISPLAY_NAME, newName)
		}

		context.contentResolver.update(uri, contentValues, null)
	}

	/**
	 * @param uri file uri
	 * @param where condition
	 */
	private fun renameAPI29(context: Context, uri: Uri, newName: String, where: () -> String) {
		context.grantUriPermission(
			context.packageName,
			uri,
			Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
		)

		val contentValues = ContentValues().apply {
			put(MediaStore.Video.Media.DISPLAY_NAME, newName)
		}

		context.contentResolver.update(uri, contentValues, where(), null)
	}

}