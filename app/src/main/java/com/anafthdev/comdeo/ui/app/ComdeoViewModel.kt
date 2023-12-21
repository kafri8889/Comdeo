package com.anafthdev.comdeo.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.comdeo.foundation.common.VideoManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComdeoViewModel @Inject constructor(
	private val videoManager: VideoManager
): ViewModel() {

	fun scanVideo() = viewModelScope.launch {
		videoManager.scan()
	}

}