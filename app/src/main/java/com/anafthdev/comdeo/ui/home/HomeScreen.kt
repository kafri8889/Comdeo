package com.anafthdev.comdeo.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anafthdev.comdeo.R
import com.anafthdev.comdeo.data.Destination
import com.anafthdev.comdeo.data.Destinations
import com.anafthdev.comdeo.foundation.base.ui.BaseScreenWrapper
import com.anafthdev.comdeo.foundation.uicomponent.VideoList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateTo: (Destination) -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    BaseScreenWrapper(
        viewModel = viewModel,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.video))
                },
                actions = {
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

                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_more_vert),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { scaffoldPadding ->
        HomeScreenContent(
            state = state,
            modifier = Modifier
                .padding(scaffoldPadding)
        )
    }
}

@Composable
private fun HomeScreenContent(
    state: HomeState,
    modifier: Modifier = Modifier
) {

    VideoList(
        videos = state.videos,
        onVideoClicked = { video ->

        },
        modifier = modifier
    )

}
