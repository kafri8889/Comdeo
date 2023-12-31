package com.anafthdev.comdeo.ui.video

sealed interface VideoAction {

	data class SetIsPlaying(val isPlaying: Boolean): VideoAction
	data object Previous: VideoAction
	data object Next: VideoAction

}