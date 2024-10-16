package com.app.sample.compose.ui.state

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

private const val REFRESH_STATE_ROUTE = "refresh_state"

fun NavGraphBuilder.refreshStateScreen(navController: NavHostController) {
    composable(REFRESH_STATE_ROUTE) {
        RefreshStateScreen()
    }
}

fun NavHostController.navigateToRefreshStateScreen() {
    navigate(REFRESH_STATE_ROUTE)
}