package com.anafthdev.comdeo.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anafthdev.comdeo.R
import com.anafthdev.comdeo.foundation.base.ui.BaseScreenWrapper
import com.anafthdev.comdeo.foundation.uicomponent.VideoList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    navigateUp: () -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    BaseScreenWrapper(
        viewModel = viewModel,
        topBar = {
            TopAppBar(
                title = {
                    BasicTextField(
                        value = state.query,
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = LocalContentColor.current
                        ),
                        onValueChange = { query ->
                            viewModel.onAction(SearchAction.SetQuery(query))
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            }
                        ),
                        cursorBrush = Brush.linearGradient(listOf(LocalContentColor.current, LocalContentColor.current)),
                        decorationBox = { innerTextField ->
                            Box{
                                if (state.query.isEmpty()) {
                                    Text(
                                        text = stringResource(id = R.string.search_by_name),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }

                                innerTextField()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.onAction(SearchAction.SetQuery(""))
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { scaffoldPadding ->
        SearchScreenContent(
            state = state,
            modifier = Modifier
                .padding(scaffoldPadding)
        )
    }
}

@Composable
private fun SearchScreenContent(
    state: SearchState,
    modifier: Modifier = Modifier
) {

    VideoList(
        videos = state.result,
        onVideoClicked = { video ->

        },
        modifier = modifier
    )

}
