package com.anafthdev.comdeo.ui.home

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.anafthdev.comdeo.data.model.Video
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
data class HomeState(
	val showVideoCheckbox: Boolean = false,
	val showConfirmationDialog: Boolean = false,
	val selectedVideos: List<Video> = emptyList(),
	val videos: List<Video> = emptyList()
): Parcelable
