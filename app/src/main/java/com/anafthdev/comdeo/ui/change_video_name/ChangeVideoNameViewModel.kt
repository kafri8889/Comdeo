package com.anafthdev.comdeo.ui.change_video_name

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.anafthdev.comdeo.data.DestinationArgument
import com.anafthdev.comdeo.data.repository.VideoRepository
import com.anafthdev.comdeo.foundation.base.ui.BaseViewModel
import com.anafthdev.comdeo.foundation.common.VideoManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ChangeVideoNameViewModel @Inject constructor(
	private val videoRepository: VideoRepository,
	private val videoManager: VideoManager,
	savedStateHandle: SavedStateHandle
): BaseViewModel<ChangeVideoNameState, ChangeVideoNameAction>(
	defaultState = ChangeVideoNameState(),
	savedStateHandle = savedStateHandle
) {

	private val argVideoId = savedStateHandle.getStateFlow(DestinationArgument.ARG_VIDEO_ID, -1L)

	init {
		viewModelScope.launch {
			argVideoId.flatMapLatest { id ->
				videoRepository.getById(id)
			}.filterNotNull().collectLatest { video ->
				updateState {
					copy(
						name = video.displayName.substringBeforeLast("."), // Remove file extension
						video = video
					)
				}
			}
		}
	}

	override fun onAction(action: ChangeVideoNameAction) {
		when (action) {
			is ChangeVideoNameAction.UpdateName -> viewModelScope.launch {
				updateState {
					copy(
						name = action.name
					)
				}
			}
			ChangeVideoNameAction.Scan -> viewModelScope.launch {
				videoManager.scan()
			}
			ChangeVideoNameAction.Save -> viewModelScope.launch {

			}
		}
	}
}