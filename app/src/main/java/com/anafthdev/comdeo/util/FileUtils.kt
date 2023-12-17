package com.anafthdev.comdeo.util

import android.content.Context
import android.content.Intent
import android.net.Uri
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

}