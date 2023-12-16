package com.anafthdev.comdeo.ui.search

import android.os.Parcelable
import com.anafthdev.comdeo.data.model.Video
import kotlinx.parcelize.Parcelize

/**
 * @property query query
 * @property result search results
 */
@Parcelize
data class SearchState(
	val query: String = "",
	val result: List<Video> = emptyList()
): Parcelable
