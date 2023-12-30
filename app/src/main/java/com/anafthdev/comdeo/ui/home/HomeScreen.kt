package com.anafthdev.comdeo.ui.home

import android.app.Activity
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anafthdev.comdeo.R
import com.anafthdev.comdeo.data.Destination
import com.anafthdev.comdeo.data.DestinationArgument
import com.anafthdev.comdeo.data.Destinations
import com.anafthdev.comdeo.data.SortVideoBy
import com.anafthdev.comdeo.data.model.Video
import com.anafthdev.comdeo.foundation.base.ui.BaseScreenWrapper
import com.anafthdev.comdeo.foundation.extension.toast
import com.anafthdev.comdeo.foundation.uicomponent.CircleCheckbox
import com.anafthdev.comdeo.foundation.uicomponent.ComdeoDropdownMenu
import com.anafthdev.comdeo.foundation.uicomponent.ComdeoDropdownMenuItem
import com.anafthdev.comdeo.foundation.uicomponent.VideoList
import com.anafthdev.comdeo.util.FileUtils
import com.anafthdev.comdeo.util.VideoUtil
import kotlinx.collections.immutable.ImmutableList
import timber.log.Timber

/**
 * First -> label
 *
 * Second -> icon drawable res
 *
 * Third -> allow multiple selection
 */
private val bottomBarIcons: List<Triple<String, Int, Boolean>>
	@Composable
	get() = listOf(
		Triple(stringResource(R.string.share), R.drawable.ic_share, true),
		Triple(stringResource(R.string.change_name), R.drawable.ic_edit_with_underline, false),
		Triple(stringResource(R.string.info), R.drawable.ic_info_circle, false),
		Triple(stringResource(R.string.delete), R.drawable.ic_trash, true),
	)

@Composable
fun HomeScreen(
	viewModel: HomeViewModel,
	navigateTo: (Destination) -> Unit
) {

	val context = LocalContext.current

	val state by viewModel.state.collectAsStateWithLifecycle()

	val deleteLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.StartIntentSenderForResult(),
		onResult = { activityResult ->
			if (activityResult.resultCode == Activity.RESULT_OK) {
				Timber.i("Videos deleted")
				context.toast(context.getString(R.string.video_deleted))
				viewModel.onAction(HomeAction.DeleteVideos(state.selectedVideos))
			}
		}
	)

	BackHandler(state.showVideoCheckbox) {
		viewModel.onAction(HomeAction.ShowVideoCheckbox(false))
	}

	AnimatedVisibility(visible = state.showConfirmationDialog) {
		AlertDialog(
			onDismissRequest = {
				viewModel.onAction(HomeAction.ShowConfirmationDialog(false))
			},
			icon = {
				Icon(
					painter = painterResource(id = R.drawable.ic_trash),
					contentDescription = null
				)
			},
			title = {
				Text(stringResource(id = R.string.delete_video))
			},
			text = {
				Text(stringResource(id = R.string.delete_video_msg))
			},
			confirmButton = {
				Button(
					onClick = {
						VideoUtil.deleteMultipleVideo(
							context = context,
							videos = state.selectedVideos,
							onDeleted = {
								Timber.i("Videos deleted")
								context.toast(context.getString(R.string.video_deleted))
								viewModel.onAction(HomeAction.DeleteVideos(state.selectedVideos))
							}
						)

						viewModel.onAction(HomeAction.ShowConfirmationDialog(false))
					}
				) {
					Text(stringResource(id = R.string.delete))
				}
			},
			dismissButton = {
				TextButton(
					onClick = {
						viewModel.onAction(HomeAction.ShowConfirmationDialog(false))
					}
				) {
					Text(stringResource(id = R.string.cancel))
				}
			}
		)
	}

	BaseScreenWrapper(
		viewModel = viewModel,
		topBar = {
			TopBar(
				videos = state.videos,
				onNavigateTo = navigateTo,
				selectedVideos = state.selectedVideos,
				showVideoCheckbox = state.showVideoCheckbox,
				onCheckAllVideo = { checked ->
					viewModel.onAction(HomeAction.SelectAllVideo(checked))
				},
				onSortVideoBy = { sortVideoBy ->
					viewModel.onAction(HomeAction.SortVideoBy(sortVideoBy))
				},
				onNavigationIconClicked = {
					viewModel.onAction(HomeAction.ShowVideoCheckbox(false))
				}
			)
		},
		bottomBar = {
			AnimatedVisibility(
				visible = state.showVideoCheckbox,
				enter = slideInVertically(
					animationSpec = tween(256),
					initialOffsetY = { it }
				),
				exit = slideOutVertically(
					animationSpec = tween(256),
					targetOffsetY = { it }
				),
			) {
				BottomBar(
					isSelectedVideosEmpty = state.selectedVideos.isEmpty(),
					multipleSelection = state.selectedVideos.size > 1,
					onEditVideoNameClicked = {
						navigateTo(
							Destinations.changeVideoName.createRoute(
								DestinationArgument.ARG_VIDEO_ID to state.selectedVideos[0].id
							)
						)
					},
					onDeleteClicked = {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
							deleteLauncher.launch(
								VideoUtil.deleteMultipleVideo(
									context = context,
									videos = state.selectedVideos
								)
							)
						} else viewModel.onAction(HomeAction.ShowConfirmationDialog(true))
					},
					onShareClicked = {
						FileUtils.shareMultipleFiles(
							context,
							state.selectedVideos.map { it.path.toUri() }
						)
					},
					onInfoClicked = {
						navigateTo(
							Destinations.videoInfo.createRoute(
								DestinationArgument.ARG_VIDEO_ID to state.selectedVideos[0].id
							)
						)
					}
				)
			}
		},
		modifier = Modifier
			.navigationBarsPadding()
	) { scaffoldPadding ->
		HomeScreenContent(
			state = state,
			onClick = { video ->
				if (state.showVideoCheckbox) {
					viewModel.onAction(HomeAction.UpdateSelectedVideo(video))
				} else {
					navigateTo(
						Destinations.video.createRoute(
							DestinationArgument.ARG_VIDEO_ID to video.id
						)
					)
				}
			},
			onLongClick = { video ->
				viewModel.onAction(HomeAction.ShowVideoCheckbox(!state.showVideoCheckbox))
				viewModel.onAction(HomeAction.UpdateSelectedVideo(video))
			},
			modifier = Modifier
				.padding(scaffoldPadding)
		)
	}
}

@Composable
private fun HomeScreenContent(
	state: HomeState,
	modifier: Modifier = Modifier,
	onClick: (Video) -> Unit,
	onLongClick: (Video) -> Unit
) {

	VideoList(
		modifier = modifier,
		videos = state.videos,
		selectedVideos = state.selectedVideos,
		showCheckbox = state.showVideoCheckbox,
		onLongClick = onLongClick,
		onClick = onClick
	)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
	showVideoCheckbox: Boolean,
	selectedVideos: ImmutableList<Video>,
	videos: ImmutableList<Video>,
	modifier: Modifier = Modifier,
	onNavigationIconClicked: () -> Unit,
	onCheckAllVideo: (checkAll: Boolean) -> Unit,
	onNavigateTo: (Destination) -> Unit,
	onSortVideoBy: (SortVideoBy) -> Unit
) {
	TopAppBar(
		modifier = modifier,
		title = {
			if (!showVideoCheckbox) {
				Text(stringResource(id = R.string.video))
			}
		},
		actions = {
			if (showVideoCheckbox) {
				CircleCheckbox(
					checked = videos.size == selectedVideos.size,
					onCheckedChange = onCheckAllVideo
				)
			} else {
				IconButton(
					onClick = {
						onNavigateTo(Destinations.search)
					}
				) {
					Icon(
						painter = painterResource(id = R.drawable.ic_search_normal),
						contentDescription = null
					)
				}

				MoreIconButton(
					onSortBy = onSortVideoBy
				)
			}
		},
		navigationIcon = {
			if (showVideoCheckbox) {
				IconButton(
					onClick = onNavigationIconClicked
				) {
					Icon(
						painter = painterResource(id = R.drawable.ic_close),
						contentDescription = null
					)
				}
			}
		}
	)
}

@Composable
private fun BottomBar(
	multipleSelection: Boolean,
	isSelectedVideosEmpty: Boolean,
	modifier: Modifier = Modifier,
	onEditVideoNameClicked: () -> Unit,
	onDeleteClicked: () -> Unit,
	onShareClicked: () -> Unit,
	onInfoClicked: () -> Unit
) {

	Column(
		modifier = Modifier
			.background(MaterialTheme.colorScheme.background)
	) {
		HorizontalDivider()

		Row(
			horizontalArrangement = Arrangement.SpaceEvenly,
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.padding(8.dp)
				.fillMaxWidth()
				.then(modifier)
		) {
			for ((i, item) in bottomBarIcons.withIndex()) {
				val enabled = (!multipleSelection or item.third) and !isSelectedVideosEmpty

				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.Center,
					modifier = Modifier
						.weight(1f)
						.aspectRatio(1f)
						.clip(CircleShape)
						.clickable(
							enabled = enabled,
						) {
							when (i) {
								0 -> onShareClicked()
								1 -> onEditVideoNameClicked()
								2 -> onInfoClicked()
								3 -> onDeleteClicked()
							}
						}
				) {
					CompositionLocalProvider(
						LocalContentColor provides if (enabled) LocalContentColor.current
						else LocalContentColor.current.copy(alpha = 0.32f)
					) {
						Icon(
							painter = painterResource(id = item.second),
							contentDescription = null,
							tint = LocalContentColor.current
						)

						Spacer(modifier = Modifier.height(4.dp))

						Text(
							text = item.first,
							style = MaterialTheme.typography.labelSmall
						)
					}
				}
			}
		}
	}
}

@Composable
private fun MoreIconButton(
	modifier: Modifier = Modifier,
	onSortBy: (SortVideoBy) -> Unit
) {
	var expanded by remember { mutableStateOf(false) }
	var isSortContent by remember { mutableStateOf(false) }

	val reset: () -> Unit = {
		isSortContent = false
		expanded = false
	}

	IconButton(
		modifier = modifier,
		onClick = {
			expanded = true
		}
	) {
		Icon(
			painter = painterResource(id = R.drawable.ic_more_vert),
			contentDescription = null
		)

		ComdeoDropdownMenu(
			expanded = expanded,
			onDismissRequest = reset
		) {
			if (isSortContent) {
				for (entry in SortVideoBy.entries) {
					ComdeoDropdownMenuItem(
						onClick = {
							onSortBy(entry)
							reset()
						},
						title = {
							Text(text = entry.localizedName)
						}
					)
				}
			} else {
				ComdeoDropdownMenuItem(
					onClick = {
						isSortContent = true
					},
					title = {
						Text(text = stringResource(id = R.string.sort))
					}
				)
			}
		}
	}
}
