package com.anafthdev.comdeo.ui.video

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsIgnoringVisibility
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anafthdev.comdeo.R
import com.anafthdev.comdeo.foundation.base.ui.BaseScreenWrapper
import com.anafthdev.comdeo.foundation.common.SystemBarsVisibility
import com.anafthdev.comdeo.foundation.common.installSystemBarsController
import com.anafthdev.comdeo.foundation.common.rememberSystemBarsControllerState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VideoScreen(
	viewModel: VideoViewModel,
	navigateUp: () -> Unit
) {

	val state by viewModel.state.collectAsStateWithLifecycle()

	val systemBarsControllerState = rememberSystemBarsControllerState(
		initialNavigationBarVisibility = SystemBarsVisibility.Gone,
		initialStatusBarVisibility = SystemBarsVisibility.Gone
	)

	installSystemBarsController(systemBarsControllerState)

	BaseScreenWrapper(
		viewModel = viewModel,
		topBar = {
			TopBar(
				state = state,
				visible = systemBarsControllerState.isSystemBarVisible,
				onNavigationIconClicked = navigateUp
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
	modifier: Modifier = Modifier
) {

	Column(
		modifier = modifier
	) {

	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
	state: VideoState,
	visible: Boolean,
	modifier: Modifier = Modifier,
	onNavigationIconClicked: () -> Unit,
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
				IconButton(
					onClick = {
						// TODO: Maximize/Minimize
					}
				) {
					Icon(
						painter = painterResource(id = R.drawable.ic_maximize_3),
						contentDescription = null
					)
				}

				IconButton(
					onClick = {
						// TODO: Headphone/Video
					}
				) {
					Icon(
						painter = painterResource(id = R.drawable.ic_headphone),
						contentDescription = null
					)
				}

				IconButton(
					onClick = {
						// TODO: Lock/Unlock
					}
				) {
					Icon(
						painter = painterResource(id = R.drawable.ic_unlock),
						contentDescription = null
					)
				}
			}
		)
	}
}
