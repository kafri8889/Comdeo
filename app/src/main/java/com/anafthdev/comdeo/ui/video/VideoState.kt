package com.anafthdev.comdeo.ui.video

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.anafthdev.comdeo.data.model.Video
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class VideoState(
	val video: Video? = null,
): Parcelable
