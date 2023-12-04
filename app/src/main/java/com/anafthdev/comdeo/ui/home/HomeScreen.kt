package com.anafthdev.comdeo.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anafthdev.comdeo.R
import com.anafthdev.comdeo.foundation.base.ui.BaseScreenWrapper
import com.anafthdev.comdeo.foundation.uicomponent.VideoItem
import com.bumptech.glide.integration.compose.rememberGlidePreloadingData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreenContent(
    state: HomeState,
    modifier: Modifier = Modifier
) {

    val preloadingData = rememberGlidePreloadingData(
        dataSize = state.videos.size,
        dataGetter = { i -> state.videos[i] },
        preloadImageSize = Size(512f, 512f)
    ) { video, requestBuilder ->
        requestBuilder.load(video.path.toUri())
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(preloadingData.size) { index ->
            val (video, preloadRequest) = preloadingData[index]

            VideoItem(
                video = video,
                onClick = {

                }
            )
        }
    }

}
