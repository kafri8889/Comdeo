package com.anafthdev.comdeo.ui.change_video_name

sealed interface ChangeVideoNameAction {
	data class UpdateName(val name: String): ChangeVideoNameAction

	/**
	 * Get all video from device
	 */
	data object Scan: ChangeVideoNameAction

	/**
	 * Save video
	 */
	data object Save: ChangeVideoNameAction
}