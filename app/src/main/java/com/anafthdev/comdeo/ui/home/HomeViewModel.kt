package com.anafthdev.comdeo.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.anafthdev.comdeo.data.repository.VideoRepository
import com.anafthdev.comdeo.foundation.base.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    savedStateHandle: SavedStateHandle
): BaseViewModel<HomeState, HomeAction>(
    savedStateHandle = savedStateHandle,
    defaultState = HomeState()
) {

    init {
        viewModelScope.launch {
            videoRepository.getAll().collectLatest { videos ->
                updateState {
                    copy(
                        videos = videos
                    )
                }
            }
        }
    }

    override fun onAction(action: HomeAction) {

    }
}