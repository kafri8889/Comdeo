package com.anafthdev.comdeo.ui.change_video_name

import android.os.Parcelable
import com.anafthdev.comdeo.data.model.Video
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChangeVideoNameState(
	val name: String = "",
	val video: Video? = null
): Parcelable
