package com.anafthdev.comdeo.foundation.uicomponent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import com.anafthdev.comdeo.R
import com.anafthdev.comdeo.data.model.Video
import com.anafthdev.comdeo.foundation.common.prependZero
import com.anafthdev.comdeo.util.DateUtil
import com.anafthdev.comdeo.util.VideoUtil
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VideoItem(
	video: Video,
	modifier: Modifier = Modifier,
	isCheckboxVisible: Boolean = false,
	checked: Boolean = false,
	onLongClick: () -> Unit,
	onClick: () -> Unit
) {

	BoxWithConstraints(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.fillMaxWidth()
			.combinedClickable(
				onClick = onClick,
				onLongClick = onLongClick
			)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(8.dp),
			modifier = modifier
				.padding(8.dp)
		) {
			VideoThumbnail(
				video = video,
				maxWidth = this@BoxWithConstraints.maxWidth
			)

			VideoInfo(
				displayName = video.displayName,
				dateAdded = video.dateAdded,
				duration = video.duration ?: 0L,
				modifier = Modifier
					.align(Alignment.Top)
					.weight(1f)
			)

			AnimatedVisibility(
				visible = isCheckboxVisible,
				enter = scaleIn(tween(256)),
				exit = scaleOut(tween(256))
			) {
				CircleCheckbox(
					checked = checked,
					onCheckedChange = {
						onClick()
					}
				)
			}
		}
	}
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun VideoThumbnail(
	video: Video,
	maxWidth: Dp,
	modifier: Modifier = Modifier
) {
	val resolution = remember(video) {
		VideoUtil.getResolution(video.width, video.height)
	}

	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.width(maxWidth / 3.2f)
			.aspectRatio(1.4f / 1f)
			.clip(RoundedCornerShape(16))
			.background(Color.LightGray)
			.then(modifier)
	) {
		if (resolution.isNotBlank()) {
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.padding(4.dp)
					.clip(RoundedCornerShape(24))
					.background(Color.Black.copy(alpha = 0.32f))
					.zIndex(10f)
					.align(Alignment.TopStart)
			) {
				Text(
					text = resolution,
					style = MaterialTheme.typography.labelSmall,
					modifier = Modifier
						.padding(4.dp)
				)
			}
		}

		Icon(
			painter = painterResource(id = R.drawable.ic_video),
			contentDescription = null,
			tint = Color.DarkGray,
			modifier = Modifier
				.zIndex(1f)
		)

		GlideImage(
			model = video.path.toUri(),
			contentScale = ContentScale.Crop,
			contentDescription = null,
			modifier = Modifier
				.matchParentSize()
				.zIndex(2f)
		)
	}
}

@Composable
private fun VideoInfo(
	displayName: String,
	dateAdded: Long,
	duration: Long,
	modifier: Modifier = Modifier
) {

	Column(
		verticalArrangement = Arrangement.spacedBy(4.dp),
		modifier = modifier
	) {
		Text(
			text = displayName,
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			style = MaterialTheme.typography.titleMedium
		)

		Text(
			text = buildString {
				val seconds = (duration / 1000) % 60
				val minutes = (duration / (60 * 1000)) % 60
				val hours = (duration / (60 * 60 * 1000)) % 24
				val days = duration / (24 * 60 * 60 * 1000)

				val time = "${prependZero(hours)}:${prependZero(minutes)}:${prependZero(seconds)}"

				append(if (days > 0) "${prependZero(days)}:$time" else time)
			},
			style = MaterialTheme.typography.labelMedium.copy(
				fontWeight = FontWeight.Light
			)
		)

		Text(
			text = DateUtil.formatMedium(dateAdded),
			style = MaterialTheme.typography.labelMedium.copy(
				fontWeight = FontWeight.Light
			)
		)
	}
}
