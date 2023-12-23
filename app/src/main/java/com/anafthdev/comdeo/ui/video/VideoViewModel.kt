package com.anafthdev.comdeo.ui.video

import androidx.lifecycle.SavedStateHandle
import com.anafthdev.comdeo.data.DestinationArgument
import com.anafthdev.comdeo.foundation.base.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle
): BaseViewModel<VideoState, VideoAction>(
	defaultState = VideoState(),
	savedStateHandle = savedStateHandle
) {

	private val argVideoId = savedStateHandle.getStateFlow(DestinationArgument.ARG_VIDEO_ID, -1L)

	init {

	}

	override fun onAction(action: VideoAction) {

	}
}