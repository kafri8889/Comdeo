package com.anafthdev.comdeo.ui.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anafthdev.comdeo.data.Destinations
import com.anafthdev.comdeo.ui.change_video_name.ChangeVideoNameScreen
import com.anafthdev.comdeo.ui.home.HomeScreen
import com.anafthdev.comdeo.ui.search.SearchScreen
import com.anafthdev.comdeo.ui.video_info.VideoInfoScreen
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterialApi::class)
@Composable
fun Comdeo(
	viewModel: ComdeoViewModel
) {

	val modalBottomSheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		skipHalfExpanded = true
	)

	val bottomSheetNavigator = remember {
		BottomSheetNavigator(modalBottomSheetState)
	}

	val navController = rememberNavController(bottomSheetNavigator)

	ModalBottomSheetLayout(
		bottomSheetNavigator = bottomSheetNavigator,
		sheetBackgroundColor = MaterialTheme.colorScheme.surface,
		sheetContentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.surface),
		scrimColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.32f),
		sheetShape = MaterialTheme.shapes.large
	) {
		NavHost(
			navController = navController,
			startDestination = Destinations.home.route,
			enterTransition = {
				slideIntoContainer(
					towards = AnimatedContentTransitionScope.SlideDirection.Start,
					animationSpec = tween(512)
				)
			},
			exitTransition = {
				slideOutOfContainer(
					towards = AnimatedContentTransitionScope.SlideDirection.Start,
					animationSpec = tween(512)
				)
			},
			popEnterTransition = {
				slideIntoContainer(
					towards = AnimatedContentTransitionScope.SlideDirection.End,
					animationSpec = tween(512)
				)
			},
			popExitTransition = {
				slideOutOfContainer(
					towards = AnimatedContentTransitionScope.SlideDirection.End,
					animationSpec = tween(512)
				)
			}
		) {
			composable(Destinations.home.route) { backEntry ->
				HomeScreen(
					viewModel = hiltViewModel(backEntry),
					navigateTo = { destination ->
						navController.navigate(destination.route)
					}
				)
			}

			composable(Destinations.search.route) { backEntry ->
				SearchScreen(
					viewModel = hiltViewModel(backEntry),
					navigateUp = navController::popBackStack
				)
			}

			bottomSheet(
				route = Destinations.videoInfo.route,
				arguments = Destinations.videoInfo.arguments
			) { backEntry ->
				VideoInfoScreen(
					viewModel = hiltViewModel(backEntry)
				)
			}

			bottomSheet(
				route = Destinations.changeVideoName.route,
				arguments = Destinations.changeVideoName.arguments
			) { backEntry ->
				ChangeVideoNameScreen(
					viewModel = hiltViewModel(backEntry),
					onNavigateUp = navController::popBackStack
				)
			}
		}
	}
}
