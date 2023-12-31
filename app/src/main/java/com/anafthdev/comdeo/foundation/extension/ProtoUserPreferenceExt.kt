package com.anafthdev.comdeo.foundation.extension

import com.anafthdev.comdeo.ProtoUserPreference
import com.anafthdev.comdeo.data.VideoSortOption
import com.anafthdev.comdeo.data.model.UserPreference

fun ProtoUserPreference.toUserPreference(): UserPreference = UserPreference(VideoSortOption.entries[videoSortOption])
