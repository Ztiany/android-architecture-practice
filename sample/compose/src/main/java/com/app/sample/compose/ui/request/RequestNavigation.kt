package com.app.sample.compose.ui.request

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

private const val REQUEST_STATE_ROUTE = "request"

fun NavGraphBuilder.requestScreen(navController: NavHostController) {
    composable(REQUEST_STATE_ROUTE) {
        RequestScreen()
    }
}

fun NavHostController.navigateToRequestHandlingScreen() {
    navigate(REQUEST_STATE_ROUTE)
}