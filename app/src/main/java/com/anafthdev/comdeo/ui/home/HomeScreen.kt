package com.anafthdev.comdeo.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anafthdev.comdeo.R
import com.anafthdev.comdeo.data.Destination
import com.anafthdev.comdeo.data.Destinations
import com.anafthdev.comdeo.data.SortVideoBy
import com.anafthdev.comdeo.data.model.Video
import com.anafthdev.comdeo.foundation.base.ui.BaseScreenWrapper
import com.anafthdev.comdeo.foundation.uicomponent.CircleCheckbox
import com.anafthdev.comdeo.foundation.uicomponent.ComdeoDropdownMenu
import com.anafthdev.comdeo.foundation.uicomponent.ComdeoDropdownMenuItem
import com.anafthdev.comdeo.foundation.uicomponent.VideoList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateTo: (Destination) -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    BackHandler(state.showVideoCheckbox) {
        viewModel.onAction(HomeAction.ShowVideoCheckbox(false))
    }

    BaseScreenWrapper(
        viewModel = viewModel,
        topBar = {
            TopAppBar(
                title = {
                    if (!state.showVideoCheckbox) {
                        Text(stringResource(id = R.string.video))
                    }
                },
                actions = {
                    if (state.showVideoCheckbox) {
                        CircleCheckbox(
                            checked = state.videos.size == state.selectedVideos.size,
                            onCheckedChange = { checked ->
                                viewModel.onAction(HomeAction.SelectAllVideo(checked))
                            }
                        )
                    } else {
                        IconButton(
                            onClick = {
                                navigateTo(Destinations.search)
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search_normal),
                                contentDescription = null
                            )
                        }

                        MoreIconButton(
                            onSortBy = { sortVideoBy ->
                                viewModel.onAction(HomeAction.SortVideoBy(sortVideoBy))
                            }
                        )
                    }
                },
                navigationIcon = {
                    if (state.showVideoCheckbox) {
                        IconButton(
                            onClick = {
                                viewModel.onAction(HomeAction.ShowVideoCheckbox(false))
                            }
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
    ) { scaffoldPadding ->
        HomeScreenContent(
            state = state,
            onClick = { video ->
                if (state.showVideoCheckbox) {
                    viewModel.onAction(HomeAction.UpdateSelectedVideo(video))
                } else {

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
