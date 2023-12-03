package com.anafthdev.comdeo.ui.app

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anafthdev.comdeo.data.Destinations
import com.anafthdev.comdeo.ui.home.HomeScreen

@Composable
fun Comdeo() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destinations.home.route
    ) {
        composable(Destinations.home.route) { backEntry ->
            HomeScreen(viewModel = hiltViewModel(backEntry))
        }
    }
}
