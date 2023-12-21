package com.anafthdev.comdeo.ui.change_video_name

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.anafthdev.comdeo.data.model.Video
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
data class ChangeVideoNameState(
	val name: String = "",
	val video: Video? = null
): Parcelable
