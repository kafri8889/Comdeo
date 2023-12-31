package com.anafthdev.comdeo.ui.video

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
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
class VideoViewModel @Inject constructor(
	private val videoRepository: VideoRepository,
	savedStateHandle: SavedStateHandle
): BaseViewModel<VideoState, VideoAction>(
	defaultState = VideoState(),
	savedStateHandle = savedStateHandle
) {

	private val argVideoId = savedStateHandle.getStateFlow(DestinationArgument.ARG_VIDEO_ID, -1L)

	private val runnable = object : Runnable {
		override fun run() {
			exoPlayer?.let {
				updateState {
					copy(
						currentPosition = exoPlayer!!.currentPosition
					)
				}
			}

			handler.postDelayed(this, 1000)
		}
	}

	private val handler = Handler(Looper.getMainLooper())

	private var exoPlayer: ExoPlayer? = null

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

	fun setExoPlayer(player: ExoPlayer) {
		exoPlayer = player
	}

	override fun onAction(action: VideoAction) {
		when (action) {
			is VideoAction.SetIsPlaying -> {
				if (action.isPlaying) handler.post(runnable)
				else handler.removeCallbacks(runnable)

				updateState {
					copy(
						isPlaying = action.isPlaying
					)
				}
			}
			VideoAction.Previous -> {}
			VideoAction.Next -> {}
		}
	}
}