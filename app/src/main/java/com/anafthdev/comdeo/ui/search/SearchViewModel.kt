package com.anafthdev.comdeo.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.anafthdev.comdeo.data.repository.VideoRepository
import com.anafthdev.comdeo.foundation.base.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
	private val videoRepository: VideoRepository,
	savedStateHandle: SavedStateHandle
): BaseViewModel<SearchState, SearchAction>(
	savedStateHandle = savedStateHandle,
	defaultState = SearchState()
) {

	init {
		viewModelScope.launch {
			combine(
				videoRepository.getAll(),
				state
			) { videos, searchState ->
				videos to searchState
			}.collectLatest { (videos, searchState) ->
				updateState {
					copy(
						result = videos.filter {
							it.displayName.contains(searchState.query, true)
						}
					)
				}
			}
		}
	}

	override fun onAction(action: SearchAction) {
		when (action) {
			is SearchAction.SetQuery -> viewModelScope.launch {
				updateState {
					copy(
						query = action.query
					)
				}
			}
		}
	}

}