package com.anafthdev.comdeo.ui.video

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.anafthdev.comdeo.R
import com.anafthdev.comdeo.foundation.base.ui.BaseScreenWrapper
import com.anafthdev.comdeo.foundation.common.SystemBarsVisibility
import com.anafthdev.comdeo.foundation.common.formatDuration
import com.anafthdev.comdeo.foundation.common.installSystemBarsController
import com.anafthdev.comdeo.foundation.common.rememberSystemBarsControllerState
import com.anafthdev.comdeo.foundation.uicomponent.ObserveLifecycle
import com.anafthdev.comdeo.foundation.uicomponent.VideoPlayer
import timber.log.Timber

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
			.apply {
				viewModel.setExoPlayer(this)

				addListener(
					object : Player.Listener {
						override fun onIsPlayingChanged(isPlaying: Boolean) {
							super.onIsPlayingChanged(isPlaying)
							viewModel.onAction(VideoAction.SetIsPlaying(isPlaying))
						}
					}
				)
			}
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

	BackHandler {
		systemBarsControllerState.showSystemBar()
		navigateUp()
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
				isPlaying = state.isPlaying,
				maxPosition = (state.video?.duration ?: 0L).toFloat(),
				currentPosition = { state.currentPosition.toFloat() },
				onSeekTo = { pos ->
					exoPlayer.apply {
						seekTo(pos)
					}
				},
				onSliderDragged = { isPressed ->
					Timber.i("onPositionBeingChanged: isPressed $isPressed")

					if (isPressed) exoPlayer.pause()
					else exoPlayer.play()
				},
				onPlayPause = {
					if (exoPlayer.isPlaying) exoPlayer.pause()
					else exoPlayer.play()
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
	) { _ ->
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
	isPlaying: Boolean = false,
	onSliderDragged: (isDragged: Boolean) -> Unit,
	onSeekTo: (position: Long) -> Unit,
	onPlayPause: () -> Unit,
	onPrevious: () -> Unit,
	onNext: () -> Unit
) {

	val interactionSource = remember { MutableInteractionSource() }

	var sliderProgress by remember { mutableFloatStateOf(0f) }

	val isSliderDragged by interactionSource.collectIsDraggedAsState()

	LaunchedEffect(currentPosition()) {
		sliderProgress = currentPosition()
	}

	LaunchedEffect(isSliderDragged) {
		onSliderDragged(isSliderDragged)
	}

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
					text = formatDuration(sliderProgress.toLong()),
					style = MaterialTheme.typography.labelMedium
				)

				Slider(
					value = sliderProgress,
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
						sliderProgress = newValue
						onSeekTo(sliderProgress.toLong())
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
							id = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
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
