package com.anafthdev.comdeo.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.anafthdev.comdeo.foundation.base.BaseScreenWrapper

@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {

    BaseScreenWrapper(
        viewModel = viewModel
    ) { scaffoldPadding ->
        HomeScreenContent(
            modifier = Modifier
                .padding(scaffoldPadding)
        )
    }
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier
) {

}
