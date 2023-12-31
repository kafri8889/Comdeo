package com.anafthdev.comdeo.ui.home

import com.anafthdev.comdeo.data.VideoSortOption
import com.anafthdev.comdeo.data.model.Video

sealed interface HomeAction {
	data class SortVideoBy(val videoSortOption: VideoSortOption): HomeAction
	data class UpdateSelectedVideo(val video: Video): HomeAction
	data class ShowConfirmationDialog(val show: Boolean): HomeAction
	data class ShowVideoCheckbox(val show: Boolean): HomeAction
	data class SelectAllVideo(val select: Boolean): HomeAction
	data class DeleteVideos(val videos: List<Video>): HomeAction
}