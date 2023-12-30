package com.anafthdev.comdeo.ui.home

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.anafthdev.comdeo.data.model.Video
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
@Immutable
data class HomeState(
	val showVideoCheckbox: Boolean = false,
	val showConfirmationDialog: Boolean = false,
	val selectedVideos: @RawValue ImmutableList<Video> = persistentListOf(),
	val videos: @RawValue ImmutableList<Video> = persistentListOf()
): Parcelable
