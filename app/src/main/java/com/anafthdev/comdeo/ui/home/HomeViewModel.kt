package com.anafthdev.comdeo.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.anafthdev.comdeo.data.SortVideoBy
import com.anafthdev.comdeo.data.repository.VideoRepository
import com.anafthdev.comdeo.foundation.base.ui.BaseViewModel
import com.anafthdev.comdeo.foundation.common.VideoManager
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
class HomeViewModel @Inject constructor(
	private val videoRepository: VideoRepository,
	private val videoManager: VideoManager,
	private val workManager: WorkManager,
	savedStateHandle: SavedStateHandle
): BaseViewModel<HomeState, HomeAction>(
	savedStateHandle = savedStateHandle,
	defaultState = HomeState()
) {

	private val _sortVideoBy = MutableStateFlow(SortVideoBy.Name)

	init {
		viewModelScope.launch {
			// Get videos from repository and apply sort
			_sortVideoBy.flatMapLatest { sortBy ->
				videoRepository.getAll().map {
					when (sortBy) {
						SortVideoBy.Name -> it.sortedBy { it.displayName }
						SortVideoBy.DateAdded -> it.sortedByDescending { it.dateAdded }
						SortVideoBy.Duration -> it.sortedByDescending { it.duration }
					}
				}
			}.collectLatest { videos ->
				updateState {
					copy(
						videos = videos
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
							selectedVideos = emptyList()
						)
					}
					else -> {}
				}
			}
		}
	}

	override fun onAction(action: HomeAction) {
		when (action) {
			is HomeAction.SortVideoBy -> viewModelScope.launch {
				_sortVideoBy.update { action.sortVideoBy }
			}
			is HomeAction.UpdateSelectedVideo -> viewModelScope.launch {
				updateState {
					copy(
						selectedVideos = selectedVideos.toMutableList().apply {
							if (action.video in this) remove(action.video)
							else add(action.video)
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
						selectedVideos = if (!action.show) emptyList() else selectedVideos
					)
				}
			}
			is HomeAction.SelectAllVideo -> viewModelScope.launch {
				updateState {
					copy(
						selectedVideos = if (action.select) ArrayList(videos) else emptyList()
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