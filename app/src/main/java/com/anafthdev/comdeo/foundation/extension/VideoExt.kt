package com.anafthdev.comdeo.foundation.extension

import android.content.Context
import android.text.format.Formatter
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import com.anafthdev.comdeo.R
import com.anafthdev.comdeo.data.model.Video
import com.anafthdev.comdeo.foundation.common.formatDuration
import com.anafthdev.comdeo.util.UriUtil

val Video.fileExtension: String
	get() = displayName.substringAfterLast(".")

@Composable
fun Video.formattedResolution(): String {
	return "$width x $height ${stringResource(id = R.string.pixels)}"
}

@Composable
fun Video.formattedDuration(): String {
	return if (duration == null) "00:00" else formatDuration(duration)
}

@Composable
fun Video.formattedSize(): String {
	val context = LocalContext.current
	return Formatter.formatFileSize(context, size)
}

fun Video.getRealPath(context: Context): String? {
	return relativePath ?: UriUtil.getRealPathFromURIAPI19(context, path.toUri())
}
