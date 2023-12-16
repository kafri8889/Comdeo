package com.anafthdev.comdeo.ui.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anafthdev.comdeo.data.Destinations
import com.anafthdev.comdeo.ui.home.HomeScreen
import com.anafthdev.comdeo.ui.search.SearchScreen

@Composable
fun Comdeo(
	viewModel: ComdeoViewModel
) {

	val navController = rememberNavController()

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
	}
}
