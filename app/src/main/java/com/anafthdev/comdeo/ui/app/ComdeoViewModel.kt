package com.anafthdev.comdeo.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.comdeo.data.model.Video
import com.anafthdev.comdeo.data.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComdeoViewModel @Inject constructor(
	private val videoRepository: VideoRepository
): ViewModel() {

	fun insertVideo(video: Collection<Video>) {
		viewModelScope.launch {
			videoRepository.insert(video)
		}
	}

}