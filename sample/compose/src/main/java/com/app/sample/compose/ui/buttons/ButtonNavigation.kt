package com.app.sample.compose.ui.buttons

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

private const val ROUTE_NAME = "buttons"

fun NavGraphBuilder.buttonComponentScreen(navController: NavHostController) {
    composable(ROUTE_NAME) {
        ButtonsScreen()
    }
}

fun NavHostController.navigateToButtonComponentScreen() {
    navigate(ROUTE_NAME)
}

