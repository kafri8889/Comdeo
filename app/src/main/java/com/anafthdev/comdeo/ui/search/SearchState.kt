package com.anafthdev.comdeo.ui.search

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.anafthdev.comdeo.data.model.Video
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * @property query query
 * @property result search results
 */
@Parcelize
@Immutable
data class SearchState(
	val query: String = "",
	val result: @RawValue ImmutableList<Video> = persistentListOf()
): Parcelable
