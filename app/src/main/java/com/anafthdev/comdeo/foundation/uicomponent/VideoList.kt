package com.anafthdev.comdeo.foundation.uicomponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.anafthdev.comdeo.data.model.Video
import com.bumptech.glide.integration.compose.rememberGlidePreloadingData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Component for displaying video list
 */
@Composable
fun VideoList(
	videos: ImmutableList<Video>,
	modifier: Modifier = Modifier,
	selectedVideos: ImmutableList<Video> = persistentListOf(),
	showCheckbox: Boolean = false,
	onLongClick: (Video) -> Unit,
	onClick: (Video) -> Unit
) {

	val preloadingData = rememberGlidePreloadingData(
		dataSize = videos.size,
		dataGetter = { i -> videos[i] },
		preloadImageSize = Size(128f, 128f),
		numberOfItemsToPreload = 20
	) { video, requestBuilder ->
		requestBuilder.load(video.path.toUri())
	}

	LazyColumn(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(8.dp),
		modifier = modifier
	) {
		items(
			count = preloadingData.size,
			key = { index -> videos[index].id }
		) { index ->
			val (video, preloadRequest) = preloadingData[index]

			VideoItem(
				video = video,
				isCheckboxVisible = showCheckbox,
				checked = video in selectedVideos,
				preloadRequest = { preloadRequest },
				onClick = {
					onClick(video)
				},
				onLongClick = {
					onLongClick(video)
				}
			)
		}
	}

}