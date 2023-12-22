package com.anafthdev.comdeo.ui.video_info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.anafthdev.comdeo.data.DestinationArgument
import com.anafthdev.comdeo.data.repository.VideoRepository
import com.anafthdev.comdeo.foundation.base.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class VideoInfoViewModel @Inject constructor(
	private val videoRepository: VideoRepository,
	savedStateHandle: SavedStateHandle
): BaseViewModel<VideoInfoState, Unit>(
	savedStateHandle = savedStateHandle,
	defaultState = VideoInfoState()
) {

	private val argVideoId = savedStateHandle.getStateFlow(DestinationArgument.ARG_VIDEO_ID, -1L)

	init {
		viewModelScope.launch {
			argVideoId.flatMapLatest { id ->
				videoRepository.getById(id)
			}.filterNotNull().collectLatest { video ->
				updateState {
					copy(
						video = video
					)
				}
			}
		}
	}

	override fun onAction(action: Unit) {}
}