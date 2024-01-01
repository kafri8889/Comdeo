package com.anafthdev.comdeo.ui.video

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import com.anafthdev.comdeo.data.DestinationArgument
import com.anafthdev.comdeo.data.VideoSortOption
import com.anafthdev.comdeo.data.model.Video
import com.anafthdev.comdeo.data.repository.UserPreferenceRepository
import com.anafthdev.comdeo.data.repository.VideoRepository
import com.anafthdev.comdeo.foundation.base.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class VideoViewModel @Inject constructor(
	private val userPreferenceRepository: UserPreferenceRepository,
	private val videoRepository: VideoRepository,
	savedStateHandle: SavedStateHandle
): BaseViewModel<VideoState, VideoAction>(
	defaultState = VideoState(),
	savedStateHandle = savedStateHandle
) {

	private val argVideoId = savedStateHandle.getStateFlow(DestinationArgument.ARG_VIDEO_ID, -1L)

	private val _currentVideo = MutableStateFlow<Video?>(null)
	private val _videoSortOption = MutableStateFlow(VideoSortOption.Name)

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
	private val videos = arrayListOf<Video>()
	private var exoPlayer: ExoPlayer? = null

	init {
		viewModelScope.launch {
			argVideoId.flatMapLatest { id ->
				videoRepository.getById(id)
			}.filterNotNull().collectLatest(_currentVideo::emit)
		}

		viewModelScope.launch {
			_currentVideo.filterNotNull().collectLatest { video ->
				updateState {
					copy(
						video = video
					)
				}
			}
		}

		viewModelScope.launch {
			// Get videos from repository and apply sort
			_videoSortOption.flatMapLatest { sortBy ->
				videoRepository.getAll().map {
					when (sortBy) {
						VideoSortOption.Name -> it.sortedBy { it.displayName }
						VideoSortOption.DateAdded -> it.sortedByDescending { it.dateAdded }
						VideoSortOption.Duration -> it.sortedByDescending { it.duration }
					}
				}
			}.collectLatest { videoList ->
				videos.apply {
					clear()
					addAll(videoList)
				}
			}
		}

		viewModelScope.launch {
			userPreferenceRepository.getUserPreference.collectLatest { pref ->
				_videoSortOption.update { pref.videoSortOption }
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
			VideoAction.Previous -> {
				state.value.video?.let { video ->
					val currentVideoIndex = videos.indexOfFirst { it.id == video.id }

					_currentVideo.update {
						if (currentVideoIndex <= 0) videos.last()
						else videos[currentVideoIndex - 1]
					}
				}
			}
			VideoAction.Next -> {
				state.value.video?.let { video ->
					val currentVideoIndex = videos.indexOfFirst { it.id == video.id }

					_currentVideo.update {
						if (currentVideoIndex >= videos.lastIndex) videos.first()
						else videos[currentVideoIndex + 1]
					}
				}
			}
		}
	}
}