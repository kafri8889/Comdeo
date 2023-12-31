package com.anafthdev.comdeo.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import com.anafthdev.comdeo.ProtoUserPreference
import com.anafthdev.comdeo.data.VideoSortOption
import com.anafthdev.comdeo.data.model.UserPreference
import com.anafthdev.comdeo.foundation.extension.toUserPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferenceRepository @Inject constructor(
	private val userPreferenceDatastore: DataStore<ProtoUserPreference>
) {

	val getUserPreference: Flow<UserPreference>
		get() = userPreferenceDatastore.data.map { it.toUserPreference() }

	suspend fun setVideoSortOption(option: VideoSortOption) {
		userPreferenceDatastore.updateData { pref ->
			pref.copy(
				videoSortOption = option.ordinal
			)
		}
	}

	companion object {
		val corruptionHandler = ReplaceFileCorruptionHandler(
			produceNewData = { ProtoUserPreference() }
		)
	}
}