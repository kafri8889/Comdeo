package com.anafthdev.comdeo.data.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.anafthdev.comdeo.data.VideoSortOption
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
data class UserPreference(
	val videoSortOption: VideoSortOption
): Parcelable
