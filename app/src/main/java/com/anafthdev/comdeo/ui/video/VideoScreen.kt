package com.anafthdev.comdeo.ui.video

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsIgnoringVisibility
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.anafthdev.comdeo.R
import com.anafthdev.comdeo.foundation.base.ui.BaseScreenWrapper
import com.anafthdev.comdeo.foundation.common.SystemBarsVisibility
import com.anafthdev.comdeo.foundation.common.formatDuration
import com.anafthdev.comdeo.foundation.common.installSystemBarsController
import com.anafthdev.comdeo.foundation.common.rememberSystemBarsControllerState
import com.anafthdev.comdeo.foundation.uicomponent.ObserveLifecycle
import com.anafthdev.comdeo.foundation.uicomponent.VideoPlayer

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VideoScreen(
	viewModel: VideoViewModel,
	navigateUp: () -> Unit
) {

	val context = LocalContext.current

	val state by viewModel.state.collectAsStateWithLifecycle()

	val systemBarsControllerState = rememberSystemBarsControllerState(
		initialNavigationBarVisibility = SystemBarsVisibility.Gone,
		initialStatusBarVisibility = SystemBarsVisibility.Gone
	)

	val exoPlayer = remember {
		ExoPlayer.Builder(context)
			.setWakeMode(C.WAKE_MODE_LOCAL)
			.build()
	}

	installSystemBarsController(systemBarsControllerState)

	LaunchedEffect(state.video) {
		if (state.video != null) {
			exoPlayer.apply {
				setMediaItem(MediaItem.fromUri(state.video!!.path))
				prepare()
				play()
			}
		}
	}

	ObserveLifecycle(
		onPause = exoPlayer::pause,
		onResume = exoPlayer::play,
		onDestroy = exoPlayer::release
	)

	BaseScreenWrapper(
		viewModel = viewModel,
		topBar = {
			TopBar(
				state = state,
				visible = systemBarsControllerState.isStatusBarVisible,
				onNavigationIconClicked = navigateUp,
				onMaximizeOrMinimizeClicked = {

				},
				onHeadphoneOrVideoClicked = {

				},
				onLockOrUnlockClicked = {

				}
			)
		},
		bottomBar = {
			BottomBar(
				visible = systemBarsControllerState.isNavigationBarVisible,
				maxPosition = (state.video?.duration ?: 0L).toFloat(),
				currentPosition = { state.currentPosition.toFloat() },
				onSeekTo = { pos ->

				},
				onPlayPause = {

				},
				onPrevious = {

				},
				onNext = {

				}
			)
		},
		modifier = Modifier
			.pointerInput(Unit) {
				detectTapGestures {
					if (systemBarsControllerState.isSystemBarVisible) {
						systemBarsControllerState.hideSystemBar()
					} else systemBarsControllerState.showSystemBar()
				}
			}
			.windowInsetsPadding(WindowInsets.statusBarsIgnoringVisibility)
			.systemBarsPadding()
	) { scaffoldPadding ->
		VideoScreenContent(
			state = state,
			videoPlayer = {
				VideoPlayer(
					player = exoPlayer,
					modifier = Modifier
						.fillMaxWidth()
						.aspectRatio(1f / 1f)
				)
			},
			modifier = Modifier
				.background(MaterialTheme.colorScheme.background)
				.padding(scaffoldPadding)
				.fillMaxSize()
		)
	}

}

@Composable
private fun VideoScreenContent(
	state: VideoState,
	modifier: Modifier = Modifier,
	videoPlayer: @Composable ColumnScope.() -> Unit
) {

	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
		modifier = modifier
	) {
		videoPlayer()
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
	state: VideoState,
	visible: Boolean,
	modifier: Modifier = Modifier,
	onNavigationIconClicked: () -> Unit,
	onMaximizeOrMinimizeClicked: () -> Unit,
	onHeadphoneOrVideoClicked: () -> Unit,
	onLockOrUnlockClicked: () -> Unit
) {
	AnimatedVisibility(
		visible = visible,
		enter = slideInVertically(
			initialOffsetY = { -it },
			animationSpec = tween(256)
		) + fadeIn(tween(256)),
		exit = slideOutVertically(
			targetOffsetY = { -it },
			animationSpec = tween(256)
		) + fadeOut(tween(256))
	) {
		TopAppBar(
			modifier = modifier,
			title = {
				Text(
					text = state.video?.displayName ?: "",
					maxLines = 2,
					overflow = TextOverflow.Ellipsis,
					style = MaterialTheme.typography.bodySmall.copy(
						fontWeight = FontWeight.Medium
					)
				)
			},
			navigationIcon = {
				IconButton(onClick = onNavigationIconClicked) {
					Icon(
						painter = painterResource(id = R.drawable.ic_arrow_left),
						contentDescription = null
					)
				}
			},
			actions = {
				IconButton(onClick = onMaximizeOrMinimizeClicked) {
					Icon(
						painter = painterResource(id = R.drawable.ic_maximize_3),
						contentDescription = null
					)
				}

				IconButton(onClick = onHeadphoneOrVideoClicked) {
					Icon(
						painter = painterResource(id = R.drawable.ic_headphone),
						contentDescription = null
					)
				}

				IconButton(onClick = onLockOrUnlockClicked) {
					Icon(
						painter = painterResource(id = R.drawable.ic_unlock),
						contentDescription = null
					)
				}
			}
		)
	}
}

/**
 * @param maxPosition video duration in millis
 * @param currentPosition current duration in millis
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomBar(
	visible: Boolean,
	maxPosition: Float,
	currentPosition: () -> Float,
	modifier: Modifier = Modifier,
	isPaused: Boolean = false,
	onSeekTo: (Long) -> Unit,
	onPlayPause: () -> Unit,
	onPrevious: () -> Unit,
	onNext: () -> Unit
) {

	val interactionSource = remember { MutableInteractionSource() }

	AnimatedVisibility(
		visible = visible,
		enter = slideInVertically(
			initialOffsetY = { it },
			animationSpec = tween(256)
		) + fadeIn(tween(256)),
		exit = slideOutVertically(
			targetOffsetY = { it },
			animationSpec = tween(256)
		) + fadeOut(tween(256))
	) {
		Column(
			modifier = Modifier
				.padding(16.dp)
				.then(modifier)
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(8.dp),
				modifier = Modifier
					.fillMaxWidth()
			) {
				Text(
					text = formatDuration(currentPosition().toLong()),
					style = MaterialTheme.typography.labelMedium
				)

				Slider(
					value = currentPosition(),
					valueRange = 0f..maxPosition,
					interactionSource = interactionSource,
					thumb = {
						SliderDefaults.Thumb(
							interactionSource = interactionSource,
							colors = SliderDefaults.colors(),
							enabled = true,
							thumbSize = DpSize(16.dp, 16.dp)
						)
					},
					onValueChange = { newValue ->
						onSeekTo(newValue.toLong())
					},
					onValueChangeFinished = {

					},
					modifier = Modifier
						.weight(1f)
				)

				Text(
					text = formatDuration(maxPosition.toLong()),
					style = MaterialTheme.typography.labelMedium
				)
			}

			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.SpaceEvenly,
				modifier = Modifier
					.fillMaxWidth()
			) {
				IconButton(onClick = onPrevious) {
					Icon(
						painter = painterResource(id = R.drawable.ic_previous),
						contentDescription = null
					)
				}

				IconButton(onClick = onPlayPause) {
					Icon(
						painter = painterResource(
							id = if (isPaused) R.drawable.ic_play else R.drawable.ic_pause
						),
						contentDescription = null
					)
				}

				IconButton(onClick = onNext) {
					Icon(
						painter = painterResource(id = R.drawable.ic_next),
						contentDescription = null
					)
				}
			}
		}
	}

}
