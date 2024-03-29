package com.anafthdev.comdeo.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.anafthdev.comdeo.data.VideoSortOption
import com.anafthdev.comdeo.data.repository.UserPreferenceRepository
import com.anafthdev.comdeo.data.repository.VideoRepository
import com.anafthdev.comdeo.foundation.base.ui.BaseViewModel
import com.anafthdev.comdeo.foundation.common.VideoManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
	private val userPreferenceRepository: UserPreferenceRepository,
	private val videoRepository: VideoRepository,
	private val videoManager: VideoManager,
	private val workManager: WorkManager,
	savedStateHandle: SavedStateHandle
): BaseViewModel<HomeState, HomeAction>(
	savedStateHandle = savedStateHandle,
	defaultState = HomeState()
) {

	private val _videoSortOption = MutableStateFlow(VideoSortOption.Name)

	init {
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
			}.collectLatest { videos ->
				updateState {
					copy(
						videos = videos.toImmutableList()
					)
				}
			}
		}

		// when user renamed the video and success, disable multiple selection mode
		viewModelScope.launch {
			videoManager.currentScanWorkId.filterNotNull().flatMapLatest { uuid ->
				workManager.getWorkInfoByIdFlow(uuid)
			}.collectLatest { workInfo ->
				when (workInfo.state) {
					WorkInfo.State.SUCCEEDED -> updateState {
						copy(
							showVideoCheckbox = false,
							selectedVideos = persistentListOf()
						)
					}
					else -> {}
				}
			}
		}

		viewModelScope.launch {
			userPreferenceRepository.getUserPreference.collectLatest { pref ->
				_videoSortOption.emit(pref.videoSortOption)
			}
		}
	}

	override fun onAction(action: HomeAction) {
		when (action) {
			is HomeAction.SortVideoBy -> viewModelScope.launch {
				userPreferenceRepository.setVideoSortOption(action.videoSortOption)
			}
			is HomeAction.UpdateSelectedVideo -> viewModelScope.launch {
				updateState {
					copy(
						selectedVideos = selectedVideos.toPersistentList().let {
							if (action.video in it) it.remove(action.video)
							else it.add(action.video)
						}
					)
				}
			}
			is HomeAction.ShowConfirmationDialog -> viewModelScope.launch {
				updateState {
					copy(
						showConfirmationDialog = action.show
					)
				}
			}
			is HomeAction.ShowVideoCheckbox -> viewModelScope.launch {
				updateState {
					copy(
						showVideoCheckbox = action.show,
						// Remove selected video when showVideoCheckbox is false
						selectedVideos = if (!action.show) persistentListOf() else selectedVideos
					)
				}
			}
			is HomeAction.SelectAllVideo -> viewModelScope.launch {
				updateState {
					copy(
						selectedVideos = if (action.select) videos else persistentListOf()
					)
				}
			}
			is HomeAction.DeleteVideos -> viewModelScope.launch {
				videoRepository.delete(action.videos)

				onAction(HomeAction.ShowVideoCheckbox(false))
			}
		}
	}
}