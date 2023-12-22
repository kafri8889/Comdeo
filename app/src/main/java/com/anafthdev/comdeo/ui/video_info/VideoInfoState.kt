package com.anafthdev.comdeo.ui.video_info

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.anafthdev.comdeo.data.model.Video
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
data class VideoInfoState(
	val video: Video? = null,
): Parcelable
