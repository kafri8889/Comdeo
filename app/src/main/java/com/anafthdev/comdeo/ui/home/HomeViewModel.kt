package com.anafthdev.comdeo.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.anafthdev.comdeo.data.SortVideoBy
import com.anafthdev.comdeo.data.repository.VideoRepository
import com.anafthdev.comdeo.foundation.base.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
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
                        SortVideoBy.DateAdded -> it.sortedBy { it.dateAdded }
                        SortVideoBy.Duration -> it.sortedBy { it.duration }
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
    }

    override fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.SortVideoBy -> viewModelScope.launch {
                _sortVideoBy.update { action.sortVideoBy }
            }
        }
    }
}