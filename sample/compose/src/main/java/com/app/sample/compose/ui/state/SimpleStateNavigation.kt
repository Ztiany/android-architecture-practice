package com.app.sample.compose.ui.state

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

private const val SIMPLE_STATE_ROUTE = "simple_state"

fun NavGraphBuilder.simpleStateScreen(navController: NavHostController) {
    composable(SIMPLE_STATE_ROUTE) {
        SimpleStateScreen()
    }
}

fun NavHostController.navigateToSimpleStateScreen() {
    navigate(SIMPLE_STATE_ROUTE)
}