package com.anafthdev.comdeo.ui.video_info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anafthdev.comdeo.R
import com.anafthdev.comdeo.data.model.Video
import com.anafthdev.comdeo.foundation.extension.fileExtension
import com.anafthdev.comdeo.foundation.extension.formattedDuration
import com.anafthdev.comdeo.foundation.extension.formattedResolution
import com.anafthdev.comdeo.foundation.extension.formattedSize
import com.anafthdev.comdeo.foundation.extension.getRealPath
import com.anafthdev.comdeo.foundation.theme.ComdeoTheme
import com.anafthdev.comdeo.foundation.uicomponent.DragHandle

/**
 * @return [title, text]
 */
private val videoDetailTitles: @Composable (Video) -> List<Pair<String, String?>>
	get() = { video ->
		val context = LocalContext.current

		listOf(
			stringResource(id = R.string.name) to video.displayName,
			stringResource(id = R.string.format) to video.fileExtension,
			stringResource(id = R.string.size) to video.formattedSize(),
			stringResource(id = R.string.resolution) to video.formattedResolution(),
			stringResource(id = R.string.duration) to video.formattedDuration(),
			stringResource(id = R.string.path) to video.getRealPath(context),
		)
	}

@Preview
@Composable
private fun VideoInfoScreenPreview() {
	ComdeoTheme {
		Box(
			modifier = Modifier
				.clip(RoundedCornerShape(topStartPercent = 36, topEndPercent = 36))
				.background(MaterialTheme.colorScheme.surface)
		) {
			VideoInfoScreenContent(
				state = VideoInfoState(),
				modifier = Modifier
					.padding(16.dp)
			)
		}
	}
}

@Composable
fun VideoInfoScreen(
	viewModel: VideoInfoViewModel
) {

	val state by viewModel.state.collectAsStateWithLifecycle()

	VideoInfoScreenContent(
		state = state,
		modifier = Modifier
			.padding(16.dp)
			.fillMaxWidth()
	)
}

@Composable
private fun VideoInfoScreenContent(
	state: VideoInfoState,
	modifier: Modifier = Modifier
) {

	Column(
		verticalArrangement = Arrangement.spacedBy(16.dp),
		modifier = Modifier
			.padding(bottom = 16.dp)
			.then(modifier)
	) {
		DragHandle(
			modifier = Modifier
				.fillMaxWidth(0.16f)
				.align(Alignment.CenterHorizontally)
		)

		Text(
			text = stringResource(id = R.string.detail),
			style = MaterialTheme.typography.titleMedium,
			modifier = Modifier
				.align(Alignment.CenterHorizontally)
		)

		state.video?.let { video ->
			for ((title, text) in videoDetailTitles(video)) {
				VideoDetailItem(
					title = title,
					text = text
				)
			}
		}
	}
}

@Composable
private fun VideoDetailItem(
	title: String,
	text: String?,
	modifier: Modifier = Modifier
) {

	if (text != null) {
		Column(
			verticalArrangement = Arrangement.spacedBy(4.dp),
			modifier = modifier
		) {
			Text(
				text = "$title:",
				style = MaterialTheme.typography.bodyMedium.copy(
					color = LocalContentColor.current.copy(alpha = 0.48f)
				)
			)

			Text(
				text = text,
				style = MaterialTheme.typography.bodyMedium
			)
		}
	}
}
